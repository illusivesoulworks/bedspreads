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

package top.theillusivec4.bedspreads.common;

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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;

public class DecoratedBedBlock extends BedBlock {

  public DecoratedBedBlock() {
    super(DyeColor.WHITE,
        Block.Properties.of(Material.WOOL).sound(SoundType.WOOD).strength(0.2F));
  }

  private static Direction getDirectionToOther(BedPart part, Direction facing) {
    return part == BedPart.FOOT ? facing : facing.getOpposite();
  }

  @Override
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new DecoratedBedTileEntity(pos, state);
  }

  @Override
  public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world,
                                BlockPos pos, Player player) {
    BlockEntity te = world.getBlockEntity(pos);
    return te instanceof DecoratedBedTileEntity ? ((DecoratedBedTileEntity) te).getItem()
        : super.getPickBlock(state, target, world, pos, player);
  }

  @Override
  public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state,
                                @Nonnull Player player) {
    BedPart bedpart = state.getValue(PART);
    boolean flag = bedpart == BedPart.HEAD;
    BlockPos blockpos = pos.relative(getDirectionToOther(bedpart, state.getValue(FACING)));
    BlockState iblockstate = worldIn.getBlockState(blockpos);
    BlockEntity tileentity = worldIn.getBlockEntity(pos);
    BlockEntity tileentityother = worldIn.getBlockEntity(blockpos);

    if (iblockstate.getBlock() == this && iblockstate.getValue(PART) != bedpart) {
      worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
      worldIn.levelEvent(player, 2001, blockpos, Block.getId(iblockstate));

      if (!worldIn.isClientSide && !player.isCreative()) {

        if (flag) {

          if (tileentity instanceof DecoratedBedTileEntity) {
            popResource(worldIn, pos, ((DecoratedBedTileEntity) tileentity).getItem());
          }
        } else {

          if (tileentityother instanceof DecoratedBedTileEntity) {
            popResource(worldIn, blockpos, ((DecoratedBedTileEntity) tileentityother).getItem());
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
    BlockEntity tileentity = worldIn.getBlockEntity(pos);

    if (tileentity instanceof DecoratedBedTileEntity) {
      ((DecoratedBedTileEntity) tileentity).loadFromItemStack(stack);
    }
    tileentity = worldIn.getBlockEntity(blockpos);

    if (tileentity instanceof DecoratedBedTileEntity) {
      ((DecoratedBedTileEntity) tileentity).loadFromItemStack(stack);
    }
  }
}
