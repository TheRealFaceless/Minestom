package me.dev.faceless.utils;

import me.dev.faceless.Main;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String getJarName() {
        try {
            String pathToJar = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(pathToJar, StandardCharsets.UTF_8);
            return new File(decodedPath).getName();
        } catch (Exception e) {
            Main.getLogger().info("Error getting jar name: {}", e.getMessage());
            return null;
        }
    }

    public static File getServerFolder() {
        return new File(System.getProperty("user.dir"));
    }
}
