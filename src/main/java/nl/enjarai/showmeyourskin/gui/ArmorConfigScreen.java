package nl.enjarai.showmeyourskin.gui;

import io.wispforest.owo.braid.core.Alignment;
import io.wispforest.owo.braid.core.BraidScreen;
import io.wispforest.owo.braid.widgets.basic.Align;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import nl.enjarai.showmeyourskin.config.ArmorConfig;

public class ArmorConfigScreen extends BraidScreen {
  private final Screen parent;

  public ArmorConfigScreen(ArmorConfig armorConfig, AbstractClientPlayer player, boolean synced, Component title, Screen parent) {
    super(new Align(
      Alignment.CENTER,
      new ArmorConfigWidget(armorConfig, player, synced, title)
    ));
    this.parent = parent;
  }

  @Override
  public void onClose() {
    this.minecraft.setScreen(this.parent);
  }
}
