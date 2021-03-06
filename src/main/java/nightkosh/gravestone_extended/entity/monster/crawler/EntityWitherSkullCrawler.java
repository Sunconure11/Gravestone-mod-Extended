package nightkosh.gravestone_extended.entity.monster.crawler;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntityWitherSkullCrawler extends EntitySkullCrawler {

    public EntityWitherSkullCrawler(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2);
    }

    @Override
    protected ItemStack getRareDrop() {
        return new ItemStack(Items.SKULL, 1, 1);
    }

    @Override
    protected PotionEffect getPotionEffect() {
        return new PotionEffect(MobEffects.WITHER, 100);
    }

    @Override
    public boolean canHideInBones() {
        return false;
    }
}
