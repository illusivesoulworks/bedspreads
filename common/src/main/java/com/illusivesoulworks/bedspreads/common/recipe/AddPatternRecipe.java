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

package com.illusivesoulworks.bedspreads.common.recipe;

import com.illusivesoulworks.bedspreads.common.BedspreadsData;
import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public class AddPatternRecipe extends CustomRecipe {

  public static final RecipeSerializer<AddPatternRecipe> CRAFTING_ADD_PATTERN =
      new SimpleCraftingRecipeSerializer<>(AddPatternRecipe::new);

  public AddPatternRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level level) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getItem(i);

      if (!stack.isEmpty()) {

        if (stack.getItem() instanceof BannerItem) {

          if (!itemstack.isEmpty()) {
            return false;
          }

          itemstack = stack;
        } else if (stack.getItem() instanceof BedItem) {

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

  @Nonnull
  @Override
  public ItemStack assemble(@Nonnull CraftingInput inv,
                            @Nonnull HolderLookup.Provider provider) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getItem(i);

      if (!stack.isEmpty()) {

        if (stack.getItem() instanceof BannerItem) {
          itemstack = stack.copy();
        } else if (stack.getItem() instanceof BedItem) {
          itemstack1 = stack.copy();
        }
      }
    }

    if (itemstack1.isEmpty()) {
      return ItemStack.EMPTY;
    } else {
      ItemStack stack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM.get());
      stack.set(BedspreadsRegistry.BEDSPREADS_DATA.get(),
                new BedspreadsData(itemstack1.copy(), itemstack.copy()));
      return stack;
    }
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Nonnull
  @Override
  public RecipeSerializer<?> getSerializer() {
    return CRAFTING_ADD_PATTERN;
  }
}
