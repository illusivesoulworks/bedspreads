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

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;

public class DecoratedBedBlockEntity extends BlockEntity {

  private ItemStack bed = ItemStack.EMPTY;
  private ItemStack banner = ItemStack.EMPTY;
  private DyeColor bannerColor = DyeColor.WHITE;
  private ListTag patterns;
  private boolean patternDataSet;
  private List<Pair<BannerPattern, DyeColor>> patternList;

  public DecoratedBedBlockEntity() {
    super(BedspreadsRegistry.DECORATED_BED_BE);
  }

  public void loadFromItemStack(ItemStack stack) {
    this.patterns = null;
    CompoundTag nbttagcompound = stack.getSubTag("BlockEntityTag");

    if (nbttagcompound != null) {
      this.bed = ItemStack.fromTag(nbttagcompound.getCompound("BedStack"));
      this.banner = ItemStack.fromTag(nbttagcompound.getCompound("BannerStack"));

      if (!this.banner.isEmpty()) {
        this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
      }
    }
    CompoundTag bannernbt = banner.getSubTag("BlockEntityTag");

    if (bannernbt != null && bannernbt.contains("Patterns", 9)) {
      this.patterns = bannernbt.getList("Patterns", 10).copy();
    }
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  public CompoundTag toTag(CompoundTag compound) {
    super.toTag(compound);

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.toTag(new CompoundTag()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.toTag(new CompoundTag()));
    }
    return compound;
  }

  @Override
  public void fromTag(BlockState blockState, CompoundTag compound) {
    super.fromTag(blockState, compound);
    this.bed = compound.contains("BedStack") ? ItemStack.fromTag(compound.getCompound("BedStack"))
        : ItemStack.EMPTY;
    this.banner =
        compound.contains("BannerStack") ? ItemStack.fromTag(compound.getCompound("BannerStack"))
            : ItemStack.EMPTY;

    if (!this.banner.isEmpty()) {
      this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
    }
    CompoundTag bannernbt = banner.getSubTag("BlockEntityTag");
    this.patterns = bannernbt != null ? bannernbt.getList("Patterns", 10).copy() : null;
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  public BlockEntityUpdateS2CPacket toUpdatePacket() {
    return new BlockEntityUpdateS2CPacket(this.pos, 11, this.toInitialChunkDataTag());
  }

  @Override
  public CompoundTag toInitialChunkDataTag() {
    return this.toTag(new CompoundTag());
  }

  public List<Pair<BannerPattern, DyeColor>> getPatternList() {
    if (this.patternList == null && this.patternDataSet) {
      this.patternList = BannerBlockEntity.method_24280(this.getBannerColor(), this.patterns);
    }
    return this.patternList;
  }

  public ItemStack getItem() {
    ItemStack itemstack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM);
    CompoundTag compound = itemstack.getOrCreateSubTag("BlockEntityTag");

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.toTag(new CompoundTag()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.toTag(new CompoundTag()));
    }
    return itemstack;
  }

  public DyeColor getBannerColor() {
    return this.bannerColor;
  }
}
