package nl.enjarai.showmeyourskin.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import nl.enjarai.cicada.api.util.AbstractModConfig;
import nl.enjarai.showmeyourskin.Components;
import nl.enjarai.showmeyourskin.ShowMeYourSkin;
import nl.enjarai.showmeyourskin.ShowMeYourSkinClient;
import nl.enjarai.showmeyourskin.util.CombatLogger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class ModConfig extends AbstractModConfig {
  public static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(ShowMeYourSkin.MODID + ".json");
  public static ModConfig INSTANCE = loadConfigFile(CONFIG_FILE, new ModConfig());

  public boolean globalEnabled = true;
  public boolean overridesEnabledInServerMode = false;
  public float combatCooldown = 16;
  public float fadeOutTime = 2;
  public final ArmorConfig global = new ArmorConfig();
  public final HashMap<UUID, ArmorConfig> overrides = new HashMap<>();


  public ArmorConfig getOverrideOrGlobal(UUID uuid) {
    var client = Minecraft.getInstance();

    boolean serverAvailable = ShowMeYourSkinClient.serverConfig != null && client.level != null;
    boolean useClientValues = overridesEnabledInServerMode || !serverAvailable;

    if (useClientValues) {
      var config = overrides.get(uuid);

      if (config != null) {
        return config;
      }
    }

    if (serverAvailable) {
      var player = client.level.getPlayerByUUID(uuid);

      if (player != null) {
        var config = player.getComponent(Components.ARMOR_CONFIG).getConfig();

        if (!config.equals(ArmorConfig.VANILLA_VALUES)) {
          return config;
        }
      }
    }

    if (useClientValues) {
      return global;
    }

    return ArmorConfig.VANILLA_VALUES;
  }

  public ArmorConfig getApplicable(UUID uuid) {
    if (!globalEnabled) {
      return ArmorConfig.VANILLA_VALUES;
    }

    var applicable = getOverrideOrGlobal(uuid);
    if (applicable.showInCombat && CombatLogger.INSTANCE.isInCombat(uuid)) {
      return ArmorConfig.VANILLA_VALUES;
    }

    applicable = applicable.copy();

    var level = Minecraft.getInstance().level;
    if (applicable.forceElytraWhenFlying && level != null) {
      var player = level.getPlayerByUUID(uuid);
      if (player != null && player.isFallFlying()) {
        applicable.elytra = ArmorConfig.PieceConfig.VANILLA_VALUES;
      }
    }

    return applicable;
  }

  public ArmorConfig getOverride(UUID uuid) {
    return overrides.get(uuid);
  }

  public ArmorConfig getOrCreateOverride(UUID uuid) {
    var armorConfig = getOverride(uuid);

    if (armorConfig == null) {
      armorConfig = new ArmorConfig();
      overrides.put(uuid, armorConfig);
    }

    return armorConfig;
  }

  public void deleteOverride(UUID uuid) {
    overrides.remove(uuid);
  }

  public void ensureValid() {
    if (combatCooldown < 0) {
      combatCooldown = 0;
    }
    if (fadeOutTime < 0) {
      fadeOutTime = 0;
    }
    if (fadeOutTime > combatCooldown) {
      fadeOutTime = combatCooldown;
    }

    global.ensureValid();
    overrides.values().forEach(ArmorConfig::ensureValid);

    save();
  }
}
