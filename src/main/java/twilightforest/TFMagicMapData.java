package twilightforest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

import twilightforest.world.ChunkProviderTwilightForest;
import twilightforest.world.TFWorldChunkManager;
import twilightforest.world.WorldProviderTwilightForest;

public class TFMagicMapData extends MapData {

    private static final int FEATURE_DATA_BYTE = 18;
    public List<MapCoord> featuresVisibleOnMap = new ArrayList<>();
    public static final byte CONQ_OFFSET = 80;

    public TFMagicMapData(String par1Str) {
        super(par1Str);
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        byte[] featureStorage = par1NBTTagCompound.getByteArray("features");
        if (featureStorage.length > 0) {
            this.updateMPMapData(featureStorage);
        }
        // else {
        // System.out.println("Can't find feature storage for " + this.mapName);
        // }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        if (this.featuresVisibleOnMap.size() > 0) {
            byte[] featureStorage = makeFeatureStorageArray();
            par1NBTTagCompound.setByteArray("features", featureStorage);
        }

    }

    /**
     * Adds a twilight forest feature to the map.
     */
    public void addFeatureToMap(TFFeature feature, int x, int z, boolean conq) {
        byte relativeX = (byte) ((x - this.xCenter) >> this.scale);
        byte relativeZ = (byte) ((z - this.zCenter) >> this.scale);
        byte rangeX = 64;
        byte rangeY = 64;

        if (relativeX >= (-rangeX) && relativeZ >= (-rangeY) && relativeX <= rangeX && relativeZ <= rangeY) {
            byte markerIcon = (byte) feature.featureID;
            markerIcon += conq ? CONQ_OFFSET : 0;
            byte mapX = (byte) (relativeX << 1);
            byte mapZ = (byte) (relativeZ << 1);
            byte mapRotation = 8;

            boolean featureFound = false;

            // look for a feature already at those coordinates
            for (MapCoord existingCoord : featuresVisibleOnMap) {
                if (existingCoord.centerX == mapX && existingCoord.centerZ == mapZ) {
                    featureFound = true;
                }
            }

            // if we didn't find it, add it
            if (!featureFound) {
                this.featuresVisibleOnMap.add(new MapCoord(markerIcon, mapX, mapZ, mapRotation));
            }
        }
    }

    /**
     * Checks existing features against the feature cache and removes/changes wrong ones. Does not add features.
     */
    public void checkExistingFeatures(World world) {
        ArrayList<MapCoord> toRemove = null;

        for (MapCoord coord : featuresVisibleOnMap) {

            // System.out.printf("Existing feature at %d, %d.\n", coord.centerX, coord.centerZ);

            int worldX = (coord.centerX << this.scale - 1) + this.xCenter;
            int worldZ = (coord.centerZ << this.scale - 1) + this.zCenter;

            if (world != null && world.getWorldChunkManager() instanceof TFWorldChunkManager tfManager) {
                if (coord.iconSize == 0) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<>();
                    }
                    toRemove.add(coord);
                } else if (coord.iconSize < CONQ_OFFSET) {
                    ChunkProviderTwilightForest chunkProvider = ((WorldProviderTwilightForest) world.provider)
                            .getChunkProvider();
                    coord.iconSize = (byte) tfManager.getFeatureID(worldX, worldZ, world);
                    if (chunkProvider.isStructureConquered(worldX, 0, worldZ)) {
                        coord.iconSize += CONQ_OFFSET;
                    }
                }
            }
        }

        if (toRemove != null) {
            featuresVisibleOnMap.removeAll(toRemove);
        }
    }

    /**
     * Updates the client's map with information from other players in MP
     */
    @Override
    public void updateMPMapData(byte[] par1ArrayOfByte) {
        if (par1ArrayOfByte[0] == FEATURE_DATA_BYTE) {
            // this is feature data, we can handle that directly
            this.featuresVisibleOnMap.clear();

            for (int i = 0; i < (par1ArrayOfByte.length - 1) / 3; ++i) {
                byte markerIcon = par1ArrayOfByte[i * 3 + 1];
                byte mapX = par1ArrayOfByte[i * 3 + 2];
                byte mapZ = par1ArrayOfByte[i * 3 + 3];
                byte mapRotation = 8;
                this.featuresVisibleOnMap.add(new MapCoord(markerIcon, mapX, mapZ, mapRotation));
            }
        } else {
            super.updateMPMapData(par1ArrayOfByte);
        }
    }

    /**
     * This makes a byte array that we store our feature data in to send with map update packets
     */
    public byte[] makeFeatureStorageArray() {
        byte[] storage = new byte[this.featuresVisibleOnMap.size() * 3 + 1];
        storage[0] = FEATURE_DATA_BYTE;

        for (int i = 0; i < featuresVisibleOnMap.size(); ++i) {
            MapCoord featureCoord = this.featuresVisibleOnMap.get(i);
            storage[i * 3 + 1] = (featureCoord.iconSize);
            storage[i * 3 + 2] = featureCoord.centerX;
            storage[i * 3 + 3] = featureCoord.centerZ;
        }
        return storage;
    }
}
