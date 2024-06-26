package twilightforest.structures.minotaurmaze;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;

public class ComponentTFMazeCorridorRoots extends ComponentTFMazeCorridor {

    public ComponentTFMazeCorridorRoots() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ComponentTFMazeCorridorRoots(int i, int x, int y, int z, int rotation) {
        super(i, x, y, z, rotation);
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
        for (int x = 1; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                int freq = x;
                if (rand.nextInt(freq + 2) > 0) {
                    int length = rand.nextInt(6);

                    // place dirt above ceiling
                    this.placeBlockAtCurrentPosition(world, Blocks.dirt, 0, x, 6, z, sbb);

                    // roots
                    for (int y = 6 - length; y < 6; y++) {
                        this.placeBlockAtCurrentPosition(
                                world,
                                TFBlocks.plant,
                                BlockTFPlant.META_ROOT_STRAND,
                                x,
                                y,
                                z,
                                sbb);
                    }

                    // occasional gravel
                    if (rand.nextInt(freq + 1) > 1) {
                        this.placeBlockAtCurrentPosition(world, Blocks.gravel, 0, x, 1, z, sbb);

                        if (rand.nextInt(freq + 1) > 1) {
                            this.placeBlockAtCurrentPosition(world, Blocks.gravel, 0, x, 2, z, sbb);
                        }
                    }
                }
            }
        }
        return true;
    }

}
