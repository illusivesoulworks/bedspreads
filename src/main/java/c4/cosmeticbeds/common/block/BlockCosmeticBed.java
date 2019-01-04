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

package c4.cosmeticbeds.common.block;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.common.tileentity.TileEntityCosmeticBed;
import c4.cosmeticbeds.init.Registry;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockCosmeticBed extends BlockBed {

    public BlockCosmeticBed() {
        super();
        this.setRegistryName(CosmeticBeds.MODID, "cosmetic_bed");
        this.setTranslationKey(CosmeticBeds.MODID + ".cosmetic_bed");
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public MapColor getMapColor(IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return MapColor.CLOTH;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == BlockBed.EnumPartType.FOOT ? Items.AIR : Registry.COSMETIC_BED_ITEM;
    }

    @Override
    public void dropBlockAsItemWithChance(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state, float chance,
                                          int fortune) {
        if (state.getValue(PART) == BlockBed.EnumPartType.HEAD) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            ItemStack stack;
            if (tileentity instanceof TileEntityCosmeticBed) {
                stack = ((TileEntityCosmeticBed) tileentity).getItemStack();
            } else {
                stack = new ItemStack(Registry.COSMETIC_BED_ITEM);
            }
            spawnAsEntity(worldIn, pos, stack);
        }
    }

    @Nonnull
    @Override
    public ItemStack getItem(World worldIn, @Nonnull BlockPos pos, IBlockState state) {
        BlockPos blockpos = pos;

        if (state.getValue(PART) == BlockBed.EnumPartType.FOOT) {
            blockpos = pos.offset(state.getValue(FACING));
        }
        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        ItemStack stack;
        if (tileentity instanceof TileEntityCosmeticBed) {
            stack = ((TileEntityCosmeticBed) tileentity).getItemStack();
        } else {
            stack = new ItemStack(Registry.COSMETIC_BED_ITEM);
        }
        return stack;
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, IBlockState state,
                             TileEntity te, @Nonnull ItemStack stack) {

        if (state.getValue(PART) == BlockBed.EnumPartType.HEAD && te instanceof TileEntityCosmeticBed) {
            TileEntityCosmeticBed tileentitybed = (TileEntityCosmeticBed)te;
            ItemStack itemstack = tileentitybed.getItemStack();
            spawnAsEntity(worldIn, pos, itemstack);
        } else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCosmeticBed();
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {
        return true;
    }
}
