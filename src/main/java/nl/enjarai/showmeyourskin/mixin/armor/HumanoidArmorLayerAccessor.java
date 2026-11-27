package nl.enjarai.showmeyourskin.mixin.armor;

import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HumanoidArmorLayer.class)
public interface HumanoidArmorLayerAccessor {
  @Accessor("modelSet")
  ArmorModelSet<?> showmeyourskin$getModelSet();

  @Accessor("babyModelSet")
  ArmorModelSet<?> showmeyourskin$getBabyModelSet();
}
