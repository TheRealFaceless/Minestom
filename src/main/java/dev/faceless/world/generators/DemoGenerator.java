package dev.faceless.world.generators;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class DemoGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fillHeight(0, 1, Block.BEDROCK);
        unit.modifier().fillHeight(1, 100, Block.STONE);
        unit.modifier().fillHeight(100, 119, Block.DIRT);
        unit.modifier().fillHeight(119, 120, Block.GRASS_BLOCK);
    }
}
