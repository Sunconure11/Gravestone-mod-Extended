package nightkosh.gravestone_extended.item.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import nightkosh.gravestone_extended.core.GSBlock;
import nightkosh.gravestone_extended.core.ModInfo;
import nightkosh.gravestone_extended.core.Tabs;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemBoneSword extends ItemSword implements IBoneSword{

    public ItemBoneSword() {
        this(ToolMaterial.STONE);
        this.setUnlocalizedName("gravestone.bone_sword");
        this.setRegistryName(ModInfo.ID, "gs_bone_sword");
    }

    public ItemBoneSword(ToolMaterial material) {
        super(material);
        this.setCreativeTab(Tabs.otherItemsTab);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return super.getIsRepairable(toRepair, repair) || repair.getItem() == Item.getItemFromBlock(GSBlock.BONE_BLOCK);
    }
}
