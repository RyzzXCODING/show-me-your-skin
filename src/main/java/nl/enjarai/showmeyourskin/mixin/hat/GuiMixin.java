package nl.enjarai.showmeyourskin.mixin.hat;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import nl.enjarai.showmeyourskin.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {
  @WrapWithCondition(
    method = "renderCameraOverlays",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/Gui;renderTextureOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/Identifier;F)V",
      ordinal = 0
    )
  )
  private boolean hidePumpkinOverlay(Gui instance, GuiGraphics guiGraphics, Identifier identifier, float f,
                                     @Local Equippable equippable, @Local LocalPlayer localPlayer) {
    if (equippable.slot() != EquipmentSlot.HEAD) {
      return true;
    }

    var config = ModConfig.INSTANCE.getApplicable(localPlayer.getUUID());
    return config.hat.base();
  }
}
