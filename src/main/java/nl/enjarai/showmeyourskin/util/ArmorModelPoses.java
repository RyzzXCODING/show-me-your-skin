package nl.enjarai.showmeyourskin.util;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Per-render-state copy of the player model's animated parts, keyed by the armor model instance that should receive it.
 *
 * <p>Minecraft 1.21.11 submits armor models as separate models. Their {@code setupAnim} runs again when the submitted
 * nodes are rendered, so simply changing an armor model part during submission is not enough: the copied pose has to be
 * reapplied after {@code setupAnim}. Keeping the snapshot on the render state (instead of in a static/thread-local
 * cache) also keeps multiple players and frames isolated from each other.</p>
 */
public final class ArmorModelPoses {

    private final Map<HumanoidModel<?>, ArmorPoseSnapshot> poses = new IdentityHashMap<>();

    public void put(HumanoidModel<?> armorModel, ArmorPoseSnapshot snapshot) {
        poses.put(armorModel, snapshot);
    }

    public void applyTo(HumanoidModel<?> armorModel) {
        var snapshot = poses.get(armorModel);
        if (snapshot != null) {
            snapshot.applyTo(armorModel);
        }
    }

    /** An immutable copy of the seven humanoid parts plus the hat, captured after the player model was animated. */
    public record ArmorPoseSnapshot(PartSnapshot[] parts) {

        public static ArmorPoseSnapshot capture(HumanoidModel<?> source) {
            return new ArmorPoseSnapshot(new PartSnapshot[] {
                    PartSnapshot.of(source.head),
                    PartSnapshot.of(source.hat),
                    PartSnapshot.of(source.body),
                    PartSnapshot.of(source.rightArm),
                    PartSnapshot.of(source.leftArm),
                    PartSnapshot.of(source.rightLeg),
                    PartSnapshot.of(source.leftLeg),
            });
        }

        public void applyTo(HumanoidModel<?> target) {
            var parts = parts();
            PartSnapshot.apply(target.head, parts[0]);
            PartSnapshot.apply(target.hat, parts[1]);
            PartSnapshot.apply(target.body, parts[2]);
            PartSnapshot.apply(target.rightArm, parts[3]);
            PartSnapshot.apply(target.leftArm, parts[4]);
            PartSnapshot.apply(target.rightLeg, parts[5]);
            PartSnapshot.apply(target.leftLeg, parts[6]);
        }
    }

    /** The mutable properties of a single {@link ModelPart} that animation and compatibility mods typically change. */
    public record PartSnapshot(
            boolean visible,
            float x,
            float y,
            float z,
            float xRot,
            float yRot,
            float zRot,
            float xScale,
            float yScale,
            float zScale
    ) {

        public static PartSnapshot of(ModelPart part) {
            return new PartSnapshot(
                    part.visible,
                    part.x,
                    part.y,
                    part.z,
                    part.xRot,
                    part.yRot,
                    part.zRot,
                    part.xScale,
                    part.yScale,
                    part.zScale
            );
        }

        public static void apply(ModelPart part, PartSnapshot snapshot) {
            part.visible = snapshot.visible();
            part.x = snapshot.x();
            part.y = snapshot.y();
            part.z = snapshot.z();
            part.xRot = snapshot.xRot();
            part.yRot = snapshot.yRot();
            part.zRot = snapshot.zRot();
            part.xScale = snapshot.xScale();
            part.yScale = snapshot.yScale();
            part.zScale = snapshot.zScale();
        }
    }
}
