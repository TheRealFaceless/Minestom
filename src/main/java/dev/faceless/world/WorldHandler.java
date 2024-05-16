package dev.faceless.world;

import dev.faceless.world.generators.DemoGenerator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.*;

public class WorldHandler {
    public static InstanceContainer instanceContainer;

    public static void init() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(new DemoGenerator());
        instanceContainer.enableAutoChunkLoad(true);
        instanceContainer.setChunkSupplier(LightingChunk::new);
        instanceContainer.setTimeRate(0);
        instanceContainer.setTime(12000);

        loadChunks();
    }

    public static void loadChunks() {
        for(int i = 0; i < 100; i++) {
            instanceContainer.loadChunk(i, i);
        }
    }
}
