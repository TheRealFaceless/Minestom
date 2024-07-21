package me.dev.faceless.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;
import java.util.Locale;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode", "gm");

        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);
        gamemode.setCallback((sender, exception) ->
                sender.sendMessage(
                        Component.text("Invalid gamemode ", NamedTextColor.RED)
                                .append(Component.text(exception.getInput(), NamedTextColor.WHITE))
                                .append(Component.text("!"))));

        ArgumentEntity player = ArgumentType.Entity("targets").onlyPlayers(true);

        setDefaultExecutor((sender, context) -> {
            String commandName = context.getCommandName();

            sender.sendMessage(Component.text("Usage: /" + commandName + " <gamemode> [targets]", NamedTextColor.RED));
        });

        //Command Syntax for /gamemode <gamemode>
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(Component.text("Please run this command in-game.", NamedTextColor.RED));
                return;
            }

            if (p.getPermissionLevel() < 2) {
                sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
                return;
            }

            GameMode mode = context.get(gamemode);

            executeSelf(p, mode);
        }, gamemode);

        //Command Syntax for /gamemode <gamemode> [targets]
        addSyntax((sender, context) -> {
            if (sender instanceof Player p && p.getPermissionLevel() < 2) {
                sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
                return;
            }

            EntityFinder finder = context.get(player);
            GameMode mode = context.get(gamemode);

            executeOthers(sender, mode, finder.find(sender));
        }, gamemode, player);
    }

    /**
     * Sets the gamemode for the specified entities, and
     * notifies them (and the sender) in the chat.
     */
    private void executeOthers(CommandSender sender, GameMode mode, List<Entity> entities) {
        if (entities.isEmpty()) {
            //If there are no players that could be modified, display an error message
            if (sender instanceof Player)
                sender.sendMessage(Component.translatable("argument.entity.notfound.player", NamedTextColor.RED));
            else sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
        } else for (Entity entity : entities) {
            if (entity instanceof Player p) {
                if (p == sender) {
                    //If the player is the same as the sender, call
                    //executeSelf to display one message instead of two
                    executeSelf((Player) sender, mode);
                } else {
                    p.setGameMode(mode);

                    String gamemodeString = "gameMode." + mode.name().toLowerCase(Locale.ROOT);
                    Component gamemodeComponent = Component.translatable(gamemodeString);
                    Component playerName = p.getDisplayName() == null ? p.getName() : p.getDisplayName();

                    //Send a message to the changed player and the sender
                    p.sendMessage(Component.translatable("gameMode.changed", gamemodeComponent));
                    sender.sendMessage(Component.translatable("commands.gamemode.success.other", playerName, gamemodeComponent));
                }
            }
        }
    }

    /**
     * Sets the gamemode for the executing Player, and
     * notifies them in the chat.
     */
    private void executeSelf(Player sender, GameMode mode) {
        sender.setGameMode(mode);


        String gamemodeString = "gameMode." + mode.name().toLowerCase(Locale.ROOT);
        Component gamemodeComponent = Component.translatable(gamemodeString);

        sender.sendMessage(Component.translatable("commands.gamemode.success.self", gamemodeComponent));
    }
}