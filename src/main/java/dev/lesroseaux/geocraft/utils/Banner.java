package dev.lesroseaux.geocraft.utils;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Banner {
  private final Material material;

  public static final Banner WHITE_BANNER = new Banner(Material.WHITE_BANNER);
  public static final Banner LIGHT_GRAY_BANNER = new Banner(Material.LIGHT_GRAY_BANNER);
  public static final Banner GRAY_BANNER = new Banner(Material.GRAY_BANNER);
  public static final Banner BLACK_BANNER = new Banner(Material.BLACK_BANNER);
  public static final Banner BROWN_BANNER = new Banner(Material.BROWN_BANNER);
  public static final Banner RED_BANNER = new Banner(Material.RED_BANNER);
  public static final Banner ORANGE_BANNER = new Banner(Material.ORANGE_BANNER);
  public static final Banner YELLOW_BANNER = new Banner(Material.YELLOW_BANNER);
  public static final Banner LIME_BANNER = new Banner(Material.LIME_BANNER);
  public static final Banner GREEN_BANNER = new Banner(Material.GREEN_BANNER);
  public static final Banner CYAN_BANNER = new Banner(Material.CYAN_BANNER);
  public static final Banner LIGHT_BLUE_BANNER = new Banner(Material.LIGHT_BLUE_BANNER);
  public static final Banner BLUE_BANNER = new Banner(Material.BLUE_BANNER);
  public static final Banner PURPLE_BANNER = new Banner(Material.PURPLE_BANNER);
  public static final Banner MAGENTA_BANNER = new Banner(Material.MAGENTA_BANNER);
  public static final Banner PINK_BANNER = new Banner(Material.PINK_BANNER);

  private Banner(Material material) {
    this.material = material;
  }

  public static final List<Banner> bannersList = Arrays.asList(
      WHITE_BANNER, LIGHT_GRAY_BANNER, GRAY_BANNER, BLACK_BANNER, BROWN_BANNER, RED_BANNER,
      ORANGE_BANNER, YELLOW_BANNER, LIME_BANNER, GREEN_BANNER, CYAN_BANNER, LIGHT_BLUE_BANNER,
      BLUE_BANNER, PURPLE_BANNER, MAGENTA_BANNER, PINK_BANNER);

  public ItemStack getItem() {
    return new ItemStack(this.material);
  }

  public @Nullable Color getDyeColor() {
    return switch (this.material) {
      case WHITE_BANNER -> Color.WHITE;
      case LIGHT_GRAY_BANNER -> Color.SILVER;
      case GRAY_BANNER -> Color.GRAY;
      case BLACK_BANNER -> Color.BLACK;
      case BROWN_BANNER -> Color.MAROON;
      case RED_BANNER -> Color.RED;
      case ORANGE_BANNER -> Color.ORANGE;
      case YELLOW_BANNER -> Color.YELLOW;
      case LIME_BANNER -> Color.LIME;
      case GREEN_BANNER -> Color.GREEN;
      case CYAN_BANNER -> Color.AQUA;
      case LIGHT_BLUE_BANNER -> Color.fromRGB(173, 216, 230);
      case BLUE_BANNER -> Color.BLUE;
      case PURPLE_BANNER -> Color.PURPLE;
      case MAGENTA_BANNER -> Color.FUCHSIA;
      case PINK_BANNER -> Color.fromRGB(255, 192, 203);
      default -> null;
    };
  }

  public Material getMaterial() {
    return this.material;
  }
}
