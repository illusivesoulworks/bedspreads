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

package top.theillusivec4.cosmeticbeds.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import top.theillusivec4.cosmeticbeds.CosmeticBeds;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedsRegistry;

import javax.annotation.Nonnull;

public class BedAddPatternRecipe extends IRecipeHidden {

    private static final ResourceLocation ID = new ResourceLocation(CosmeticBeds.MODID, "add_pattern");

    public static final RecipeSerializers.SimpleSerializer<BedAddPatternRecipe> CRAFTING_ADD_PATTERN =
            new RecipeSerializers.SimpleSerializer<>(ID.toString(), BedAddPatternRecipe::new);

    public BedAddPatternRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World worldIn) {

        if (!(inv instanceof InventoryCrafting)) {
            return false;
        } else {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for(int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    if (stack.getItem() instanceof ItemBanner) {

                        if (!itemstack.isEmpty()) {
                            return false;
                        }
                        itemstack = stack;
                    } else if (stack.getItem() instanceof ItemBed) {

                        if (!itemstack1.isEmpty()) {
                            return false;
                        }
                        itemstack1 = stack;
                    } else {
                        return false;
                    }
                }
            }
            return !itemstack.isEmpty() && !itemstack1.isEmpty();
        }
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {

                if (stack.getItem() instanceof ItemBanner) {
                    itemstack = stack.copy();
                } else if (stack.getItem() instanceof ItemBed) {
                    itemstack1 = stack.copy();
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(CosmeticBedsRegistry.COSMETIC_BED_ITEM);
            NBTTagCompound nbttagcompound = stack.getOrCreateChildTag("BlockEntityTag");
            nbttagcompound.put("BannerStack", itemstack.write(new NBTTagCompound()));
            nbttagcompound.put("BedStack", itemstack1.write(new NBTTagCompound()));
            return stack;
        }
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CRAFTING_ADD_PATTERN;
    }
}
