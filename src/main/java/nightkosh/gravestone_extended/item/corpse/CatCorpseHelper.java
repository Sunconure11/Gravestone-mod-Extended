package nightkosh.gravestone_extended.item.corpse;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nightkosh.gravestone_extended.ModGravestoneExtended;
import nightkosh.gravestone_extended.block.enums.EnumCorpse;
import nightkosh.gravestone_extended.core.GSBlock;
import nightkosh.gravestone_extended.entity.monster.pet.EnumUndeadMobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class CatCorpseHelper extends CorpseHelper {

    private CatCorpseHelper() {
    }

    private static final int CORPSE_TYPE = EnumCorpse.CAT.ordinal();

    public static enum EnumCatType {
        OCELOT,
        BLACK,
        RED,
        SIAMESE
    }

    public static ItemStack getRandomCorpse(Random random) {
        return getDefaultCatCorpse(EnumCatType.values()[random.nextInt(EnumCatType.values().length)], EnumUndeadMobType.OTHER);
    }

    public static List<ItemStack> getDefaultCorpses() {
        List<ItemStack> list = new ArrayList<>();

        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.OTHER));
        list.add(getDefaultCatCorpse(EnumCatType.BLACK, EnumUndeadMobType.OTHER));
        list.add(getDefaultCatCorpse(EnumCatType.RED, EnumUndeadMobType.OTHER));
        list.add(getDefaultCatCorpse(EnumCatType.SIAMESE, EnumUndeadMobType.OTHER));

        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.ZOMBIE));
        list.add(getDefaultCatCorpse(EnumCatType.BLACK, EnumUndeadMobType.ZOMBIE));
        list.add(getDefaultCatCorpse(EnumCatType.RED, EnumUndeadMobType.ZOMBIE));
        list.add(getDefaultCatCorpse(EnumCatType.SIAMESE, EnumUndeadMobType.ZOMBIE));

        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.HUSK));
        list.add(getDefaultCatCorpse(EnumCatType.BLACK, EnumUndeadMobType.HUSK));
        list.add(getDefaultCatCorpse(EnumCatType.RED, EnumUndeadMobType.HUSK));
        list.add(getDefaultCatCorpse(EnumCatType.SIAMESE, EnumUndeadMobType.HUSK));

        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.SKELETON));
//        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.STRAY)); //TODO
//        list.add(getDefaultCatCorpse(EnumCatType.OCELOT, EnumUndeadMobType.WITHER));
        return list;
    }

    private static ItemStack getDefaultCatCorpse(EnumCatType catType, EnumUndeadMobType mobType) {
        ItemStack corpse = new ItemStack(GSBlock.CORPSE, 1, CORPSE_TYPE);
        NBTTagCompound nbtTag = new NBTTagCompound();

        nbtTag.setByte("MobType", (byte) mobType.ordinal());
        nbtTag.setByte("CatType", (byte) catType.ordinal());

        corpse.setTagCompound(nbtTag);
        return corpse;
    }

    public static void setNbt(EntityOcelot cat, NBTTagCompound nbt) {
        setName(cat, nbt);
        nbt.setByte("MobType", (byte) EnumUndeadMobType.OTHER.ordinal());
        nbt.setByte("CatType", (byte) cat.getTameSkin());
    }

    public static byte getCatType(NBTTagCompound nbtTag) {
        if (nbtTag == null) {
            return 0;
        } else {
            return nbtTag.getByte("CatType");
        }
    }

    public static void spawnCat(World world, int x, int y, int z, NBTTagCompound nbtTag, EntityPlayer player) {
        EntityOcelot cat = new EntityOcelot(world);
        setMobName(cat, nbtTag);

        cat.setTamed(true);
        cat.setTameSkin(getCatType(nbtTag));
        cat.setOwnerId(player.getUniqueID());
        world.setEntityState(cat, (byte) 7);
        spawnMob(cat, world, x, y, z);
    }

    public static void addInfo(List list, NBTTagCompound nbtTag) {
        addNameInfo(list, nbtTag);
        if (hasType(nbtTag)) {
            list.add(getType(nbtTag));
        }
        addMobTypeInfo(list, nbtTag);
    }

    private static boolean hasType(NBTTagCompound nbtTag) {
        return nbtTag.hasKey("CatType");
    }

    private static String getType(NBTTagCompound nbtTag) {
        return ModGravestoneExtended.proxy.getLocalizedString("item.corpse.cat_type") + " " +
                ModGravestoneExtended.proxy.getLocalizedString(getCatType(nbtTag.getByte("CatType")));
    }

    private static String getCatType(int type) {
        switch (type) {
            case 0:
                return "item.corpse.cat_type.ocelot";
            case 1:
                return "item.corpse.cat_type.black";
            case 2:
                return "item.corpse.cat_type.red";
            case 3:
                return "item.corpse.cat_type.siamese";
            default:
                return "item.corpse.cat_type.unknown";
        }
    }
}
