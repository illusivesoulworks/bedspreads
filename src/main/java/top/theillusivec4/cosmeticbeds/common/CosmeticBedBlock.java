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

package top.theillusivec4.cosmeticbeds.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BedPart;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import top.theillusivec4.cosmeticbeds.CosmeticBeds;

public class CosmeticBedBlock extends BedBlock {

    public CosmeticBedBlock() {
        super(DyeColor.WHITE, Block.Properties.create(Material.WOOL).sound(SoundType.WOOD).hardnessAndResistance(0.2F));
        this.setRegistryName(CosmeticBeds.MODID, "cosmetic_bed");
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityCosmeticBed();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityCosmeticBed ? ((TileEntityCosmeticBed) te).getItem(state) : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, @Nonnull PlayerEntity player) {
        BedPart bedpart = state.get(PART);
        boolean flag = bedpart == BedPart.HEAD;
        BlockPos blockpos = pos.offset(getDirectionToOther(bedpart, state.get(HORIZONTAL_FACING)));
        BlockState iblockstate = worldIn.getBlockState(blockpos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        TileEntity tileentityother = worldIn.getTileEntity(blockpos);

        if (iblockstate.getBlock() == this && iblockstate.get(PART) != bedpart) {
            worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
            worldIn.playEvent(player, 2001, blockpos, Block.getStateId(iblockstate));

            if (!worldIn.isRemote && !player.isCreative()) {

                if (flag) {

                    if (tileentity instanceof TileEntityCosmeticBed) {
                        spawnAsEntity(worldIn, pos, ((TileEntityCosmeticBed) tileentity).getItem(state));
                    }
                } else {

                    if (tileentityother instanceof TileEntityCosmeticBed) {
                        spawnAsEntity(worldIn, blockpos, ((TileEntityCosmeticBed) tileentityother).getItem(iblockstate));
                    }
                }
            }
            player.addStat(Stats.BLOCK_MINED.get(this));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        BlockPos blockpos = pos.offset(state.get(HORIZONTAL_FACING));
        worldIn.setBlockState(blockpos, state.with(PART, BedPart.HEAD), 3);
        worldIn.notifyNeighbors(pos, Blocks.AIR);
        state.updateNeighbors(worldIn, pos, 3);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityCosmeticBed) {
            ((TileEntityCosmeticBed) tileentity).loadFromItemStack(stack);
        }
        tileentity = worldIn.getTileEntity(blockpos);

        if (tileentity instanceof TileEntityCosmeticBed) {
            ((TileEntityCosmeticBed) tileentity).loadFromItemStack(stack);
        }
    }

    private static Direction getDirectionToOther(BedPart part, Direction facing) {
        return part == BedPart.FOOT ? facing : facing.getOpposite();
    }
}
