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

package c4.cosmeticbeds.common.recipe;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.init.Registry;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class BedPatternRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public BedPatternRecipe() {
        this.setRegistryName(CosmeticBeds.MODID, "bed_pattern_recipe");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack2 = inv.getStackInSlot(i);

            if (!itemstack2.isEmpty()) {

                if (itemstack2.getItem() == Items.BANNER) {

                    if (!itemstack1.isEmpty()) {
                        return false;
                    }
                    itemstack1 = itemstack2;
                } else {

                    if (itemstack2.getItem() != Items.BED || !itemstack.isEmpty() || itemstack2.getSubCompound
                            ("BlockEntityTag") != null) {
                        return false;
                    }
                    itemstack = itemstack2;
                }
            }
        }
        return !itemstack.isEmpty() && !itemstack1.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack2 = inv.getStackInSlot(i);

            if (!itemstack2.isEmpty()) {

                if (itemstack2.getItem() == Items.BANNER) {
                    itemstack = itemstack2;
                } else if (itemstack2.getItem() == Items.BED) {
                    itemstack1 = itemstack2.copy();
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return itemstack1;
        } else {
            ItemStack stack = new ItemStack(Registry.COSMETIC_BED_ITEM, 1, itemstack1.getMetadata());
            NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");
            NBTTagCompound nbttagcompound1 = nbttagcompound == null ? new NBTTagCompound() : nbttagcompound.copy();
            nbttagcompound1.setInteger("Base", itemstack.getMetadata() & 15);
            stack.setTagInfo("BlockEntityTag", nbttagcompound1);
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }
}
