package me.dev.faceless.properties;

import me.dev.faceless.Main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class PropertiesUpdater {

    public static void updatePropertiesFile(String fileName) {
        Properties defaultProperties = new Properties();

        // Custom properties
        defaultProperties.setProperty("minestom.online-mode", "true");
        defaultProperties.setProperty("minestom.server-port", "25565");

        // Default properties setup
        defaultProperties.setProperty("minestom.shutdown-on-signal", "true");
        defaultProperties.setProperty("minestom.tps", "20");
        defaultProperties.setProperty("minestom.max-tick-catch-up", "5");
        defaultProperties.setProperty("minestom.chunk-view-distance", "8");
        defaultProperties.setProperty("minestom.entity-view-distance", "5");
        defaultProperties.setProperty("minestom.entity-synchronization-ticks", "20");
        defaultProperties.setProperty("minestom.workers", Integer.toString(Runtime.getRuntime().availableProcessors()));
        defaultProperties.setProperty("minestom.dispatcher-threads", "1");
        defaultProperties.setProperty("minestom.max-packet-size", "2097151");
        defaultProperties.setProperty("minestom.send-buffer-size", "262143");
        defaultProperties.setProperty("minestom.receive-buffer-size", "32767");
        defaultProperties.setProperty("minestom.pooled-buffer-size", "262143");
        defaultProperties.setProperty("minestom.packet-per-tick", "20");
        defaultProperties.setProperty("minestom.packet-queue-size", "1000");
        defaultProperties.setProperty("minestom.send-light-after-block-placement-delay", "100");
        defaultProperties.setProperty("minestom.keep-alive-delay", "10000");
        defaultProperties.setProperty("minestom.keep-alive-kick", "30000");
        defaultProperties.setProperty("minestom.login-plugin-message-timeout", "5000");
        defaultProperties.setProperty("minestom.chunk-queue.min-per-tick", "0.01");
        defaultProperties.setProperty("minestom.chunk-queue.max-per-tick", "64.0");
        defaultProperties.setProperty("minestom.chunk-queue.multiplier", "1");
        defaultProperties.setProperty("minestom.grouped-packet", "true");
        defaultProperties.setProperty("minestom.cached-packet", "true");
        defaultProperties.setProperty("minestom.viewable-packet", "true");
        defaultProperties.setProperty("minestom.tag-handler-cache", "true");
        defaultProperties.setProperty("minestom.serialization.serialize-empty-nbt-compound", "false");
        defaultProperties.setProperty("minestom.auth.url", "https://sessionserver.mojang.com/session/minecraft/hasJoined");
        defaultProperties.setProperty("minestom.world-border-size", "29999984");
        defaultProperties.setProperty("minestom.map.rgbmapping", "lazy");
        defaultProperties.setProperty("minestom.map.rgbreduction", "");
        defaultProperties.setProperty("minestom.registry.late-register", "false");
        defaultProperties.setProperty("minestom.registry.unsafe-ops", "false");
        defaultProperties.setProperty("minestom.event.multiple-parents", "false");
        defaultProperties.setProperty("minestom.inside-test", "false");

        Properties existingProperties = new Properties();
        File file = new File(fileName);
        if (file.isDirectory()) {
            Main.getLogger().error("The path '{}' is a directory, not a file.", fileName);
            return;
        }

        if (!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    Main.getLogger().error("Error creating server properties file.");
                    return;
                }
            } catch (IOException e) {
                Main.getLogger().error("Failed to create new properties file.", e);
                return;
            }
        }

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                existingProperties.load(fis);
            } catch (IOException e) {
                Main.getLogger().error("Failed to read existing properties file.", e);
                return;
            }
        }

        Map<String, String> missingProperties = new HashMap<>();
        for (String name : defaultProperties.stringPropertyNames()) {
            if (!existingProperties.containsKey(name)) {
                missingProperties.put(name, defaultProperties.getProperty(name));
            }
        }

        if (!missingProperties.isEmpty()) {
            Map<String, String> allProperties = new TreeMap<>();
            for (Map.Entry<Object, Object> entry : existingProperties.entrySet()) {
                allProperties.put((String) entry.getKey(), (String) entry.getValue());
            }
            allProperties.putAll(missingProperties);

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
                writer.println("# Updated server properties");
                writer.println("# " + new java.util.Date());

                for (Map.Entry<String, String> entry : allProperties.entrySet()) {
                    writeProperty(writer, entry.getKey(), entry.getValue());
                }

                Main.getLogger().warn("Modified server.properties with missing properties added");
            } catch (IOException e) {
                Main.getLogger().error("Failed to write properties file.", e);
            }
        }

        loadProperties(fileName);
    }

    private static void writeProperty(PrintWriter writer, String key, String value) {
        writer.printf("%s=%s%n", key, value);
    }

    private static void loadProperties(String fileName) {
        Main.getLogger().info("Loading server.properties");
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
            for (String name : properties.stringPropertyNames()) {
                System.setProperty(name, properties.getProperty(name));
            }
        } catch (IOException e) {
            Main.getLogger().error("Failed to load properties file.", e);
        }
    }
}