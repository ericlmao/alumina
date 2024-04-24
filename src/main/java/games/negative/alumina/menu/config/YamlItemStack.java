package games.negative.alumina.menu.config;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.exlll.configlib.Configuration;
import games.negative.alumina.builder.ItemBuilder;
import games.negative.alumina.model.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Represents a configurable item stack.
 */
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class YamlItemStack {

    private String displayName = null;
    private Material material = null;
    private Integer amount = null;
    private Integer slot = null;
    private Boolean glowing = null;
    private List<String> lore = null;
    private String headTextureValue = null;
    private String headTextureSignature = null;

    /**
     * Converts this YamlItemStack to an {@link ItemStack}.
     * @return The ItemStack.
     */
    @NotNull
    public ItemStack asItemStack(@Nullable String... placeholders) {
        Preconditions.checkNotNull(material, "Material must not be null");

        ItemBuilder builder = new ItemBuilder(material, (amount == null ? 1 : amount));

        if (displayName != null) {
            builder.setName(displayName);

            if (placeholders != null) {
                List<Pair<String, String>> mapped = Lists.newArrayList();

                Preconditions.checkArgument(placeholders.length % 2 == 0, "Placeholders must be in pairs");
                for (int i = 0; i < placeholders.length; i += 2) {
                    mapped.add(new Pair<>(placeholders[i], placeholders[i + 1]));
                }

                for (Pair<String, String> pair : mapped) {
                    builder.replaceName(pair.left(), pair.right());
                }
            }

        }

        if (lore != null && !lore.isEmpty()) {
            builder.setLore(lore);

            if (placeholders != null) {
                List<Pair<String, String>> mapped = Lists.newArrayList();

                Preconditions.checkArgument(placeholders.length % 2 == 0, "Placeholders must be in pairs");
                for (int i = 0; i < placeholders.length; i += 2) {
                    mapped.add(new Pair<>(placeholders[i], placeholders[i + 1]));
                }

                for (Pair<String, String> pair : mapped) {
                    builder.replaceLore(pair.left(), pair.right());
                }
            }
        }

        if (material == Material.PLAYER_HEAD && headTextureValue != null && headTextureSignature != null) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", headTextureValue, headTextureSignature));

            builder.setSkullOwner(profile);
        }

        if (glowing != null && glowing) {
            builder.addEnchantment(Enchantment.LUCK, 10);
            builder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        return builder.build();
    }
}
