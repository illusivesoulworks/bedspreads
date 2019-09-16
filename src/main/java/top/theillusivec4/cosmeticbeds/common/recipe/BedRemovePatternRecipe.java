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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import top.theillusivec4.cosmeticbeds.CosmeticBeds;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedItem;

import javax.annotation.Nonnull;

public class BedRemovePatternRecipe extends IRecipeHidden {

    private static final ResourceLocation ID = new ResourceLocation(CosmeticBeds.MODID, "remove_pattern");

    public static final RecipeSerializers.SimpleSerializer<BedRemovePatternRecipe> CRAFTING_REMOVE_PATTERN =
            new RecipeSerializers.SimpleSerializer<>(ID.toString(), BedRemovePatternRecipe::new);

    public BedRemovePatternRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World worldIn) {

        if (!(inv instanceof InventoryCrafting)) {
            return false;
        } else {
            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    if (!itemstack.isEmpty() || !(stack.getItem() instanceof CosmeticBedItem)) {
                        return false;
                    } else {
                        itemstack = stack.copy();
                    }
                }
            }
            return !itemstack.isEmpty();
        }
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {

                if (stack.getItem() instanceof CosmeticBedItem) {
                    itemstack = stack.copy();
                    break;
                }
            }
        }

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return CosmeticBedItem.getBannerStack(itemstack);
        }
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = inv.getStackInSlot(i);

            if (!item.isEmpty() && item.getItem() instanceof CosmeticBedItem) {
                nonnulllist.set(i, CosmeticBedItem.getBedStack(item));
            }
        }
        return nonnulllist;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CRAFTING_REMOVE_PATTERN;
    }
}
