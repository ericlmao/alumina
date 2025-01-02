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
import games.negative.alumina.logger.Logs;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class MinecraftSkinGenerator {

    @NotNull
    public CompletableFuture<ItemStack> fetchSkull(@NotNull String url) {
        return CompletableFuture.supplyAsync(() -> getSkull(url));
    }

    public ItemStack getSkull(@NotNull String url) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        Preconditions.checkArgument(item.getItemMeta() instanceof SkullMeta, "ItemMeta is not a SkullMeta.");
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        URL texture = null;
        try {
            texture = new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            Logs.severe("[Minecraft Skin] Failed to fetch skin from URL: " + url);
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
