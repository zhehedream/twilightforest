package twilightforest.world;

import java.util.Random;

import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;

public class TFGenTorchBerries extends TFGenerator {
    public boolean generate(World world, Random rand, int x, int y, int z) {
        int copyX = x;
        int copyZ = z;

        for (; y > 5; --y) {
            if (world.isAirBlock(x, y, z) && BlockTFPlant.canPlaceRootBelow(world, x, y + 1, z) && rand.nextInt(6) > 0) {
                world.setBlock(x, y, z, TFBlocks.plant, 13, 2);
                break;
            } else {
                x = copyX + rand.nextInt(4) - rand.nextInt(4);
                z = copyZ + rand.nextInt(4) - rand.nextInt(4);
            }
        }
        return true;
    }
}
