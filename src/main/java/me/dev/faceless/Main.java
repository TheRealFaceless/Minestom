package me.dev.faceless;

import me.dev.faceless.console.ConsoleThread;
import lombok.Getter;
import me.dev.faceless.listeners.GeneralListeners;
import me.dev.faceless.properties.PropertiesUpdater;
import me.dev.faceless.world.WorldManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Main {
    private final static long startTime = System.currentTimeMillis();
    @Getter private static final Logger Logger = LoggerFactory.getLogger(Main.class);

    static {
        PropertiesUpdater.updatePropertiesFile("server.properties");
    }

    @Getter private static final MinecraftServer minecraftServer = MinecraftServer.init();
    @Getter private static final InstanceManager instanceManager = MinecraftServer.getInstanceManager();
    @Getter private static final CommandManager commandManager = MinecraftServer.getCommandManager();
    @Getter private static final GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
    @Getter private static final WorldManager worldManager = new WorldManager(instanceManager);

    public static void main(String[] args) {
        Commands.register(commandManager);
        setupEventHandlers();
        setupShutdownHook();

        initMojangAuth();
        startServer();
        Logger.info("Server Started in {}ms", System.currentTimeMillis() - startTime);
    }

    private static void initMojangAuth() {
        boolean onlineMode = Boolean.parseBoolean(System.getProperty("minestom.online-mode", "true"));
        if (onlineMode) MojangAuth.init();
        else Logger.warn("WARNING: Online mode is OFF. The server is not verifying player accounts with Mojang.");
    }

    private static void setupEventHandlers() {
        eventHandler.addChild(GeneralListeners.ALL_NODE);
    }

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(disable()));
        ConsoleThread.start();
    }

    private static void startServer() {
        minecraftServer.start("localhost", Integer.parseInt(System.getProperty("minestom.server-port")));
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
}
