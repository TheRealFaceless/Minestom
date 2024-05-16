package dev.faceless.listeners;

import dev.faceless.world.WorldHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import net.minestom.server.utils.time.TimeUnit;

import java.time.Duration;

public class ServerSetupHandler {

    private static final EventNode<Event> DEMO_NODE = EventNode.all("demo")
            .addListener(EntityAttackEvent.class, event -> {
                final Entity source = event.getEntity();
                final Entity entity = event.getTarget();

                entity.takeKnockback(0.4f, Math.sin(source.getPosition().yaw() * 0.017453292), -Math.cos(source.getPosition().yaw() * 0.017453292));

                if (entity instanceof Player target) {
                    target.damage(Damage.fromEntity(source, 5));
                }

                if (source instanceof Player) {
                    ((Player) source).sendMessage("You attacked something!");
                }
            })
            .addListener(PickupItemEvent.class, event -> {
                final Entity entity = event.getLivingEntity();
                if (entity instanceof Player) {
                    final ItemStack itemStack = event.getItemEntity().getItemStack();
                    event.setCancelled(!((Player) entity).getInventory().addItemStack(itemStack));
                }
            })
            .addListener(ItemDropEvent.class, event -> {
                final Player player = event.getPlayer();
                ItemStack droppedItem = event.getItemStack();

                Pos playerPos = player.getPosition();
                ItemEntity itemEntity = new ItemEntity(droppedItem);
                itemEntity.setPickupDelay(Duration.of(500, TimeUnit.MILLISECOND));
                itemEntity.setInstance(player.getInstance(), playerPos.withY(y -> y + 1.5));
                Vec velocity = playerPos.direction().mul(20);
                itemEntity.setVelocity(velocity);
            })
            .addListener(AsyncPlayerConfigurationEvent.class, (event)-> {
                Player player = event.getPlayer();
                event.setSpawningInstance(WorldHandler.instanceContainer);
                player.setRespawnPoint(new Pos(0, 120, 0));
                player.setPermissionLevel(4);
            })
            .addListener(ServerListPingEvent.class, event -> {
                ResponseData responseData = event.getResponseData();
                if(event.getConnection() != null) {
                    responseData.addEntry(NamedAndIdentified.named(Component.text("Connection Info:").decorate(TextDecoration.UNDERLINED)));
                    String ip = event.getConnection().getServerAddress();
                    responseData.addEntry(NamedAndIdentified.named(Component.text('-', NamedTextColor.DARK_GRAY)
                            .append(Component.text(" IP: ", NamedTextColor.GRAY))
                            .append(Component.text(ip != null ? ip : "???", NamedTextColor.DARK_GRAY))));
                    responseData.addEntry(NamedAndIdentified.named(Component.text('-', NamedTextColor.DARK_GRAY)
                            .append(Component.text(" PORT: ", NamedTextColor.GRAY))
                            .append(Component.text(event.getConnection().getServerPort()))));
                    responseData.addEntry(NamedAndIdentified.named(Component.text('-', NamedTextColor.DARK_GRAY)
                            .append(Component.text(" VERSION: ", NamedTextColor.GRAY))
                            .append(Component.text(event.getConnection().getProtocolVersion()))));
                    responseData.addEntry(NamedAndIdentified.named(Component.text('-')
                            .append(Component.text(" YOUR SOCKET ADDRESS: ", NamedTextColor.GRAY))
                            .append(Component.text(event.getConnection().getRemoteAddress().toString().replace("/", ""), NamedTextColor.DARK_GRAY))));
                }
                responseData.setDescription(Component.text("This is a Minestom Server \nHover over player count sir toast", NamedTextColor.BLUE));
            });

    public static void init() {
        MinecraftServer.getGlobalEventHandler().addChild(DEMO_NODE);
        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        MinestomAdventure.COMPONENT_TRANSLATOR = (c, l) -> c;
    }
}
