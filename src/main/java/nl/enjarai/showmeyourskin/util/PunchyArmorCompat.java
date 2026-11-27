package nl.enjarai.showmeyourskin.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import nl.enjarai.showmeyourskin.mixin.LivingEntityRendererAccessor;
import nl.enjarai.showmeyourskin.mixin.armor.HumanoidArmorLayerAccessor;
import nl.enjarai.showmeyourskin.mixin.armor.ModelPartAccessor;
import org.joml.Matrix4f;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Keeps armor models in their vanilla pose while Punchy! builds its first-person sleeve parts.
 *
 * <p>Punchy! derives the armor sleeve transform from the armor model's own part pose. Minecraft 1.21.11 leaves
 * submitted armor models in their animated pose after rendering, so a later first-person pass can otherwise clone an
 * already-animated arm and apply the player's hand transform on top of it. The result is a sleeve that stays behind
 * while the visible hand moves. Resetting those model parts before Punchy! clones them removes the stale pose without
 * changing what was already rendered in third person.</p>
 */
public final class PunchyArmorCompat {

  private static final Class<?> TRANSFORMABLE_PART = findTransformablePart();
  private static final Method SET_EXPLICIT_TRANSFORM = findMethod("punchy$setExplicitTransform", Matrix4f.class);
  private static final Method SET_GRIP_TRANSFORM = findMethod("punchy$setBlendedGripTransform", Matrix4f.class);
  private static final Method CLEAR_DIRECT_POSE_BASE = findMethod("punchy$clearDirectPoseBase");

  private PunchyArmorCompat() {
  }

  public static void resetFirstPersonArmorModels() {
    var minecraft = Minecraft.getInstance();
    if (minecraft.player == null) {
      return;
    }

    var renderer = minecraft.getEntityRenderDispatcher().getRenderer(minecraft.player);
    if (!(renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer)) {
      return;
    }

    var layers = ((LivingEntityRendererAccessor) livingRenderer).showmeyourskin$getLayers();
    for (var layer : layers) {
      if (!(layer instanceof HumanoidArmorLayer<?, ?, ?> armorLayer)) {
        continue;
      }

      var accessor = (HumanoidArmorLayerAccessor) armorLayer;
      resetModelSet(accessor.showmeyourskin$getModelSet());
      resetModelSet(accessor.showmeyourskin$getBabyModelSet());
    }
  }

  private static void resetModelSet(ArmorModelSet<?> modelSet) {
    if (modelSet == null) {
      return;
    }

    resetModel(modelSet.head());
    resetModel(modelSet.chest());
    resetModel(modelSet.legs());
    resetModel(modelSet.feet());
  }

  private static void resetModel(Object model) {
    if (!(model instanceof HumanoidModel<?> humanoidModel)) {
      return;
    }

    var visited = new IdentityHashMap<ModelPart, Boolean>();
    resetPart(humanoidModel.head, visited);
    resetPart(humanoidModel.hat, visited);
    resetPart(humanoidModel.body, visited);
    resetPart(humanoidModel.rightArm, visited);
    resetPart(humanoidModel.leftArm, visited);
    resetPart(humanoidModel.rightLeg, visited);
    resetPart(humanoidModel.leftLeg, visited);
  }

  private static void resetPart(ModelPart part, Map<ModelPart, Boolean> visited) {
    if (part == null || visited.put(part, Boolean.TRUE) != null) {
      return;
    }

    part.resetPose();
    clearPunchyTransforms(part);

    var children = ((ModelPartAccessor) (Object) part).showmeyourskin$getChildren();
    if (children != null) {
      for (var child : children.values()) {
        resetPart(child, visited);
      }
    }
  }

  private static void clearPunchyTransforms(ModelPart part) {
    if (TRANSFORMABLE_PART == null || !TRANSFORMABLE_PART.isInstance(part)) {
      return;
    }

    invoke(SET_EXPLICIT_TRANSFORM, part);
    invoke(SET_GRIP_TRANSFORM, part);
    invoke(CLEAR_DIRECT_POSE_BASE, part);
  }

  private static void invoke(Method method, ModelPart part) {
    if (method == null) {
      return;
    }

    try {
      if (method.getParameterCount() == 0) {
        method.invoke(part);
      } else {
        method.invoke(part, new Object[]{null});
      }
    } catch (ReflectiveOperationException ignored) {
      // A Punchy version with a different compat interface should never break our own rendering.
    }
  }

  private static Class<?> findTransformablePart() {
    try {
      return Class.forName("punchy.client.access.TransformablePart");
    } catch (ClassNotFoundException ignored) {
      return null;
    }
  }

  private static Method findMethod(String name, Class<?>... parameters) {
    if (TRANSFORMABLE_PART == null) {
      return null;
    }

    try {
      return TRANSFORMABLE_PART.getMethod(name, parameters);
    } catch (NoSuchMethodException ignored) {
      return null;
    }
  }
}
