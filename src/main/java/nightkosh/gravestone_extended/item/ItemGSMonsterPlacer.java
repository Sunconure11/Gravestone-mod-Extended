package nightkosh.gravestone_extended.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import nightkosh.gravestone_extended.core.Entity;
import nightkosh.gravestone_extended.core.ModInfo;
import nightkosh.gravestone_extended.core.Tabs;
import org.apache.commons.lang3.StringUtils;

/**
 * GraveStone mod
 *
 * @author Portablejim
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemGSMonsterPlacer extends ItemMonsterPlacer {

    public static final int WITHER_BACKGROUND_COLOR = 0x000000;
    public static final int ZOMBIE_BACKGROUND_COLOR = 0x00AFAF;
    public static final int SKELETON_BACKGROUND_COLOR = 0xC1C1C1;
    public static final int SKELETON_FOREGROUND_COLOR = 4802889;
    public static final int CAT_BACKGROUND_COLOR = 0xEFDE7D;
    public static final int DOG_BACKGROUND_COLOR = 0xD7D3D3;
    public static final int HORSE_FOREGROUND_COLOR = 15656192;
    public static final int RAVEN_BACKGROUND_COLOR = 14144467;
    public static final int RAVEN_FOREGROUND_COLOR = 11013646;
    public static final int CRAWLER_FOREGROUND_COLOR = 0xA80E0E;
    public static final int SKELETON_PET_FOREGROUND_COLOR = 0x494949;
    public static final int ZOMBIE_PET_FOREGROUND_COLOR = 0x799c65;

    public static enum EnumEggs {
        ZOMBIE_DOG(Entity.ZOMBIE_DOG_NAME, Entity.ZOMBIE_DOG_ID, DOG_BACKGROUND_COLOR, ZOMBIE_PET_FOREGROUND_COLOR),
        ZOMBIE_CAT(Entity.ZOMBIE_CAT_NAME, Entity.ZOMBIE_CAT_ID, CAT_BACKGROUND_COLOR, ZOMBIE_PET_FOREGROUND_COLOR),
        SKELETON_DOG(Entity.SKELETON_DOG_NAME, Entity.SKELETON_DOG_ID, DOG_BACKGROUND_COLOR, SKELETON_PET_FOREGROUND_COLOR),
        SKELETON_CAT(Entity.SKELETON_CAT_NAME, Entity.SKELETON_CAT_ID, CAT_BACKGROUND_COLOR, SKELETON_PET_FOREGROUND_COLOR),
        SKULL_CRAWLER(Entity.SKULL_CRAWLER_NAME, Entity.SKULL_CRAWLER_ID, SKELETON_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        WITHER_SKULL_CRAWLER(Entity.WITHER_SKULL_CRAWLER_NAME, Entity.WITHER_SKULL_CRAWLER_ID, WITHER_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        ZOMBIE_SKULL_CRAWLER(Entity.ZOMBIE_SKULL_CRAWLER_NAME, Entity.ZOMBIE_SKULL_CRAWLER_ID, ZOMBIE_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        SKELETON(Entity.SKELETON_NAME, Entity.SKELETON_ID, SKELETON_BACKGROUND_COLOR, SKELETON_FOREGROUND_COLOR, Entity.MINECRAFT_SKELETON_NAME),
        ZOMBIE_HORSE(Entity.ZOMBIE_HORSE_NAME, Entity.ZOMBIE_HORSE_ID, ZOMBIE_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR, Entity.MINECRAFT_ZOMBIE_HORSE_NAME),
        SKELETON_HORSE(Entity.SKELETON_HORSE_NAME, Entity.SKELETON_HORSE_ID, SKELETON_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR, Entity.MINECRAFT_SKELETON_HORSE_NAME),
        ZOMBIE_RAIDER(Entity.ZOMBIE_RAIDER_NAME, Entity.ZOMBIE_RAIDER_ID, ZOMBIE_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR),
        SKELETON_RAIDER(Entity.SKELETON_RAIDER_NAME, Entity.SKELETON_RAIDER_ID, SKELETON_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR);//,
//        DAMNED_WARRIOR(Entity.DAMNED_WARRIOR_NAME, SKELETON_BACKGROUND_COLOR, SKELETON_FOREGROUND_COLOR),
//        RAVEN(nightkosh.gravestone_extended.core.Entity.RAVEN_NAME, RAVEN_BACKGROUND_COLOR, RAVEN_FOREGROUND_COLOR);

        private String name;
        private String customName;
        private ResourceLocation entityId;
        private int backgroundColor;
        private int foregroundColor;

        private EnumEggs(String name, ResourceLocation entityId, int backgroundColor, int foregroundColor) {
            this.name = name;
            this.entityId = entityId;
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
        }

        private EnumEggs(String name, ResourceLocation entityId, int backgroundColor, int foregroundColor, String customName) {
            this(name, entityId, backgroundColor, foregroundColor);
            this.customName = customName;
        }

        public static EnumEggs getById(int id) {
            if (id > EnumEggs.values().length || id < 0) {
                return ZOMBIE_DOG;
            }
            return EnumEggs.values()[id];
        }

        public String getName() {
            return name;
        }

        public String getCustomName() {
            return customName;
        }

        public ResourceLocation getEntityId() {
            return entityId;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public int getForegroundColor() {
            return foregroundColor;
        }
    }

    public ItemGSMonsterPlacer() {
        this.setHasSubtypes(true);
        this.setCreativeTab(Tabs.otherItemsTab);
        this.setUnlocalizedName("monsterPlacer");
        this.setRegistryName(ModInfo.ID, "gsspawnegg");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        StringBuilder str = new StringBuilder();
        str.append(I18n.translateToLocal(this.getUnlocalizedName() + ".name").trim());

        EnumEggs egg = EnumEggs.getById(itemStack.getItemDamage());
        String name = (StringUtils.isBlank(egg.getCustomName())) ? egg.getName() : egg.getCustomName();
        if (StringUtils.isNotBlank(name)) {
            str.append(" ").append(I18n.translateToLocal("entity." + name + ".name"));
        }

        return str.toString();
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack item = player.getHeldItem(hand);

        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else {
            IBlockState block = world.getBlockState(blockPos);
            double d0 = 0;
            if (facing == EnumFacing.UP && block instanceof BlockFence) {
                d0 = 0.5;
            }

            blockPos = blockPos.offset(facing);
            net.minecraft.entity.Entity entity = spawnCreature(world, item.getItemDamage(), blockPos.getX() + 0.5, blockPos.getY() + d0, blockPos.getZ() + 0.5);
            if (entity != null) {
                if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                    entity.setCustomNameTag(item.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode) {
                    item.setCount(item.getCount() - 1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);
        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, item);
        } else {
            RayTraceResult movingobjectposition = this.rayTrace(world, player, true);

            if (movingobjectposition == null) {
                return new ActionResult(EnumActionResult.PASS, item);
            } else {
                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (!world.canMineBlockBody(player, movingobjectposition.getBlockPos())) {
                        return new ActionResult(EnumActionResult.PASS, item);
                    }

                    if (!player.canPlayerEdit(movingobjectposition.getBlockPos(), movingobjectposition.sideHit, item)) {
                        return new ActionResult(EnumActionResult.PASS, item);
                    }

                    if (world.getBlockState(movingobjectposition.getBlockPos()).getBlock() instanceof BlockLiquid) {
                        net.minecraft.entity.Entity entity = spawnCreature(world, item.getItemDamage(), movingobjectposition.getBlockPos().getX(),
                                movingobjectposition.getBlockPos().getY(), movingobjectposition.getBlockPos().getZ());

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                                entity.setCustomNameTag(item.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode) {
                                item.setCount(item.getCount() - 1);
                            }
                        }
                    }
                }

                return new ActionResult(EnumActionResult.PASS, item);
            }
        }
    }

    public static net.minecraft.entity.Entity spawnCreature(World world, int damageValue, double x, double y, double z) {
        if (world.isRemote || damageValue < 0 || damageValue >= EnumEggs.values().length) {
            return null;
        }

        ResourceLocation entityID = EnumEggs.getById(damageValue).getEntityId();
        net.minecraft.entity.Entity entity = EntityList.createEntityByIDFromName(entityID, world);

        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLiving entityliving = (EntityLiving) entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360), 0);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
            world.spawnEntity(entity);
            entityliving.playLivingSound();
        }

        return entity;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < EnumEggs.values().length; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
