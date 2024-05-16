package dev.faceless.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop");
        addSyntax(this::execute);
    }

    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        MinecraftServer.stopCleanly();
        commandSender.sendMessage("Stopping Server...");
    }
}