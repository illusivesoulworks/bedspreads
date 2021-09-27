/*
 * Copyright (C) 2018-2021 C4
 *
 * This file is part of Bedspreads.
 *
 * Bedspreads is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Bedspreads.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.bedspreads.common;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraftforge.client.IItemRenderProperties;
import top.theillusivec4.bedspreads.client.renderer.DecoratedBedItemStackRenderer;

public class DecoratedBedItem extends BedItem {

  public DecoratedBedItem() {
    super(DecoratedBedsRegistry.DECORATED_BED_BLOCK, new Item.Properties().stacksTo(1));
  }

  public static ItemStack getBedStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundTag compound = stack.getTagElement("BlockEntityTag");

      if (compound != null) {
        return ItemStack.of(compound.getCompound("BedStack"));
      }
    }

    return ItemStack.EMPTY;
  }

  public static ItemStack getBannerStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundTag compound = stack.getTagElement("BlockEntityTag");

      if (compound != null) {
        return ItemStack.of(compound.getCompound("BannerStack"));
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
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn,
                              @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
    ItemStack bed = getBedStack(stack);
    ItemStack banner = getBannerStack(stack);

    if (!bed.isEmpty()) {
      tooltip.add(
          new TranslatableComponent(bed.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    if (!banner.isEmpty()) {
      tooltip.add(
          new TranslatableComponent(banner.getDescriptionId()).withStyle(ChatFormatting.GRAY));
      BannerItem.appendHoverTextFromBannerBlockEntityTag(banner, tooltip);
    }
  }

  @Override
  public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
    consumer.accept(new IItemRenderProperties() {
      @Override
      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        Minecraft mc = Minecraft.getInstance();
        return new DecoratedBedItemStackRenderer(mc.getBlockEntityRenderDispatcher(),
            mc.getEntityModels());
      }
    });
  }
}
