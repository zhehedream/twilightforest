package twilightforest.world;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

public class TFGenHangingLamps extends TFGenerator {
    private static final int MAX_HANG = 8;

    public boolean generate(World world, Random rand, int x, int y, int z) {
        // this must be an air block, surrounded by air
        if (world.isAirBlock(x, y, z) && TFGenerator.surroundedByAir(world, x, y, z)) {
            // there should be leaves or wood within 12 blocks above
            if (areLeavesAbove(world, x, y, z)) {
                // we need to be at least 4 above ground
                if (isClearBelow(world, x, y, z)) {
                    // generate lamp
                    world.setBlock(x, y, z, TFBlocks.fireflyJar);

                    for (int cy = 1; cy < MAX_HANG; cy++) {
                        Material above = world.getBlock(x, y + cy, z).getMaterial();
                        if (above.isSolid() || above == Material.leaves) {
                            break;
                        } else {
                            world.setBlock(x, y + cy, z, Blocks.fence);
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean areLeavesAbove(World world, int x, int y, int z) {
        boolean areLeavesAbove = false;
        for (int cy = 1; cy < MAX_HANG; cy++) {
            Material above = world.getBlock(x, y + cy, z).getMaterial();
            if (above.isSolid() || above == Material.leaves) {
                areLeavesAbove = true;
            }
        }
        return areLeavesAbove;
    }

    private boolean isClearBelow(World world, int x, int y, int z) {
        boolean isClearBelow = true;
        for (int cy = 1; cy < 4; cy++) {
            if (World.doesBlockHaveSolidTopSurface(world, x, y - cy, z)) {
                isClearBelow = false;
            }
        }
        return isClearBelow;
    }
}
