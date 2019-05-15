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

import net.minecraft.block.BlockAbstractBanner;
import net.minecraft.block.BlockBed;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.cosmeticbeds.client.renderer.BedPatternItemStackRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCosmeticBed extends ItemBed {

    public ItemCosmeticBed() {
        super(CosmeticBedsRegistry.COSMETIC_BED_BLOCK, new Item.Properties().maxStackSize(1).setTEISR(() -> BedPatternItemStackRenderer::new));
        this.setRegistryName(CosmeticBedsRegistry.COSMETIC_BED_BLOCK.getRegistryName());
    }

    public static ItemStack getBedStack(ItemStack stack) {

        if (stack.getItem() instanceof ItemCosmeticBed) {
            NBTTagCompound compound = stack.getChildTag("BlockEntityTag");

            if (compound != null) {
                return ItemStack.read(compound.getCompound("BedStack"));
            }
        }
        return ItemStack.EMPTY;
    }

    public static EnumDyeColor getBedColor(ItemStack stack) {

        if (!stack.isEmpty() && stack.getItem() instanceof ItemBed) {
            return ObfuscationReflectionHelper.getPrivateValue(BlockBed.class, (BlockBed)((ItemBed)stack.getItem()).getBlock(), "field_196352_y");
        }
        return EnumDyeColor.WHITE;
    }

    public static ItemStack getBannerStack(ItemStack stack) {

        if (stack.getItem() instanceof ItemCosmeticBed) {
            NBTTagCompound compound = stack.getChildTag("BlockEntityTag");

            if (compound != null) {
                return ItemStack.read(compound.getCompound("BannerStack"));
            }
        }
        return ItemStack.EMPTY;
    }

    public static EnumDyeColor getBannerColor(ItemStack stack) {

        if (!stack.isEmpty() && stack.getItem() instanceof ItemBanner) {
            return ((BlockAbstractBanner)((ItemBanner)stack.getItem()).getBlock()).getColor();
        }
        return EnumDyeColor.WHITE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        ItemStack bed = getBedStack(stack);
        ItemStack banner = getBannerStack(stack);

        if (!bed.isEmpty()) {
            tooltip.add(new TextComponentTranslation(bed.getTranslationKey()).applyTextStyle(TextFormatting.GRAY));
        }

        if (!banner.isEmpty()) {
            tooltip.add(new TextComponentTranslation(banner.getTranslationKey()).applyTextStyle(TextFormatting.GRAY));
            ItemBanner.appendHoverTextFromTileEntityTag(banner, tooltip);
        }
    }
}
