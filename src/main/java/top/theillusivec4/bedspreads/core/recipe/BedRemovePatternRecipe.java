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

package top.theillusivec4.bedspreads.core.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedItem;

public class BedRemovePatternRecipe extends SpecialCraftingRecipe {

  public static final SpecialRecipeSerializer<BedRemovePatternRecipe> CRAFTING_REMOVE_PATTERN = new SpecialRecipeSerializer<>(
      BedRemovePatternRecipe::new);

  public BedRemovePatternRecipe(Identifier id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); i++) {
      ItemStack stack = inv.getStack(i);

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

  @Override
  public ItemStack craft(CraftingInventory inv) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getStack(i);

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

  @Override
  public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
    DefaultedList<ItemStack> nonnulllist = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

    for (int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack item = inv.getStack(i);

      if (!item.isEmpty() && item.getItem() instanceof DecoratedBedItem) {
        nonnulllist.set(i, DecoratedBedItem.getBedStack(item));
      }
    }
    return nonnulllist;
  }

  @Override
  public boolean fits(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CRAFTING_REMOVE_PATTERN;
  }
}
