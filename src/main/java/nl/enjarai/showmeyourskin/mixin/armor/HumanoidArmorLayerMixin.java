package nl.enjarai.showmeyourskin.mixin.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import nl.enjarai.showmeyourskin.ShowMeYourSkinClient;
import nl.enjarai.showmeyourskin.util.ArmorModelPoses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Captures the player model's final animated pose right before armor pieces are submitted. Compatibility mods animate
 * model parts directly; vanilla does not carry those changes over to the separate armor models.
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>>
        extends RenderLayer<S, M> {

    protected HumanoidArmorLayerMixin(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("HEAD")
    )
    private void showmeyourskin$captureArmorPose(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords,
                                                 S renderState, float yaw, float pitch, CallbackInfo ci) {
        if (!(renderState instanceof AvatarRenderState) || !(this.getParentModel() instanceof HumanoidModel<?> humanoid)) {
            return;
        }

        renderState.setData(
                ShowMeYourSkinClient.ARMOR_SOURCE_POSE_KEY,
                ArmorModelPoses.ArmorPoseSnapshot.capture(humanoid)
        );
        renderState.setData(ShowMeYourSkinClient.ARMOR_MODEL_POSES_KEY, new ArmorModelPoses());
    }
}
