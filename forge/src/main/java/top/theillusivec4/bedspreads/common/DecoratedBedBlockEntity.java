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

import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DecoratedBedBlockEntity extends BlockEntity {

  private ItemStack bed = ItemStack.EMPTY;
  private ItemStack banner = ItemStack.EMPTY;
  private DyeColor bannerColor = DyeColor.WHITE;
  private ListTag patterns;
  private boolean patternDataSet;
  private List<Pair<BannerPattern, DyeColor>> patternList;

  public DecoratedBedBlockEntity(BlockPos pos, BlockState state) {
    super(DecoratedBedsRegistry.DECORATED_BED_TE, pos, state);
  }

  public void loadFromItemStack(ItemStack stack) {
    this.patterns = null;
    CompoundTag nbttagcompound = stack.getTagElement("BlockEntityTag");

    if (nbttagcompound != null) {
      this.bed = ItemStack.of(nbttagcompound.getCompound("BedStack"));
      this.banner = ItemStack.of(nbttagcompound.getCompound("BannerStack"));

      if (!this.banner.isEmpty()) {
        this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
      }
    }
    CompoundTag bannernbt = banner.getTagElement("BlockEntityTag");

    if (bannernbt != null && bannernbt.contains("Patterns", 9)) {
      this.patterns = bannernbt.getList("Patterns", 10).copy();
    }
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag pTag) {

    if (!this.bed.isEmpty()) {
      pTag.put("BedStack", this.bed.save(new CompoundTag()));
    }

    if (!this.banner.isEmpty()) {
      pTag.put("BannerStack", this.banner.save(new CompoundTag()));
    }
  }

  @Override
  public void load(@Nonnull CompoundTag compound) {
    super.load(compound);
    this.bed = compound.contains("BedStack") ? ItemStack.of(compound.getCompound("BedStack"))
        : ItemStack.EMPTY;
    this.banner =
        compound.contains("BannerStack") ? ItemStack.of(compound.getCompound("BannerStack"))
            : ItemStack.EMPTY;

    if (!this.banner.isEmpty()) {
      this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
    }
    CompoundTag bannernbt = banner.getTagElement("BlockEntityTag");
    this.patterns = bannernbt != null ? bannernbt.getList("Patterns", 10).copy() : null;
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Nonnull
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = new CompoundTag();
    this.saveAdditional(tag);
    return tag;
  }

  public List<Pair<BannerPattern, DyeColor>> getPatternList() {
    if (this.patternList == null && this.patternDataSet) {
      this.patternList = BannerBlockEntity.createPatterns(this.getBannerColor(), this.patterns);
    }
    return this.patternList;
  }

  public ItemStack getItem() {
    ItemStack itemstack = new ItemStack(DecoratedBedsRegistry.DECORATED_BED_ITEM);
    CompoundTag compound = itemstack.getOrCreateTagElement("BlockEntityTag");

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.save(new CompoundTag()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.save(new CompoundTag()));
    }
    return itemstack;
  }

  public DyeColor getBannerColor() {
    return this.bannerColor;
  }
}
