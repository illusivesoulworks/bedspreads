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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;

public class DecoratedBedBlockEntity extends BlockEntity {

  private ItemStack bed = ItemStack.EMPTY;
  private ItemStack banner = ItemStack.EMPTY;
  private DyeColor bannerColor = DyeColor.WHITE;
  private NbtList patterns;
  private boolean patternDataSet;
  private List<Pair<BannerPattern, DyeColor>> patternList;

  public DecoratedBedBlockEntity(BlockPos pos, BlockState state) {
    super(BedspreadsRegistry.DECORATED_BED_BE, pos, state);
  }

  public void loadFromItemStack(ItemStack stack) {
    this.patterns = null;
    NbtCompound nbttagcompound = stack.getSubNbt("BlockEntityTag");

    if (nbttagcompound != null) {
      this.bed = ItemStack.fromNbt(nbttagcompound.getCompound("BedStack"));
      this.banner = ItemStack.fromNbt(nbttagcompound.getCompound("BannerStack"));

      if (!this.banner.isEmpty()) {
        this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
      }
    }
    NbtCompound bannernbt = banner.getSubNbt("BlockEntityTag");

    if (bannernbt != null && bannernbt.contains("Patterns", 9)) {
      this.patterns = bannernbt.getList("Patterns", 10).copy();
    }
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  protected void writeNbt(NbtCompound compound) {
    super.writeNbt(compound);

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.writeNbt(new NbtCompound()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.writeNbt(new NbtCompound()));
    }
  }

  @Override
  public void readNbt(NbtCompound compound) {
    super.readNbt(compound);
    this.bed = compound.contains("BedStack") ? ItemStack.fromNbt(compound.getCompound("BedStack"))
        : ItemStack.EMPTY;
    this.banner =
        compound.contains("BannerStack") ? ItemStack.fromNbt(compound.getCompound("BannerStack"))
            : ItemStack.EMPTY;

    if (!this.banner.isEmpty()) {
      this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
    }
    NbtCompound bannernbt = banner.getSubNbt("BlockEntityTag");
    this.patterns = bannernbt != null ? bannernbt.getList("Patterns", 10).copy() : null;
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  public NbtCompound toInitialChunkDataNbt() {
    return this.createNbt();
  }

  public List<Pair<BannerPattern, DyeColor>> getPatternList() {
    if (this.patternList == null && this.patternDataSet) {
      this.patternList = BannerBlockEntity.getPatternsFromNbt(this.getBannerColor(), this.patterns);
    }
    return this.patternList;
  }

  public ItemStack getItem() {
    ItemStack itemstack = new ItemStack(BedspreadsRegistry.DECORATED_BED_ITEM);
    NbtCompound compound = itemstack.getOrCreateSubNbt("BlockEntityTag");

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.writeNbt(new NbtCompound()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.writeNbt(new NbtCompound()));
    }
    return itemstack;
  }

  public DyeColor getBannerColor() {
    return this.bannerColor;
  }

  @Nullable
  @Override
  public Packet<ClientPlayPacketListener> toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }
}
