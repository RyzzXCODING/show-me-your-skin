package nl.enjarai.showmeyourskin.config;

import io.wispforest.endec.Endec;
import io.wispforest.endec.impl.StructEndecBuilder;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorConfig {
  public static final ArmorConfig VANILLA_VALUES = new ArmorConfig();
  public static final Endec<ArmorConfig> ENDEC = StructEndecBuilder.of(
    ArmorPieceConfig.ENDEC.fieldOf("head", c -> c.head),
    ArmorPieceConfig.ENDEC.fieldOf("chest", c -> c.chest),
    ArmorPieceConfig.ENDEC.fieldOf("legs", c -> c.legs),
    ArmorPieceConfig.ENDEC.fieldOf("feet", c -> c.feet),
    OtherPieceConfig.ENDEC.fieldOf("elytra", c -> c.elytra),
    OtherPieceConfig.ENDEC.fieldOf("shield", c -> c.shield),
    OtherPieceConfig.ENDEC.fieldOf("hat", c -> c.hat),
    Endec.BOOLEAN.fieldOf("show_in_combat", c -> c.showInCombat),
    Endec.BOOLEAN.fieldOf("show_name_tag", c -> c.showNameTag),
    Endec.BOOLEAN.fieldOf("force_elytra_when_flying", c -> c.forceElytraWhenFlying),
    ArmorConfig::new
  );

  public ArmorPieceConfig head = new ArmorPieceConfig();
  public ArmorPieceConfig chest = new ArmorPieceConfig();
  public ArmorPieceConfig legs = new ArmorPieceConfig();
  public ArmorPieceConfig feet = new ArmorPieceConfig();
  public OtherPieceConfig elytra = new OtherPieceConfig();
  public OtherPieceConfig shield = new OtherPieceConfig();
  public OtherPieceConfig hat = new OtherPieceConfig();
  public boolean showInCombat = true;
  public boolean showNameTag = true;
  public boolean forceElytraWhenFlying = true;

  public ArmorConfig() {
  }

  public ArmorConfig(ArmorPieceConfig head, ArmorPieceConfig chest, ArmorPieceConfig legs, ArmorPieceConfig feet, OtherPieceConfig elytra, OtherPieceConfig shield, OtherPieceConfig hat, boolean showInCombat, boolean showNameTag, boolean forceElytraWhenFlying) {
    this.head = head;
    this.chest = chest;
    this.legs = legs;
    this.feet = feet;
    this.elytra = elytra;
    this.shield = shield;
    this.hat = hat;
    this.showInCombat = showInCombat;
    this.showNameTag = showNameTag;
    this.forceElytraWhenFlying = forceElytraWhenFlying;
  }

  public ArmorPieceConfig getEquipmentConfig(EquipmentSlot slot) {
    return switch (slot) {
      case HEAD -> head;
      case CHEST -> chest;
      case LEGS -> legs;
      case FEET -> feet;
      default -> throw new IllegalArgumentException("Unexpected value: " + slot);
    };
  }

  public void ensureValid() {
  }

  public static class ArmorPieceConfig {
    public static final Endec<ArmorPieceConfig> ENDEC = StructEndecBuilder.of(
      Endec.BOOLEAN.fieldOf("base", c -> c.base),
      Endec.BOOLEAN.fieldOf("trim", c -> c.trim),
      Endec.BOOLEAN.fieldOf("glint", c -> c.glint),
      ArmorPieceConfig::new
    );
    public static final ArmorPieceConfig VANILLA_VALUES = new ArmorPieceConfig();

    public boolean base = true;
    public boolean trim = true;
    public boolean glint = true;

    public ArmorPieceConfig(boolean base, boolean trim, boolean glint) {
      this.base = base;
      this.trim = trim;
      this.glint = glint;
    }

    public ArmorPieceConfig() {
    }

    public void setAll(boolean value) {
      this.base = value;
      this.trim = value;
      this.glint = value;
    }

    public ArmorPieceConfig copy() {
      return new ArmorPieceConfig(base, trim, glint);
    }

    @Override
    public final boolean equals(Object o) {
      if (!(o instanceof ArmorPieceConfig that)) return false;

      return base == that.base && trim == that.trim && glint == that.glint;
    }

    @Override
    public int hashCode() {
      int result = Boolean.hashCode(base);
      result = 31 * result + Boolean.hashCode(trim);
      result = 31 * result + Boolean.hashCode(glint);
      return result;
    }
  }

  public static class OtherPieceConfig {
    public static final Endec<OtherPieceConfig> ENDEC = StructEndecBuilder.of(
      Endec.BOOLEAN.fieldOf("base", c -> c.base),
      Endec.BOOLEAN.fieldOf("glint", c -> c.glint),
      OtherPieceConfig::new
    );
    public static final OtherPieceConfig VANILLA_VALUES = new OtherPieceConfig();

    public boolean base = true;
    public boolean glint = true;

    public OtherPieceConfig(boolean base, boolean glint) {
      this.base = base;
      this.glint = glint;
    }

    public OtherPieceConfig() {
    }

    public void setAll(boolean value) {
      this.base = value;
      this.glint = value;
    }

    public OtherPieceConfig copy() {
      return new OtherPieceConfig(base, glint);
    }

    @Override
    public final boolean equals(Object o) {
      if (!(o instanceof OtherPieceConfig that)) return false;

      return base == that.base && glint == that.glint;
    }

    @Override
    public int hashCode() {
      int result = Boolean.hashCode(base);
      result = 31 * result + Boolean.hashCode(glint);
      return result;
    }
  }
}
