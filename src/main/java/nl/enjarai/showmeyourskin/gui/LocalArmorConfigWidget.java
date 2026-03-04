package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Insets;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.IntrinsicHeight;
import io.wispforest.owo.braid.widgets.basic.Padding;
import io.wispforest.owo.braid.widgets.flex.CrossAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.MainAxisAlignment;
import io.wispforest.owo.braid.widgets.flex.Row;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import nl.enjarai.showmeyourskin.config.ModConfig;

import java.util.List;
import java.util.UUID;

public class LocalArmorConfigWidget extends StatefulWidget {
  private final List<PlayerOption> options;

  public LocalArmorConfigWidget(List<PlayerOption> options) {
    this.options = options;
  }

  @Override
  public WidgetState<?> createState() {
    return new State();
  }

  static class State extends WidgetState<LocalArmorConfigWidget> {
    final PlayerOption globalOption = new PlayerOption(
      ModConfig.INSTANCE.global, Minecraft.getInstance().getGameProfile().id(),
      Minecraft.getInstance().player, Component.translatable("gui.showmeyourskin.armorScreen.global")
    );
    PlayerOption selectedOption = globalOption;

    @Override
    public Widget build(BuildContext context) {
      return new Row(
        MainAxisAlignment.CENTER,
        CrossAxisAlignment.CENTER,
        new Padding(Insets.all(4)),
        List.of(
          new PlayerListWidget(widget().options, option -> setState(() -> selectedOption = option)),
          new ArmorConfigWidget(selectedOption, false)
        )
      );
    }

    @Override
    public void didUpdateWidget(LocalArmorConfigWidget oldWidget) {
      if (!widget().options.contains(selectedOption) && selectedOption != globalOption) {
        setState(() -> {
          selectedOption = globalOption;
        });
      }
    }
  }
}
