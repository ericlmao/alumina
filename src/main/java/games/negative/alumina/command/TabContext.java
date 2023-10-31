package games.negative.alumina.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents the context of a tab completion.
 * @param args The arguments of the command.
 * @param sender The sender of the command.
 */
public record TabContext(@NotNull CommandSender sender, @NotNull String[] args) {

    /**
     * Returns the player who executed the command.
     * @return the player who executed the command.
     */
    public Optional<Player> player() {
        return sender() instanceof Player ? Optional.of((Player) sender()) : Optional.empty();
    }

    /**
     * Returns the argument at the specified index.
     * @param index The index of the argument.
     * @return the argument at the specified index.
     */
    @NotNull
    public Optional<String> argument(int index) {
        return (index >= args.length ? Optional.empty() : Optional.of(args[index]));
    }

    /**
     * Returns the current argument used in the command.
     * @return the current argument.
     */
    @NotNull
    public String current() {
        return argument(args.length - 1).orElseThrow(() -> new IllegalStateException("No current argument"));
    }

}
