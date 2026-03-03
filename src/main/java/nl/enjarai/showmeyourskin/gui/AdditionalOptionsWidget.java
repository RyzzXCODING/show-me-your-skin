package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Insets;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.Padding;
import io.wispforest.owo.braid.widgets.basic.Panel;
import io.wispforest.owo.braid.widgets.basic.Tooltip;
import io.wispforest.owo.braid.widgets.flex.Row;
import net.minecraft.network.chat.Component;
import nl.enjarai.showmeyourskin.ShowMeYourSkin;
import nl.enjarai.showmeyourskin.config.ArmorConfig;

import java.util.List;

public class AdditionalOptionsWidget extends StatefulWidget {
  private final ArmorConfig armorConfig;

  public AdditionalOptionsWidget(ArmorConfig armorConfig) {
    this.armorConfig = armorConfig;
  }

  @Override
  public WidgetState<?> createState() {
    return new State();
  }

  static class State extends WidgetState<AdditionalOptionsWidget> {
    @Override
    public Widget build(BuildContext context) {
      return new Panel(
        Panel.VANILLA_INSET,
        new Padding(
          Insets.all(4),
          new Row(
            new Padding(Insets.all(1)),
            List.of(
              new Tooltip(
                Component.translatable("gui.showmeyourskin.armorScreen.combatTooltip"),
                new IconCheckbox(
                  ShowMeYourSkin.id("button/show_in_combat"),
                  ShowMeYourSkin.id("button/show_in_combat_disabled"),
                  widget().armorConfig.showInCombat,
                  true,
                  value -> setState(() -> widget().armorConfig.showInCombat = value)
                )
              ),
              new Tooltip(
                Component.translatable("gui.showmeyourskin.armorScreen.nameTagTooltip"),
                new IconCheckbox(
                  ShowMeYourSkin.id("button/show_nametag"),
                  ShowMeYourSkin.id("button/show_nametag_disabled"),
                  widget().armorConfig.showNameTag,
                  true,
                  value -> setState(() -> widget().armorConfig.showNameTag = value)
                )
              ),
              new Tooltip(
                Component.translatable("gui.showmeyourskin.armorScreen.forceElytraWhenFlyingTooltip"),
                new IconCheckbox(
                  ShowMeYourSkin.id("button/force_elytra_when_flying"),
                  ShowMeYourSkin.id("button/force_elytra_when_flying_disabled"),
                  widget().armorConfig.forceElytraWhenFlying,
                  true,
                  value -> setState(() -> widget().armorConfig.forceElytraWhenFlying = value)
                )
              )
            )
          )
        )
      );
    }
  }
}
