package nl.enjarai.showmeyourskin.mixin.compat.punchy;

import nl.enjarai.showmeyourskin.util.PunchyArmorCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Clears stale animated poses from the armor models Punchy! clones for first-person sleeves.
 */
@Mixin(targets = "punchy.client.render.PunchyArmRenderer", remap = false)
public class PunchyArmorArmMixin {
  @Inject(
    method = "original$renderArmArmor",
    at = @At("HEAD"),
    remap = false
  )
  private static void showmeyourskin$prepareArmorSleeveParts(CallbackInfo ci) {
    PunchyArmorCompat.resetFirstPersonArmorModels();
  }
}
