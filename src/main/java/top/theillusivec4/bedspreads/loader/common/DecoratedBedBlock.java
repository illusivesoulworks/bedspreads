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

package top.theillusivec4.bedspreads.loader.common;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DecoratedBedBlock extends BedBlock {

  public DecoratedBedBlock() {
    super(DyeColor.WHITE,
        Block.Settings.of(Material.WOOL).sounds(BlockSoundGroup.WOOD).strength(0.2F));
  }

  private static Direction getDirectionToOther(BedPart part, Direction facing) {
    return part == BedPart.FOOT ? facing : facing.getOpposite();
  }

  @Override
  public BlockEntity createBlockEntity(BlockView worldIn) {
    return new DecoratedBedBlockEntity();
  }

  @Override
  public void onBreak(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
    BedPart bedpart = state.get(PART);
    boolean flag = bedpart == BedPart.HEAD;
    BlockPos blockpos = pos.offset(getDirectionToOther(bedpart, state.get(FACING)));
    BlockState iblockstate = worldIn.getBlockState(blockpos);
    BlockEntity blockentity = worldIn.getBlockEntity(pos);
    BlockEntity blockentityother = worldIn.getBlockEntity(blockpos);

    if (iblockstate.getBlock() == this && iblockstate.get(PART) != bedpart) {
      worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
      worldIn.syncWorldEvent(player, 2001, blockpos, Block.getRawIdFromState(iblockstate));

      if (!worldIn.isClient && !player.isCreative()) {

        if (flag) {

          if (blockentity instanceof DecoratedBedBlockEntity) {
            dropStack(worldIn, pos, ((DecoratedBedBlockEntity) blockentity).getItem());
          }
        } else {

          if (blockentityother instanceof DecoratedBedBlockEntity) {
            dropStack(worldIn, blockpos, ((DecoratedBedBlockEntity) blockentityother).getItem());
          }
        }
      }
    }
    super.onBreak(worldIn, pos, state, player);
  }

  @Override
  public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
      ItemStack stack) {
    BlockPos blockpos = pos.offset(state.get(FACING));
    worldIn.setBlockState(blockpos, state.with(PART, BedPart.HEAD), 3);
    worldIn.updateNeighbors(pos, Blocks.AIR);
    state.method_30101(worldIn, pos, 3);
    BlockEntity blockentity = worldIn.getBlockEntity(pos);

    if (blockentity instanceof DecoratedBedBlockEntity) {
      ((DecoratedBedBlockEntity) blockentity).loadFromItemStack(stack);
    }
    blockentity = worldIn.getBlockEntity(blockpos);

    if (blockentity instanceof DecoratedBedBlockEntity) {
      ((DecoratedBedBlockEntity) blockentity).loadFromItemStack(stack);
    }
  }
}
