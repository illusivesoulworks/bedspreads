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

package top.theillusivec4.bedspreads.common.recipe;

import javax.annotation.Nonnull;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.common.DecoratedBedItem;

public class BedRemovePatternRecipe extends SpecialRecipe {

  public static final SpecialRecipeSerializer<BedRemovePatternRecipe> CRAFTING_REMOVE_PATTERN = new SpecialRecipeSerializer<>(
      BedRemovePatternRecipe::new);

  public BedRemovePatternRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack stack = inv.getStackInSlot(i);

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
  public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);

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
  public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {

    NonNullList<ItemStack> nonnulllist = NonNullList
        .withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for (int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack item = inv.getStackInSlot(i);

      if (!item.isEmpty() && item.getItem() instanceof DecoratedBedItem) {
        nonnulllist.set(i, DecoratedBedItem.getBedStack(item));
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
