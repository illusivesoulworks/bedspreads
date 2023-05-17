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
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public class RemovePatternRecipe extends CustomRecipe {

  public static final RecipeSerializer<RemovePatternRecipe> CRAFTING_REMOVE_PATTERN =
      new SimpleCraftingRecipeSerializer<>(RemovePatternRecipe::new);

  public RemovePatternRecipe(ResourceLocation id, CraftingBookCategory category) {
    super(id, category);
  }

  @Override
  public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.getContainerSize(); i++) {
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
  public ItemStack assemble(@Nonnull CraftingContainer inv,
                            @Nonnull RegistryAccess registryAccess) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.getContainerSize(); ++i) {
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
  public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
    NonNullList<ItemStack> nonnulllist = NonNullList
        .withSize(inv.getContainerSize(), ItemStack.EMPTY);

    for (int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack item = inv.getItem(i);

      if (!item.isEmpty() && item.getItem() instanceof DecoratedBedItem) {
        nonnulllist.set(i, DecoratedBedItem.getBedStack(item));
      }
    }
    return nonnulllist;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Nonnull
  @Override
  public RecipeSerializer<?> getSerializer() {
    return CRAFTING_REMOVE_PATTERN;
  }
}
