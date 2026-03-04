package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Alignment;
import io.wispforest.owo.braid.core.BraidScreen;
import io.wispforest.owo.braid.framework.widget.Widget;
import io.wispforest.owo.braid.widgets.basic.Align;
import net.minecraft.client.gui.screens.Screen;

public class ArmorConfigScreen extends BraidScreen {
  private final Screen parent;

  public ArmorConfigScreen(Widget widget, Screen parent) {
    super(new Align(
      Alignment.CENTER,
      widget
    ));
    this.parent = parent;
  }

  @Override
  public void onClose() {
    this.minecraft.setScreen(this.parent);
  }
}
