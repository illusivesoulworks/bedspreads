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
import net.minecraft.item.BannerItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;

public class BedAddPatternRecipe extends SpecialCraftingRecipe {

  public static final SpecialRecipeSerializer<BedAddPatternRecipe> CRAFTING_ADD_PATTERN =
      new SpecialRecipeSerializer<>(
          BedAddPatternRecipe::new);

  public BedAddPatternRecipe(Identifier id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getStack(i);

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

  @Override
  public ItemStack craft(CraftingInventory inv) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.size(); ++i) {
      ItemStack stack = inv.getStack(i);

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
      ItemStack stack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM);
      NbtCompound nbttagcompound = stack.getOrCreateSubTag("BlockEntityTag");
      nbttagcompound.put("BannerStack", itemstack.writeNbt(new NbtCompound()));
      nbttagcompound.put("BedStack", itemstack1.writeNbt(new NbtCompound()));
      return stack;
    }
  }

  @Override
  public boolean fits(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CRAFTING_ADD_PATTERN;
  }
}
