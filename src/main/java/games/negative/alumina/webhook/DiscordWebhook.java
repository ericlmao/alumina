/*
 *  MIT License
 *
 * Copyright (C) 2025 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package games.negative.alumina.webhook;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import games.negative.alumina.webhook.exception.WebhookException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ToString
public class DiscordWebhook implements ConfigurationSerializable {

    private Author author;
    private String title;
    private String description;
    private String content;
    private int color;
    private Field[] fields;
    private Image thumbnail;
    private Image image;
    private Footer footer;

    private String username;
    private String avatar_url;

    // Parse Color into rgb index
    public static int parseColor(@NotNull Color color) {
        int rgb = color.getRed();
        rgb = (rgb << 8) + color.getGreen();
        rgb = (rgb << 8) + color.getBlue();
        return rgb;
    }

    @SneakyThrows
    public boolean send(@NotNull String destination) {
        JsonObject message = new JsonObject();

        message.addProperty("username", this.username);
        message.addProperty("avatar_url", this.avatar_url);
        message.addProperty("content", this.content);

        JsonArray embeds = new JsonArray();
        JsonObject embed = new JsonObject();
        if (this.author != null)
            embed.add("author", this.author.get());

        embed.addProperty("title", this.title);
        embed.addProperty("description", this.description);
        embed.addProperty("color", this.color);

        JsonArray fields = new JsonArray();

        for (Field f : this.fields) {
            fields.add(f.get());
        }

        if (!fields.isEmpty())
            embed.add("fields", fields);

        if (this.thumbnail != null)
            embed.add("thumbnail", this.thumbnail.get());
        if (this.image != null)
            embed.add("imagine", this.image.get());
        if (this.footer != null)
            embed.add("footer", this.footer.get());

        embeds.add(embed);
        message.add("embeds", embeds);

        URL url = new URL(destination);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Alumina-Library-Webhook");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        @Cleanup OutputStream stream = connection.getOutputStream();
        stream.write(message.toString().getBytes(StandardCharsets.UTF_8));
        stream.flush();

        connection.getInputStream().close();
        connection.disconnect();
        return true;
    }

    /**
     * Returns a new instance of the Builder class, used to create DiscordWebhook objects with customizable properties.
     * Each method in the Builder class sets a specific property of the DiscordWebhook object being built.
     *
     * @return A new instance of the Builder class.
     */
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        if (title != null) map.put("title", title);
        if (description != null) map.put("description", description);
        if (content != null) map.put("content", content);

        map.put("color", color);

        if (fields != null) {
            Map<String, Object> fieldsMap = Maps.newHashMap();
            for (int i = 0; i < fields.length; i++) {
                fieldsMap.put("field" + i, fields[i]);
            }
            map.put("fields", fieldsMap);
        }

        if (thumbnail != null) map.put("thumbnail", thumbnail);

        if (image != null) map.put("image", image);

        if (footer != null) map.put("footer", footer);

        if (author != null) map.put("author", author);

        if (username != null) map.put("username", username);

        if (avatar_url != null) map.put("avatar_url", avatar_url);

        return map;
    }

    public static DiscordWebhook fromYaml(@NotNull ConfigurationSection section) {
        DiscordWebhook webhook = new DiscordWebhook();

        if (section.contains("title")) webhook.title = section.getString("title");
        if (section.contains("description")) webhook.description = section.getString("description");
        if (section.contains("content")) webhook.content = section.getString("content");

        webhook.color = section.getInt("color");

        if (section.contains("fields")) {
            ConfigurationSection fieldsSection = section.getConfigurationSection("fields");
            Preconditions.checkNotNull(fieldsSection, "Fields section cannot be null");

            webhook.fields = new Field[fieldsSection.getKeys(false).size()];
            for (String key : fieldsSection.getKeys(false)) {
                webhook.fields[Integer.parseInt(key.substring(5))] = (Field) fieldsSection.get(key);
            }
        }

        if (section.contains("thumbnail")) webhook.thumbnail = section.getSerializable("thumbnail", Image.class);

        if (section.contains("image")) webhook.image = section.getSerializable("image", Image.class);

        if (section.contains("footer")) webhook.footer = section.getSerializable("footer", Footer.class);

        if (section.contains("author")) webhook.author = section.getSerializable("author", Author.class);

        if (section.contains("username")) webhook.username = section.getString("username");

        if (section.contains("avatar_url")) webhook.avatar_url = section.getString("avatar_url");

        return webhook;
    }

    /**
     * This class represents a field in a Discord message.
     * It implements the ConfigurationSerializable interface for serialization purposes.
     */
    public static class Field implements ConfigurationSerializable {

        private final String name;
        private final String value;
        private final boolean inline;

        public Field(String name, String value, boolean inline) {
            this.name = name;
            if (name != null && name.length() > Constants.FIELD_NAME_LIMIT)
                throw new WebhookException("Field name cannot be longer than " + Constants.FIELD_NAME_LIMIT + " characters (" + name.length() + ")");

            this.value = value;
            if (value != null && value.length() > Constants.FIELD_VALUE_LIMIT)
                throw new WebhookException("Field value cannot be longer than " + Constants.FIELD_VALUE_LIMIT + " characters (" + value.length() + ")");

            this.inline = inline;
        }

        @NotNull
        public JsonElement get() {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", this.name);
            obj.addProperty("value", this.value);
            obj.addProperty("inline", this.inline);

            return obj;
        }

        @NotNull
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = Maps.newHashMap();

            map.put("name", name);
            map.put("value", value);
            map.put("inline", inline);

            return map;
        }
    }

    /**
     * The Image class represents an image for a webhook message.
     * It implements the ConfigurationSerializable interface, allowing for serialization and deserialization of Image objects.
     */
    @ToString
    public static class Image implements ConfigurationSerializable {

        private final String url;

        public Image(@NotNull String url) {
            this.url = url;
        }

        @NotNull
        public JsonObject get() {
            JsonObject obj = new JsonObject();
            obj.addProperty("url", this.url);
            return obj;
        }

        @NotNull
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = Maps.newHashMap();

            map.put("url", url);

            return map;
        }
    }

    /**
     * Represents the footer of a Discord webhook message.
     */
    @ToString
    public static class Footer implements ConfigurationSerializable {

        private final String text;
        private final String icon_url;

        public Footer(String text, String icon_url) {
            if (text == null && icon_url == null) throw new WebhookException("Both footer values (text, icon_url) cannot be null");

            this.text = text;
            if (text != null && text.length() > Constants.FOOTER_TEXT_LIMIT)
                throw new WebhookException("Footer text cannot be longer than " + Constants.FOOTER_TEXT_LIMIT + " characters (" + text.length() + ")");

            this.icon_url = icon_url;
        }

        @NotNull
        public JsonElement get() {
            JsonObject obj = new JsonObject();
            obj.addProperty("text", this.text);
            obj.addProperty("icon_url", this.icon_url);

            return obj;
        }

        @NotNull
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = Maps.newHashMap();

            map.put("text", text);
            map.put("icon_url", icon_url);

            return map;
        }
    }

    /**
     * The Author class represents an author for a webhook message.
     * It implements the ConfigurationSerializable interface, allowing for serialization and deserialization of Author objects.
     */
    @ToString
    public static class Author implements ConfigurationSerializable {

        private final String name;
        private final String url;
        private final String icon;

        public Author(@Nullable String name, @Nullable String url, @Nullable String icon) {
            if (name == null && url == null && icon == null) throw new WebhookException("All author values (name, url, icon) cannot be null");

            this.name = name;
            if (name != null && name.length() > Constants.AUTHOR_NAME_LIMIT)
                throw new WebhookException("Author name cannot be longer than " + Constants.AUTHOR_NAME_LIMIT + " characters (" + name.length() + ")");

            this.url = url;
            this.icon = icon;
        }

        @NotNull
        public JsonElement get() {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", this.name);
            obj.addProperty("url", this.url);
            obj.addProperty("icon_url", this.icon);
            return obj;
        }

        @NotNull
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = Maps.newHashMap();

            map.put("name", name);
            map.put("url", url);
            map.put("icon", icon);

            return map;
        }
    }

    /**
     * The Builder class is used to create DiscordWebhook objects with customizable properties.
     * Each method in the Builder class sets a specific property of the DiscordWebhook object being built.
     */
    public static class Builder {

        private final DiscordWebhook webhook;

        /**
         * The Builder class is responsible for creating an instance of DiscordWebhook with default values.
         */
        public Builder() {
            this.webhook = new DiscordWebhook();
        }

        /**
         * Sets the author of the webhook message.
         *
         * @param author The author of the message. Can be null.
         * @return The Builder object.
         */
        @NotNull
        public Builder author(@Nullable Author author) {
            this.webhook.author = author;
            return this;
        }

        /**
         * Sets the author of the webhook message.
         *
         * @param name The name of the author. Can be null.
         * @param url The URL of the author. Can be null.
         * @param icon The icon URL of the author. Can be null.
         * @return The Builder object.
         * @throws WebhookException If all author values (name, url, icon) are null.
         */
        @NotNull
        public Builder author(@Nullable String name, @Nullable String url, @Nullable String icon) {
            if (name == null && url == null && icon == null) {
                this.webhook.author = null;
                return this;
            }

            this.webhook.author = new Author(name, url, icon);
            return this;
        }

        /**
         * Sets the title for the Discord webhook.
         *
         * @param title the title to set for the webhook (can be null)
         * @return the updated instance of the Builder
         */
        @NotNull
        public Builder title(@Nullable String title) {
            this.webhook.title = title;
            return this;
        }

        /**
         * Sets the description of the webhook.
         *
         * @param description the description to set for the webhook
         * @return the Builder object
         * @throws WebhookException if the description exceeds the character limit
         */
        @NotNull
        public Builder description(@Nullable String description) {
            if (description != null && description.length() > Constants.DESCRIPTION_TEXT_LIMIT)
                throw new WebhookException("Description cannot be longer than " + Constants.DESCRIPTION_TEXT_LIMIT + " characters (" + description.length() + ")");

            this.webhook.description = description;
            return this;
        }

        /**
         * Sets the content of the Discord webhook message.
         *
         * @param content the content to be set. Null is allowed.
         * @return the updated Builder object.
         * @throws WebhookException if the content exceeds the maximum character limit.
         */
        @NotNull
        public Builder content(@Nullable String content) {
            if (content != null && content.length() > Constants.CONTENT_TEXT_LIMIT)
                throw new WebhookException("Content cannot be longer than " + Constants.CONTENT_TEXT_LIMIT + " characters (" + content.length() + ")");

            this.webhook.content = content;
            return this;
        }

        /**
         * Sets the color of the Discord webhook.
         *
         * @param color the color to set
         * @return the Builder object
         */
        @NotNull
        public Builder color(@Nullable Color color) {
            if (color == null) {
                this.webhook.color = 0;
                return this;
            }

            int parse = parseColor(color);
            return color(parse);
        }

        /**
         * Sets the color of the webhook.
         *
         * @param color the color value to be set
         * @return the updated Builder object
         * @throws WebhookException if the color value is negative
         */
        @NotNull
        public Builder color(int color) {
            if (color < 0) throw new WebhookException("Color value cannot be negative!");

            this.webhook.color = color;
            return this;
        }

        /**
         * Sets the fields for the webhook.
         *
         * @param fields The fields to set for the webhook. Can be null or empty array.
         * @return The Builder object.
         * @throws WebhookException If the number of fields exceeds the field limit.
         */
        @NotNull
        public Builder fields(@Nullable Field... fields) {
            if (fields != null && fields.length > Constants.FIELD_LIMIT)
                throw new WebhookException("Fields cannot be more than " + Constants.FIELD_LIMIT + " (" + fields.length + ")");

            this.webhook.fields = fields;
            return this;
        }

        /**
         * Sets the thumbnail image for the webhook.
         *
         * @param thumbnail The thumbnail image to set. Can be null.
         * @return The Builder object.
         */
        @NotNull
        public Builder thumbnail(@Nullable Image thumbnail) {
            this.webhook.thumbnail = thumbnail;
            return this;
        }

        /**
         * Sets the image for the webhook.
         *
         * @param image the image to set for the webhook
         * @return the modified instance of the Builder class
         */
        @NotNull
        public Builder image(@Nullable Image image) {
            this.webhook.image = image;
            return this;
        }


        /**
         * Sets the footer of the webhook message.
         *
         * @param footer The footer object containing the text and icon URL. Can be null.
         * @return The Builder object.
         * @throws WebhookException If both text and icon URL are null.
         */
        @NotNull
        public Builder footer(@Nullable Footer footer) {
            this.webhook.footer = footer;
            return this;
        }

        /**
         * Sets the footer of the webhook message.
         *
         * @param text The text to be displayed in the footer. Can be null.
         * @param icon_url The URL of the icon to be displayed in the footer. Can be null.
         * @return The Builder object.
         * @throws WebhookException If both text and icon_url are null.
         */
        @NotNull
        public Builder footer(@Nullable String text, @Nullable String icon_url) {
            if (text == null && icon_url == null) {
                this.webhook.footer = null;
                return this;
            }

            this.webhook.footer = new Footer(text, icon_url);
            return this;
        }

        /**
         * Sets the username for the Discord webhook.
         *
         * @param username the username to set for the webhook (nullable)
         * @return the Builder object to allow for method chaining
         */
        @NotNull
        public Builder username(@Nullable String username) {
            this.webhook.username = username;
            return this;
        }

        /**
         * Sets the avatar URL for the Discord webhook message.
         *
         * @param url The URL of the avatar image. Can be null.
         * @return The Builder object.
         */
        @NotNull
        public Builder avatar(@Nullable String url) {
            this.webhook.avatar_url = url;
            return this;
        }

        /**
         * Builds and returns a DiscordWebhook object.
         *
         * @return A DiscordWebhook object.
         */
        @NotNull
        public DiscordWebhook build() {
            return this.webhook;
        }
    }

    /**
     * This class contains constants used in the DiscordWebhook library.
     */
    public static class Constants {

        /**
         * The maximum character limit for an author's name.
         * This limit applies to the name of the author who is attributed for a specific action or content.
         * The author's name should not exceed this limit to ensure proper display and readability.
         */
        public static final int AUTHOR_NAME_LIMIT = 256;

        /**
         * The maximum character limit for the text in the footer of a Discord message.
         * This limit applies to the text that is displayed in the footer section of a Discord message.
         * The footer text should not exceed this limit in order to ensure proper display and readability.
         */
        public static final int FOOTER_TEXT_LIMIT = 2048;

        /**
         * The maximum limit for the number of fields in a Discord message.
         * This limit ensures that the number of fields does not exceed a certain threshold,
         * which can help maintain readability and prevent unintended issues or errors.
         * The value of this constant is 10, meaning that the number of fields should not exceed 10.
         */
        public static final int FIELD_LIMIT = 10;

        /**
         * The maximum character limit for a field name in DiscordWebhook library.
         * This limit ensures that the length of a field name does not exceed a certain threshold,
         * which can help maintain readability and prevent unintended issues or errors.
         * The value of this constant is 256, meaning that the length of a field name should not exceed 256 characters.
         */
        public static final int FIELD_NAME_LIMIT = 256;

        /**
         * The maximum character limit for the value of a field in the DiscordWebhook library.
         * This limit ensures that the length of a field value does not exceed a certain threshold,
         * which can help maintain readability and prevent unintended issues or errors.
         * The value of this constant is 1024, meaning that the length of a field value should not exceed 1024 characters.
         */
        public static final int FIELD_VALUE_LIMIT = 1024;

        /**
         * The maximum character limit for the description text.
         * The description text is used to provide additional information or details about something.
         * Commonly used in various applications and libraries, such as DiscordWebhook.
         * The description text limit helps ensure that the length of the text does not exceed a certain threshold,
         * which can help maintain readability and prevent unintended issues or errors.
         *
         * The value of this constant is 2048, meaning that the description text should not exceed 2048 characters in length.
         */
        public static final int DESCRIPTION_TEXT_LIMIT = 2048;

        /**
         * The maximum limit for the content text in a Discord message.
         * This constant is defined in the Constants class in the DiscordWebhook library.
         * The limit is set to 2048 characters.
         */
        public static final int CONTENT_TEXT_LIMIT = 2048;

    }
}
