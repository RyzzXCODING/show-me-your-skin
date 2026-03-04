package nl.enjarai.showmeyourskin.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import nl.enjarai.showmeyourskin.mixin.ClientMannequinAccessor;
import nl.enjarai.showmeyourskin.mixin.LivingEntityAccessor;
import nl.enjarai.showmeyourskin.pond.ArmorConfigContraband;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PlayerOption(ArmorConfig armorConfig, UUID uuid, @Nullable AbstractClientPlayer player, Component title) {
  public LivingEntity constructFakeEntity() {
    var player = new RemotePlayer((ClientLevel) this.player.level(), this.player.getGameProfile());
    ((LivingEntityAccessor) player).setEquipment(((LivingEntityAccessor) this.player).getEquipment());
    ((LivingEntityAccessor) player).setElytraAnimationState(((LivingEntityAccessor) this.player).getElytraAnimationState());
    player.tickCount = 20;
    player.setPosRaw(0, 9999, 0);
    return player;
  }

  public Mannequin constructMannequin() {
    Mannequin entity;

    if (player != null) {
      entity = Mannequin.create(EntityType.MANNEQUIN, this.player.level());
      ((LivingEntityAccessor) entity).setEquipment(((LivingEntityAccessor) this.player).getEquipment());
      ((LivingEntityAccessor) entity).setElytraAnimationState(((LivingEntityAccessor) this.player).getElytraAnimationState());
      entity.setComponent(DataComponents.PROFILE, ResolvableProfile.createResolved(this.player.getGameProfile()));
    } else if (Minecraft.getInstance().level != null) {
      entity = Mannequin.create(EntityType.MANNEQUIN, Minecraft.getInstance().level);
      entity.setComponent(DataComponents.PROFILE, ResolvableProfile.createUnresolved(uuid));
      ((ClientMannequinAccessor) entity).invokeUpdateSkin();
    } else {
      return null;
    }

    entity.tickCount = 20;
    var pos = Minecraft.getInstance().player.position();
    entity.absSnapTo(pos.x(), pos.y(), pos.z());
    ((ArmorConfigContraband) entity).show_me_your_skin$setArmorConfig(armorConfig);

    return entity;
  }
}
