package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.*;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.*;
import io.wispforest.owo.braid.widgets.flex.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArmorConfigWidget extends StatefulWidget {
  private final ArmorConfig armorConfig;
  private final AbstractClientPlayer player;
  private final boolean synced;
  private final Component title;

  public ArmorConfigWidget(ArmorConfig armorConfig, @Nullable AbstractClientPlayer player, boolean synced, Component title) {
    this.armorConfig = armorConfig;
    this.player = player;
    this.synced = synced;
    this.title = title;
  }

  @Override
  public WidgetState<?> createState() {
    return new State();
  }

  static class State extends WidgetState<ArmorConfigWidget> {
    @Override
    public Widget build(BuildContext buildContext) {
      return new Sized(
        200, 200,
        new Panel(
          Panel.VANILLA_LIGHT,
          new Padding(
            Insets.all(6),
            new Row(
              new Padding(Insets.all(2)),
              List.of(
                new IntrinsicWidth(
                  new Column(
                    MainAxisAlignment.CENTER,
                    CrossAxisAlignment.STRETCH,
                    new Padding(Insets.all(2)),
                    List.of(
                      new Flexible(
                        new Constrain(
                          Constraints.ofMinWidth(78),
                          new ArmorControlsWidget(widget().armorConfig, widget().synced)
                        )
                      ),
                      new AdditionalOptionsWidget(widget().armorConfig)
                    )
                  )
                ),
                new Flexible(
                  new PreviewWidget(widget().player, widget().title)
                )
              )
            )
          )
        )
      );
    }
  }
}
