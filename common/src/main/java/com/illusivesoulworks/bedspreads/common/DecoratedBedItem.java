/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Bedspreads is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Bedspreads.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.bedspreads.common;

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.AbstractBannerBlock;

public class DecoratedBedItem extends BedItem {

  public DecoratedBedItem() {
    super(BedspreadsRegistry.DECORATED_BED_BLOCK.get(), new Item.Properties().stacksTo(1).setId(
        ResourceKey.create(Registries.ITEM,
                           ResourceLocation.fromNamespaceAndPath(BedspreadsConstants.MOD_ID,
                                                                 "decorated_bed"))));
  }

  public static ItemStack getBedStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      BedspreadsData data = stack.get(BedspreadsRegistry.BEDSPREADS_DATA.get());

      if (data != null) {
        return data.bed().copy();
      }
    }
    return ItemStack.EMPTY;
  }

  public static ItemStack getBannerStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      BedspreadsData data = stack.get(BedspreadsRegistry.BEDSPREADS_DATA.get());

      if (data != null) {
        return data.banner().copy();
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

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
                              @Nonnull TooltipDisplay display,
                              @Nonnull Consumer<Component> componentConsumer,
                              @Nonnull TooltipFlag flag) {
    stack.addToTooltip(BedspreadsRegistry.BEDSPREADS_DATA.get(), context, display,
                       componentConsumer, flag);
  }
}
