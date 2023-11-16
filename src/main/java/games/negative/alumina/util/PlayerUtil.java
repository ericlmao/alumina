package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;

/**
 * Player utility to handle some player-related tasks.
 */
public class PlayerUtil {

    /**
     * Reset the player to default properties.
     * @param player The player to reset.
     */
    public static void reset(final Player player) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        player.setWalkSpeed(0.2F);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getOpenInventory().close();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20);
        resetHealth(player);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        if (player.getVehicle() != null)
            player.leaveVehicle();

        if (!player.getPassengers().isEmpty())
            player.getPassengers().forEach(Entity::leaveVehicle);
    }

    /**
     * Reset the player's health.
     * @param player The player to reset.
     */
    private static void resetHealth(final Player player) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        Collection<AttributeModifier> modifiers = attribute.getModifiers();
        if (modifiers.isEmpty()) return;

        List<AttributeModifier> toRemove = Lists.newArrayList(modifiers);
        toRemove.forEach(attribute::removeModifier);
    }
}
