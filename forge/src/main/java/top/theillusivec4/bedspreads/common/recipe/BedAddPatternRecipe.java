/*
 * Copyright (C) 2018-2021 C4
 *
 * This file is part of Bedspreads.
 *
 * Bedspreads is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Bedspreads.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.bedspreads.common.recipe;

import javax.annotation.Nonnull;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;

public class BedAddPatternRecipe extends CustomRecipe {

  public static final SimpleRecipeSerializer<BedAddPatternRecipe> CRAFTING_ADD_PATTERN = new SimpleRecipeSerializer<>(
      BedAddPatternRecipe::new);

  public BedAddPatternRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level worldIn) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.getContainerSize(); ++i) {
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
  public ItemStack assemble(@Nonnull CraftingContainer inv) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.getContainerSize(); ++i) {
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
      ItemStack stack = new ItemStack(DecoratedBedsRegistry.DECORATED_BED_ITEM);
      CompoundTag nbttagcompound = stack.getOrCreateTagElement("BlockEntityTag");
      nbttagcompound.put("BannerStack", itemstack.save(new CompoundTag()));
      nbttagcompound.put("BedStack", itemstack1.save(new CompoundTag()));
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
