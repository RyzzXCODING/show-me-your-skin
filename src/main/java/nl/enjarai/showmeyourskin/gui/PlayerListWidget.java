package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Alignment;
import io.wispforest.owo.braid.core.Insets;
import io.wispforest.owo.braid.framework.BuildContext;
import io.wispforest.owo.braid.framework.proxy.WidgetState;
import io.wispforest.owo.braid.framework.widget.StatefulWidget;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.Align;
import io.wispforest.owo.braid.widgets.basic.Padding;
import io.wispforest.owo.braid.widgets.basic.Panel;
import io.wispforest.owo.braid.widgets.basic.Sized;
import io.wispforest.owo.braid.widgets.flex.Column;
import io.wispforest.owo.braid.widgets.scroll.ButtonScrollbar;
import io.wispforest.owo.braid.widgets.scroll.ScrollableWithBars;

import java.util.List;
import java.util.function.Consumer;

public class PlayerListWidget extends StatefulWidget {
  private final List<PlayerOption> options;
  private final Consumer<PlayerOption> onSelect;

  public PlayerListWidget(List<PlayerOption> options, Consumer<PlayerOption> onSelect) {
    this.options = options;
    this.onSelect = onSelect;
  }

  @Override
  public WidgetState<?> createState() {
    return new State();
  }

  static class State extends WidgetState<PlayerListWidget> {
    @Override
    public Widget build(BuildContext context) {
      return new Sized(
        100,
        180,
        new Panel(
          Panel.VANILLA_LIGHT,
          new Padding(
            Insets.all(6),
            new Panel(
              Panel.VANILLA_INSET,
              new Padding(
                Insets.all(1),
                new ScrollableWithBars(
                  null,
                  null,
                  null,
                  4,
                  ButtonScrollbar::new,
                  new Padding(
                    Insets.all(3),
                    new Align(
                      Alignment.TOP_LEFT,
                      new Column(
                        new Padding(Insets.all(1)),
                        List.of(
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
      );
    }
  }
}
