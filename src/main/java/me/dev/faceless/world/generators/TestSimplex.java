package me.dev.faceless.world.generators;

import me.dev.faceless.utils.SimplexNoise;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TestSimplex implements Generator {
    private final Random random = new Random();

    public TestSimplex() {
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        Point start = unit.absoluteStart();
        Point size = unit.size();
        
        int startX = start.blockX();
        int startZ = start.blockZ();
        int endX = startX + size.blockX();
        int endZ = startZ + size.blockZ();

        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                int height = getHeight(x, z);
                for (int y = 0; y <= height; y++) {
                    Block block = getBlock(y, height);
                    unit.modifier().setRelative(x - startX, y, z - startZ, block);
                }
            }
        }
    }

    private int getHeight(int x, int z) {
        double noiseValue = SimplexNoise.noise(x * 0.01, z * 0.01);
        return (int) (noiseValue * 20 + 64);
    }

    private Block getBlock(int y, int height) {
        if (y == 0) {
            return Block.BEDROCK;
        } else if (y < height - 4) {
            return Block.STONE;
        } else if (y < height - 1) {
            return Block.DIRT;
        } else {
            return Block.GRASS_BLOCK;
        }
    }
}
