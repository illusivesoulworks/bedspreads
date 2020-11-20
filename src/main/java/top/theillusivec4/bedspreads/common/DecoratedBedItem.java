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

package top.theillusivec4.bedspreads.common;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.bedspreads.Bedspreads;
import top.theillusivec4.bedspreads.client.renderer.DecoratedBedItemStackRenderer;

public class DecoratedBedItem extends BedItem {

  public DecoratedBedItem() {
    super(DecoratedBedsRegistry.DECORATED_BED_BLOCK,
        new Item.Properties().maxStackSize(1).setISTER(() -> DecoratedBedItemStackRenderer::new));
  }

  public static ItemStack getBedStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundNBT compound = stack.getChildTag("BlockEntityTag");

      if (compound != null) {
        return ItemStack.read(compound.getCompound("BedStack"));
      }
    }

    return ItemStack.EMPTY;
  }

  public static ItemStack getBannerStack(ItemStack stack) {

    if (stack.getItem() instanceof DecoratedBedItem) {
      CompoundNBT compound = stack.getChildTag("BlockEntityTag");

      if (compound != null) {
        return ItemStack.read(compound.getCompound("BannerStack"));
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

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn,
      @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
    ItemStack bed = getBedStack(stack);
    ItemStack banner = getBannerStack(stack);

    if (!bed.isEmpty()) {
      tooltip.add(
          new TranslationTextComponent(bed.getTranslationKey()).mergeStyle(TextFormatting.GRAY));
    }

    if (!banner.isEmpty()) {
      tooltip.add(
          new TranslationTextComponent(banner.getTranslationKey()).mergeStyle(TextFormatting.GRAY));
      BannerItem.appendHoverTextFromTileEntityTag(banner, tooltip);
    }
  }
}
