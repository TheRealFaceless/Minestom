package dev.faceless;

import dev.faceless.commands.GamemodeCommand;
import dev.faceless.commands.StopCommand;
import dev.faceless.data.FilesInit;
import dev.faceless.listeners.ServerSetupHandler;
import dev.faceless.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extras.MojangAuth;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        MinecraftServer minecraftServer = MinecraftServer.init();

        FilesInit.init();
        MojangAuth.init();
        WorldHandler.init();
        ServerSetupHandler.init();

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new StopCommand());
        commandManager.register(new GamemodeCommand());

        minecraftServer.start("0.0.0.0", 25565);

        System.out.println("Server Started in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static String getJarName() {
        try {
            String pathToJar = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(pathToJar, StandardCharsets.UTF_8);
            return new File(decodedPath).getName();
        } catch (Exception e) {
            System.err.println("Error getting jar name: " + e.getMessage());
            return null;
        }
    }

    public static File getServerFolder() {
        return new File(System.getProperty("user.dir"));
    }
}