package efm.dev.efmcore.common.efm_events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ItemBurnEvent extends Event {
    public final ItemEntity entity;
    public final DamageSource source;
    public float amount;

    public ItemBurnEvent(ItemEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }
}
