package dev.faceless.blockhandlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GravityBlockHandler implements BlockHandler {

    @Override
    public void onPlace(@NotNull BlockHandler.Placement placement) {
        BlockHandler.super.onPlace(placement);
    }

    @Override
    public void onDestroy(@NotNull BlockHandler.Destroy destroy) {
        BlockHandler.super.onDestroy(destroy);
    }

    @Override
    public boolean onInteract(@NotNull BlockHandler.Interaction interaction) {
        return BlockHandler.super.onInteract(interaction);
    }

    @Override
    public void onTouch(@NotNull BlockHandler.Touch touch) {
        BlockHandler.super.onTouch(touch);
    }

    @Override
    public void tick(@NotNull BlockHandler.Tick tick) {
        BlockHandler.super.tick(tick);
    }

    @Override
    public boolean isTickable() {
        return BlockHandler.super.isTickable();
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return BlockHandler.super.getBlockEntityTags();
    }

    @Override
    public byte getBlockEntityAction() {
        return BlockHandler.super.getBlockEntityAction();
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minestom:gravity-block-handler");
    }
}