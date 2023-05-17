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

import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import javax.annotation.Nonnull;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public class AddPatternRecipe extends CustomRecipe {

  public static final RecipeSerializer<AddPatternRecipe> CRAFTING_ADD_PATTERN =
      new SimpleCraftingRecipeSerializer<>(AddPatternRecipe::new);

  public AddPatternRecipe(ResourceLocation id, CraftingBookCategory category) {
    super(id, category);
  }

  @Override
  public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
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
  public ItemStack assemble(@Nonnull CraftingContainer inv,
                            @Nonnull RegistryAccess registryAccess) {
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
      ItemStack stack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM.get());
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
