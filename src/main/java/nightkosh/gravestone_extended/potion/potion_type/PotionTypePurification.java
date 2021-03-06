package nightkosh.gravestone_extended.potion.potion_type;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import nightkosh.gravestone_extended.core.GSPotion;
import nightkosh.gravestone_extended.core.ModInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class PotionTypePurification extends PotionType {

    public PotionTypePurification(PotionEffect... p_i46739_1_) {
        super(p_i46739_1_);
        this.setRegistryName(ModInfo.ID, "gs_purification_type");
    }

    @Override
    public List<PotionEffect> getEffects() {
        List effectList = new ArrayList<>(1);
        effectList.add(new PotionEffect(GSPotion.PURIFICATION));
        return effectList;
    }

    @Override
    public boolean hasInstantEffect() {
        return true;
    }
}
