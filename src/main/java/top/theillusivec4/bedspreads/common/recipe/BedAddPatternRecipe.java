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

package top.theillusivec4.bedspreads.common.recipe;

import javax.annotation.Nonnull;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;

public class BedAddPatternRecipe extends SpecialRecipe {

  public static final SpecialRecipeSerializer<BedAddPatternRecipe> CRAFTING_ADD_PATTERN = new SpecialRecipeSerializer<>(
      BedAddPatternRecipe::new);

  public BedAddPatternRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);

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
  public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack itemstack1 = ItemStack.EMPTY;

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);

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
      ItemStack stack = new ItemStack(DecoratedBedsRegistry.decoratedBedItem);
      CompoundNBT nbttagcompound = stack.getOrCreateChildTag("BlockEntityTag");
      nbttagcompound.put("BannerStack", itemstack.write(new CompoundNBT()));
      nbttagcompound.put("BedStack", itemstack1.write(new CompoundNBT()));
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
