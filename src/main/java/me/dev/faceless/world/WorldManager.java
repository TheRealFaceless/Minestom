package me.dev.faceless.world;

import lombok.Getter;
import lombok.Setter;
import me.dev.faceless.Main;
import me.dev.faceless.utils.FileUtils;
import me.dev.faceless.world.generators.TestSimplex;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;

import java.io.File;

@Getter
@Setter
public class WorldManager {

    private final File worldFile;
    private final Task saveTask;
    private final InstanceContainer instanceContainer;
    
    private static final Logger LOGGER = Main.getLogger();

    public WorldManager(InstanceManager manager) {
        worldFile = new File(FileUtils.getServerFolder(), "worlds/world");
        if (!worldFile.exists()) {
            if (!worldFile.mkdirs()) throw new RuntimeException("Failed to create directories: " + worldFile.getAbsolutePath());
        }

        this.instanceContainer = manager.createInstanceContainer(new AnvilLoader("worlds/world"));
        instanceContainer.setChunkSupplier(LightingChunk::new);
        instanceContainer.enableAutoChunkLoad(true);
        instanceContainer.setGenerator(new TestSimplex());

        saveTask = MinecraftServer.getSchedulerManager().buildTask(this::save)
                .delay(TaskSchedule.seconds(300))
                .repeat(TaskSchedule.seconds(300))
                .schedule();
    }


    public void saveChunk(Chunk chunk) {
        instanceContainer.saveChunkToStorage(chunk);
        instanceContainer.saveInstance();
        LOGGER.info("Saving chunk {}", chunk);
    }

    public void save() {
        instanceContainer.saveChunksToStorage();
        instanceContainer.saveInstance();
        LOGGER.info("Saving all chunks");
    }

    public void stopSaveTask() {
        if (saveTask != null && saveTask.isAlive()) saveTask.cancel();
        LOGGER.info("Disabling World save task");
    }
}
