package nightkosh.gravestone_extended.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntitySkeletonHorse extends EntityUndeadHorse {

    public EntitySkeletonHorse(World worldIn) {
        super(worldIn);

        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6, 1, 1.2));
    }

    @Override
    public EnumHorseType getUndeadHorseType() {
        return EnumHorseType.SKELETON_HORSE_TYPE;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
    }
}
