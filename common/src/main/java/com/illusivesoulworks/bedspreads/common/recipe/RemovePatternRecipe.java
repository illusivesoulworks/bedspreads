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

import com.illusivesoulworks.bedspreads.common.DecoratedBedItem;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RemovePatternRecipe extends CustomRecipe {

  public static final RecipeSerializer<RemovePatternRecipe> CRAFTING_REMOVE_PATTERN =
      new CustomRecipe.Serializer<>(RemovePatternRecipe::new);

  public RemovePatternRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level level) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); i++) {
      ItemStack stack = inv.getItem(i);

      if (!stack.isEmpty()) {

        if (!itemstack.isEmpty() || !(stack.getItem() instanceof DecoratedBedItem)) {
          return false;
        } else {
          itemstack = stack.copy();
        }
      }
    }
    return !itemstack.isEmpty();
  }

  @Nonnull
  @Override
  public ItemStack assemble(@Nonnull CraftingInput inv,
                            @Nonnull HolderLookup.Provider provider) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getItem(i);

      if (!stack.isEmpty()) {

        if (stack.getItem() instanceof DecoratedBedItem) {
          itemstack = stack.copy();
          break;
        }
      }
    }

    if (itemstack.isEmpty()) {
      return ItemStack.EMPTY;
    } else {
      return DecoratedBedItem.getBannerStack(itemstack);
    }
  }

  @Nonnull
  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
    NonNullList<ItemStack> nonnulllist = NonNullList
        .withSize(inv.size(), ItemStack.EMPTY);

    for (int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack item = inv.getItem(i);

      if (!item.isEmpty() && item.getItem() instanceof DecoratedBedItem) {
        nonnulllist.set(i, DecoratedBedItem.getBedStack(item));
      }
    }
    return nonnulllist;
  }

  @Nonnull
  @Override
  public RecipeSerializer<? extends CustomRecipe> getSerializer() {
    return CRAFTING_REMOVE_PATTERN;
  }
}
