package me.dev.faceless.listeners;

import me.dev.faceless.Main;
import me.dev.faceless.utils.WorldUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChunkUnloadEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class GeneralListeners {

    private static final Random random = new Random(System.currentTimeMillis());
    private static final double ITEM_DROP_VELOCITY_MULTIPLIER = 6;

    public static final  EventNode<Event> ALL_NODE = EventNode.all("i-hear-all")
            .addListener(AsyncPlayerConfigurationEvent.class, event -> {
                final Player player = event.getPlayer();
                event.setSpawningInstance(Main.getWorldManager().getInstanceContainer());
                player.setPermissionLevel(4);
                player.setGameMode(GameMode.CREATIVE);
                player.setRespawnPoint(new Pos(random.nextInt(0, 10), 0, random.nextInt(0, 10)));

            }).addListener(ItemDropEvent.class, event -> {
                final Player player = event.getPlayer();
                ItemStack droppedItem = event.getItemStack();
                Pos playerPos = player.getPosition();
                ItemEntity itemEntity = new ItemEntity(droppedItem);
                itemEntity.setPickupDelay(Duration.of(500, TimeUnit.MILLISECOND));
                itemEntity.setInstance(player.getInstance(), playerPos.withY(y -> y + 1.5));
                Vec velocity = playerPos.direction().mul(ITEM_DROP_VELOCITY_MULTIPLIER);
                itemEntity.setVelocity(velocity);
            }).addListener(PickupItemEvent.class, event-> {
                ItemStack itemStack = event.getItemStack();
                if(event.getEntity() instanceof Player player) player.getInventory().addItemStack(itemStack);
            }).addListener(PlayerChunkUnloadEvent.class, event-> {
                if(event.getEntity().getInstance() == null) return;
                final Chunk chunk = event.getInstance().getChunk(event.getChunkX(), event.getChunkZ());
                if(chunk == null) return;
                save(chunk);
            });

    private static void save(@NotNull Chunk... chunks) {
        Arrays.stream(chunks)
                .filter(WorldUtils::hasViewer)
                .forEach(chunk -> {
                    Main.getWorldManager().saveChunk(chunk);
                    chunk.invalidate();
                });
    }
}
