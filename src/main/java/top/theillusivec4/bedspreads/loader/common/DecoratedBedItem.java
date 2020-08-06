/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Bedspreads, a mod made for Minecraft.
 *
 * Bedspreads is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Bedspreads.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.bedspreads.loader.common;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;

public class DecoratedBedItem extends BedItem {

  public DecoratedBedItem() {
    super(BedspreadsRegistry.DECORATED_BED_BLOCK, new Item.Settings().maxCount(1));
  }

  public static ItemStack getBedStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundTag compound = stack.getSubTag("BlockEntityTag");

      if (compound != null) {
        return ItemStack.fromTag(compound.getCompound("BedStack"));
      }
    }

    return ItemStack.EMPTY;
  }

  public static ItemStack getBannerStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundTag compound = stack.getSubTag("BlockEntityTag");

      if (compound != null) {
        return ItemStack.fromTag(compound.getCompound("BannerStack"));
      }
    }

    return ItemStack.EMPTY;
  }

  public static DyeColor getBannerColor(ItemStack stack) {

    if (!stack.isEmpty() && stack.getItem() instanceof BannerItem) {
      return ((AbstractBannerBlock) ((BannerItem) stack.getItem()).getBlock()).getColor();
    }

    return DyeColor.WHITE;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, World world, List<Text> tooltip,
      TooltipContext context) {
    ItemStack bed = getBedStack(stack);
    ItemStack banner = getBannerStack(stack);

    if (!bed.isEmpty()) {
      tooltip.add(new TranslatableText(bed.getTranslationKey()).formatted(Formatting.GRAY));
    }

    if (!banner.isEmpty()) {
      tooltip.add(new TranslatableText(banner.getTranslationKey()).formatted(Formatting.GRAY));
      BannerItem.appendBannerTooltip(banner, tooltip);
    }
  }
}
