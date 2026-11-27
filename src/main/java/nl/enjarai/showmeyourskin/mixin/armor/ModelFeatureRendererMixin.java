package nl.enjarai.showmeyourskin.mixin.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import nl.enjarai.showmeyourskin.ShowMeYourSkinClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Reapplies the captured player pose to armor models after vanilla {@code setupAnim} has run, before any render type
 * (base layer, glint or trim) is drawn. This keeps every submitted layer of an armor piece perfectly in sync.
 */
@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {

    @Inject(
            method = "renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void showmeyourskin$applyArmorPose(SubmitNodeStorage.ModelSubmit<?> modelSubmit, RenderType renderType,
                                               VertexConsumer vertexConsumer, OutlineBufferSource outlineBufferSource,
                                               MultiBufferSource.BufferSource bufferSource, CallbackInfo ci) {
        if (!(modelSubmit.model() instanceof HumanoidModel<?> humanoid)
                || !(modelSubmit.state() instanceof LivingEntityRenderState renderState)) {
            return;
        }

        var modelPoses = renderState.getData(ShowMeYourSkinClient.ARMOR_MODEL_POSES_KEY);
        if (modelPoses != null) {
            modelPoses.applyTo(humanoid);
        }
    }
}
