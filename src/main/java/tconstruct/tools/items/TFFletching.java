package tconstruct.tools.items;

import net.minecraft.item.ItemStack;

import mantle.items.abstracts.CraftingItem;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.util.IToolPart;

public class TFFletching extends CraftingItem implements IToolPart {

    public TFFletching() {
        super(toolMaterialNames, buildTextureNames("_fletching"), "parts/", "tinker", TConstructRegistry.materialTab);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    private static String[] buildTextureNames(String textureType) {
        String[] names = new String[toolMaterialNames.length];
        for (int i = 0; i < toolMaterialNames.length; i++) {
            if (toolTextureNames[i].equals("")) names[i] = "";
            else names[i] = toolTextureNames[i] + textureType;
        }
        return names;
    }

    public static final String[] toolMaterialNames = new String[] { "raven_feather", "steeleaf" };

    public static final String[] toolTextureNames = new String[] { "raven_feather", "steeleaf" };

    @Override
    public int getMaterialID(ItemStack stack) {
        if (stack.getItemDamage() >= toolMaterialNames.length) return -1;
        return 5 + stack.getItemDamage();
    }
}
