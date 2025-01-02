/*
 * MIT License
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
 */

package games.negative.alumina.skin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import games.negative.alumina.logger.Logs;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class MineSkinGenerator {

    private final String api = "https://api.mineskin.org/generate/url";
    private final String agent = "alumina/API";
    private final String cacheControl = "no-cache";
    private final String contentType = "application/json";
    private final String accept = "application/json";
    private final int connectTimeout = 2000;
    private final int readTimeout = 30000;

    @NotNull
    @CheckReturnValue
    public Optional<JsonObject> fromURL(@NotNull String url, boolean slim) {
        DataOutputStream out = null;
        InputStreamReader reader = null;

        try {
            URL target = new URI(api).toURL();
            HttpURLConnection connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", agent);
            connection.setRequestProperty("Cache-Control", cacheControl);
            connection.setRequestProperty("Accept", accept);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            out = new DataOutputStream(connection.getOutputStream());

            JsonObject request = new JsonObject();
            request.addProperty("url", url);
            request.addProperty("name", "");

            if (slim) request.addProperty("variant", "slim");

            out.writeBytes(request.toString().replace("\\", ""));
            out.close();

            reader = new InputStreamReader(connection.getInputStream());
            String str = CharStreams.toString(reader);

            if (connection.getResponseCode() != 200) {
                Logs.severe("[MineSkin (Response 200)] Failed to fetch skin from URL: " + url);
                return Optional.empty();
            }

            JsonObject output = JsonParser.parseString(str).getAsJsonObject();
            JsonObject data = output.getAsJsonObject("data");

            connection.disconnect();
            return Optional.of(data);
        } catch (URISyntaxException | IOException e) {
            Logs.severe("[MineSkin] Failed to fetch skin from URL: " + url);
            Logs.severe(e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logs.severe("[MineSkin] Failed to close output stream.");
                    Logs.severe(e.toString());
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Logs.severe("[MineSkin] Failed to close reader.");
                        Logs.severe(e.toString());
                    }
                }
            }
        }
        return Optional.empty();
    }

    @NotNull
    public CompletableFuture<Optional<JsonObject>> fetch(@NotNull String url, boolean slim) {
        return CompletableFuture.supplyAsync(() -> fromURL(url, slim));
    }

    @NotNull
    public CompletableFuture<ItemStack> fetchSkull(@NotNull String url) {
        return CompletableFuture.supplyAsync(() -> getSkull(url));
    }

    public ItemStack getSkull(@NotNull String url) {
        Optional<JsonObject> data = fromURL(url, false);
        if (data.isEmpty()) return null;

        JsonObject object = data.get();

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        Preconditions.checkArgument(item.getItemMeta() instanceof SkullMeta, "ItemMeta is not a SkullMeta.");
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        URL texture = null;
        try {
            texture = new URI(object.get("texture").getAsJsonObject().get("url").getAsString()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            Logs.severe("[MineSkin] Failed to fetch skin from URL: " + url);
            Logs.severe(e.toString());
        }

        Preconditions.checkNotNull(texture, "Texture URL is null.");

        textures.setSkin(texture);
        profile.setTextures(textures);

        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);

        return item;
    }
}
