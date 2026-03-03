package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Alignment;
import io.wispforest.owo.braid.core.Color;
import io.wispforest.owo.braid.core.Insets;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.widget.StatelessWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.*;
import io.wispforest.owo.braid.widgets.label.Label;
import io.wispforest.owo.braid.widgets.label.LabelStyle;
import io.wispforest.owo.braid.widgets.object.EntityWidget;
import io.wispforest.owo.braid.widgets.stack.Stack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import nl.enjarai.showmeyourskin.ShowMeYourSkin;
import nl.enjarai.showmeyourskin.mixin.LivingEntityAccessor;
import org.joml.Matrix3x2f;

public class PreviewWidget extends StatelessWidget {
  private final Player player;
  private final Component title;

  public PreviewWidget(Player player, Component title) {
    this.player = new RemotePlayer((ClientLevel) player.level(), player.getGameProfile());
    ((LivingEntityAccessor) this.player).setEquipment(((LivingEntityAccessor) player).getEquipment());
    this.player.tickCount = 20;
    this.title = title;
  }

  @Override
  public Widget build(BuildContext context) {
    return new Panel(
      ShowMeYourSkin.id("panel/dark_inset"),
      new Stack(
        player != null ?
          new Transform(
            new Matrix3x2f().translate(0, 8),
            new EntityWidget(1.25, player, w -> {
              w.scaleToFit(true);
              w.displayMode(EntityWidget.DisplayMode.CURSOR);
            })
          ) :
          new Padding(
            Insets.all(4),
            new Sized(
              50, 50,
              new Label(
                new LabelStyle(null, Color.formatting(ChatFormatting.DARK_GRAY), null, false),
                true,
                Component.translatable("gui.showmeyourskin.armorScreen.previewUnavailable")
              )
            )
          ),
        new Padding(
          Insets.all(4),
          new Align(
            Alignment.TOP_LEFT,
            new Label(
              new LabelStyle(null, Color.formatting(ChatFormatting.DARK_GRAY), null, false),
              true,
              title
            )
          )
        )
      )
    );
  }
}
