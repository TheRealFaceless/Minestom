package me.dev.faceless;

import me.dev.faceless.commands.GamemodeCommand;
import me.dev.faceless.commands.StopCommand;
import me.dev.faceless.commands.TeleportCommand;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandManager;

public class Commands {

    public static void register(CommandManager commandManager) {
        commandManager.register(new StopCommand());
        commandManager.register(new GamemodeCommand());
        commandManager.register(new TeleportCommand());
        commandManager.setUnknownCommandCallback((sender, command) ->
                sender.sendMessage(Component.text("Unknown command. Type \"/help\" for help."))
        );
    }
}
