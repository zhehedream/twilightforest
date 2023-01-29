package twilightforest.world;

import java.util.Random;

import net.minecraft.world.World;

import twilightforest.block.BlockTFTrollRoot;
import twilightforest.block.TFBlocks;

public class TFGenTrollRoots extends TFGenerator {

    public boolean generate(World world, Random random, int x, int y, int z) {
        int copyX = x;
        int copyZ = z;

        for (; y > 5; --y) {
            if (world.isAirBlock(x, y, z) && BlockTFTrollRoot.canPlaceRootBelow(world, x, y + 1, z)
                    && random.nextInt(6) > 0) {
                if (random.nextInt(10) == 0) {
                    world.setBlock(x, y, z, TFBlocks.unripeTrollBer);
                } else {
                    world.setBlock(x, y, z, TFBlocks.trollVidr);
                }
            } else {
                x = copyX + random.nextInt(4) - random.nextInt(4);
                z = copyZ + random.nextInt(4) - random.nextInt(4);
            }
        }
        return true;
    }
}
