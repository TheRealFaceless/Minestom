package me.dev.faceless.utils;

import net.minestom.server.instance.Chunk;

public class WorldUtils {

    public static boolean hasViewer(Chunk chunk) {
        return !chunk.getViewers().isEmpty();
    }
}
