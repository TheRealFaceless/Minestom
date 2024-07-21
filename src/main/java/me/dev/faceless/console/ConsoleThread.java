package me.dev.faceless.console;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;

import java.util.Scanner;

public class ConsoleThread implements Runnable {
    private final ConsoleSender consoleSender = new ConsoleSender();
    private static boolean running = true;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (!input.trim().isEmpty()) processCommand(input);
            }
        }
    }

    public static void start() {
        if(running) return;
        Thread thread = new Thread(new ConsoleThread());
        thread.setName("Console Thread");
        thread.start();
    }

    public static void stop() {
        running = false;
    }


    private void processCommand(String command) {
        MinecraftServer.getCommandManager().execute(consoleSender, command);
    }
}
