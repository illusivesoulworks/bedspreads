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

package com.illusivesoulworks.bedspreads.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;

public class DecoratedBedBlock extends BedBlock {

  public DecoratedBedBlock() {
    super(DyeColor.WHITE,
        Block.Properties.of().mapColor(MapColor.WOOL).ignitedByLava().sound(SoundType.WOOD)
            .strength(0.2F));
  }

  private static Direction getDirectionToOther(BedPart part, Direction facing) {
    return part == BedPart.FOOT ? facing : facing.getOpposite();
  }

  @Override
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new DecoratedBedBlockEntity(pos, state);
  }

  @Nonnull
  @Override
  public ItemStack getCloneItemStack(@Nonnull BlockGetter level, @Nonnull BlockPos pos,
                                     @Nonnull BlockState state) {
    BlockEntity be = level.getBlockEntity(pos);
    return be instanceof DecoratedBedBlockEntity ? ((DecoratedBedBlockEntity) be).getItem() :
        super.getCloneItemStack(level, pos, state);
  }

  @Override
  public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state,
                                @Nonnull Player player) {
    BedPart bedpart = state.getValue(PART);
    boolean flag = bedpart == BedPart.HEAD;
    BlockPos blockpos = pos.relative(getDirectionToOther(bedpart, state.getValue(FACING)));
    BlockState iblockstate = worldIn.getBlockState(blockpos);
    BlockEntity blockentity = worldIn.getBlockEntity(pos);
    BlockEntity blockentityother = worldIn.getBlockEntity(blockpos);

    if (iblockstate.getBlock() == this && iblockstate.getValue(PART) != bedpart) {
      worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
      worldIn.levelEvent(player, 2001, blockpos, Block.getId(iblockstate));

      if (!worldIn.isClientSide && !player.isCreative()) {

        if (flag) {

          if (blockentity instanceof DecoratedBedBlockEntity) {
            popResource(worldIn, pos, ((DecoratedBedBlockEntity) blockentity).getItem());
          }
        } else {

          if (blockentityother instanceof DecoratedBedBlockEntity) {
            popResource(worldIn, blockpos, ((DecoratedBedBlockEntity) blockentityother).getItem());
          }
        }
      }
      player.awardStat(Stats.BLOCK_MINED.get(this));
    }
    super.playerWillDestroy(worldIn, pos, state, player);
  }

  @Override
  public void setPlacedBy(Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state,
                          @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
    BlockPos blockpos = pos.relative(state.getValue(FACING));
    worldIn.setBlock(blockpos, state.setValue(PART, BedPart.HEAD), 3);
    worldIn.blockUpdated(pos, Blocks.AIR);
    state.updateNeighbourShapes(worldIn, pos, 3);
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
