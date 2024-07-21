package me.dev.faceless;

import me.dev.faceless.commands.GamemodeCommand;
import me.dev.faceless.commands.StopCommand;
import me.dev.faceless.commands.TeleportCommand;
import me.dev.faceless.console.ConsoleThread;
import lombok.Getter;
import me.dev.faceless.listeners.GeneralListeners;
import me.dev.faceless.world.WorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
public class Main {

    @Getter private static final Logger Logger = LoggerFactory.getLogger(Main.class);
    @Getter private static final MinecraftServer minecraftServer = MinecraftServer.init();
    @Getter private static final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
    @Getter private static final CommandManager commandManager = MinecraftServer.getCommandManager();
    @Getter private static final GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
    @Getter private static final WorldManager worldManager = new WorldManager(instanceManager);

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        MojangAuth.init();

        commandManager.register(new StopCommand());
        commandManager.register(new GamemodeCommand());
        commandManager.register(new TeleportCommand());
        commandManager.setUnknownCommandCallback((sender, command) -> sender.sendMessage(Component.text("Unknown command", NamedTextColor.RED)));

        eventHandler.addChild(GeneralListeners.ALL_NODE);

        Runtime.getRuntime().addShutdownHook(new Thread(Main.disable()));
        ConsoleThread.start();

        minecraftServer.start("localhost", 25565);
        Logger.info("Server Started in {}ms", System.currentTimeMillis() - startTime);
    }


    public static Runnable disable() {
        return () -> {
            Logger.info("Stopping server...");
            final long startTime = System.currentTimeMillis();
            try {
                ConsoleThread.stop();
                worldManager.stopSaveTask();
                worldManager.save();
            } finally {
                MinecraftServer.getConnectionManager().shutdown();
                MinecraftServer.stopCleanly();
                Logger.info("Server stopped in {}ms", System.currentTimeMillis() - startTime);
            }
        };
    }

    private static void tryPreloadClass(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("An expected class  " + className + " was not found for preloading: " + e.getMessage());
        }
    }

}