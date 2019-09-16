/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of Cosmetic Beds, a mod made for Minecraft.
 *
 * Cosmetic Beds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cosmetic Beds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cosmetic Beds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cosmeticbeds.common;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BedBlock;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.cosmeticbeds.client.renderer.BedPatternItemStackRenderer;

public class CosmeticBedItem extends BedItem {

  public CosmeticBedItem() {
    super(CosmeticBedsRegistry.COSMETIC_BED_BLOCK,
        new Item.Properties().maxStackSize(1).setTEISR(() -> BedPatternItemStackRenderer::new));
    this.setRegistryName(CosmeticBedsRegistry.COSMETIC_BED_BLOCK.getRegistryName());
  }

  public static ItemStack getBedStack(ItemStack stack) {

    if (stack.getItem() instanceof CosmeticBedItem) {
      CompoundNBT compound = stack.getChildTag("BlockEntityTag");

      if (compound != null) {
        return ItemStack.read(compound.getCompound("BedStack"));
      }
    }

    return ItemStack.EMPTY;
  }

  public static DyeColor getBedColor(ItemStack stack) {

    if (!stack.isEmpty() && stack.getItem() instanceof BedItem) {
      return ObfuscationReflectionHelper
          .getPrivateValue(BedBlock.class, (BedBlock) ((BedItem) stack.getItem()).getBlock(),
              "field_196352_y");
    }

    return DyeColor.WHITE;
  }

  public static ItemStack getBannerStack(ItemStack stack) {

    if (stack.getItem() instanceof CosmeticBedItem) {
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
      tooltip.add(new TranslationTextComponent(bed.getTranslationKey())
          .applyTextStyle(TextFormatting.GRAY));
    }

    if (!banner.isEmpty()) {
      tooltip.add(new TranslationTextComponent(banner.getTranslationKey())
          .applyTextStyle(TextFormatting.GRAY));
      BannerItem.appendHoverTextFromTileEntityTag(banner, tooltip);
    }
  }
}
