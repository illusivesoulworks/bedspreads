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
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DecoratedBedBlockEntity extends BlockEntity {

  private ItemStack bed = ItemStack.EMPTY;
  private ItemStack banner = ItemStack.EMPTY;
  private DyeColor bannerColor = DyeColor.WHITE;
  private BannerPatternLayers patterns;

  public DecoratedBedBlockEntity(BlockPos pos, BlockState state) {
    super(BedspreadsRegistry.DECORATED_BED_BLOCK_ENTITY.get(), pos, state);
  }

  public void loadFromItemStack(ItemStack stack) {
    this.patterns = null;
    BedspreadsData data = stack.get(BedspreadsRegistry.BEDSPREADS_DATA.get());

    if (data != null) {
      this.bed = data.bed().copy();
      this.banner = data.banner().copy();

      if (!this.banner.isEmpty()) {
        this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
      }
    }
    this.patterns = banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider) {

    if (!this.bed.isEmpty()) {
      tag.put("BedStack", this.bed.save(provider, new CompoundTag()));
    }

    if (!this.banner.isEmpty()) {
      tag.put("BannerStack", this.banner.save(provider, new CompoundTag()));
    }
  }

  @Override
  public void loadAdditional(@Nonnull CompoundTag compound,
                             @Nonnull HolderLookup.Provider provider) {
    this.bed =
        ItemStack.parse(provider, compound.getCompoundOrEmpty("BedStack")).orElse(ItemStack.EMPTY);
    this.banner = ItemStack.parse(provider, compound.getCompoundOrEmpty("BannerStack"))
        .orElse(ItemStack.EMPTY);

    if (!this.banner.isEmpty()) {
      this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
    }
    this.patterns = banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Nonnull
  @Override
  public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider) {
    CompoundTag tag = new CompoundTag();
    this.saveAdditional(tag, provider);
    return tag;
  }

  public BannerPatternLayers getPatternList() {
    return this.patterns;
  }

  public ItemStack getItem() {
    ItemStack itemstack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM.get());
    itemstack.set(BedspreadsRegistry.BEDSPREADS_DATA.get(),
                  new BedspreadsData(this.bed.copy(), this.banner.copy()));
    return itemstack;
  }

  public ItemStack getBanner() {
    return this.banner;
  }

  public DyeColor getBannerColor() {
    return this.bannerColor;
  }
}
