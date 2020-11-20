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

package top.theillusivec4.bedspreads.common;

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
import top.theillusivec4.bedspreads.Bedspreads;

public class DecoratedBedBlock extends BedBlock {

  public DecoratedBedBlock() {
    super(DyeColor.WHITE,
        Block.Properties.create(Material.WOOL).sound(SoundType.WOOD).hardnessAndResistance(0.2F));
  }

  private static Direction getDirectionToOther(BedPart part, Direction facing) {
    return part == BedPart.FOOT ? facing : facing.getOpposite();
  }

  @Override
  public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
    return new DecoratedBedTileEntity();
  }

  @Override
  public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world,
      BlockPos pos, PlayerEntity player) {
    TileEntity te = world.getTileEntity(pos);
    return te instanceof DecoratedBedTileEntity ? ((DecoratedBedTileEntity) te).getItem()
        : super.getPickBlock(state, target, world, pos, player);
  }

  @Override
  public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state,
      @Nonnull PlayerEntity player) {
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

          if (tileentity instanceof DecoratedBedTileEntity) {
            spawnAsEntity(worldIn, pos, ((DecoratedBedTileEntity) tileentity).getItem());
          }
        } else {

          if (tileentityother instanceof DecoratedBedTileEntity) {
            spawnAsEntity(worldIn, blockpos, ((DecoratedBedTileEntity) tileentityother).getItem());
          }
        }
      }
      player.addStat(Stats.BLOCK_MINED.get(this));
    }
    super.onBlockHarvested(worldIn, pos, state, player);
  }

  @Override
  public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state,
      @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
    BlockPos blockpos = pos.offset(state.get(HORIZONTAL_FACING));
    worldIn.setBlockState(blockpos, state.with(PART, BedPart.HEAD), 3);
    worldIn.func_230547_a_(pos, Blocks.AIR);
    state.updateNeighbours(worldIn, pos, 3);
    TileEntity tileentity = worldIn.getTileEntity(pos);

    if (tileentity instanceof DecoratedBedTileEntity) {
      ((DecoratedBedTileEntity) tileentity).loadFromItemStack(stack);
    }
    tileentity = worldIn.getTileEntity(blockpos);

    if (tileentity instanceof DecoratedBedTileEntity) {
      ((DecoratedBedTileEntity) tileentity).loadFromItemStack(stack);
    }
  }
}
