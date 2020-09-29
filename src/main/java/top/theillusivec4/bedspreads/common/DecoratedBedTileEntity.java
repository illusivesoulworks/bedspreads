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

import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;

public class DecoratedBedTileEntity extends TileEntity {

  private ItemStack bed = ItemStack.EMPTY;
  private ItemStack banner = ItemStack.EMPTY;
  private DyeColor bannerColor = DyeColor.WHITE;
  private ListNBT patterns;
  private boolean patternDataSet;
  private List<Pair<BannerPattern, DyeColor>> patternList;

  public DecoratedBedTileEntity() {
    super(DecoratedBedsRegistry.decoratedBedTe);
  }

  public void loadFromItemStack(ItemStack stack) {
    this.patterns = null;
    CompoundNBT nbttagcompound = stack.getChildTag("BlockEntityTag");

    if (nbttagcompound != null) {
      this.bed = ItemStack.read(nbttagcompound.getCompound("BedStack"));
      this.banner = ItemStack.read(nbttagcompound.getCompound("BannerStack"));

      if (!this.banner.isEmpty()) {
        this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
      }
    }
    CompoundNBT bannernbt = banner.getChildTag("BlockEntityTag");

    if (bannernbt != null && bannernbt.contains("Patterns", 9)) {
      this.patterns = bannernbt.getList("Patterns", 10).copy();
    }
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.write(new CompoundNBT()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.write(new CompoundNBT()));
    }
    return compound;
  }

  @Override
  public void read(BlockState blockState, CompoundNBT compound) {
    super.read(blockState, compound);
    this.bed = compound.contains("BedStack") ? ItemStack.read(compound.getCompound("BedStack"))
        : ItemStack.EMPTY;
    this.banner =
        compound.contains("BannerStack") ? ItemStack.read(compound.getCompound("BannerStack"))
            : ItemStack.EMPTY;

    if (!this.banner.isEmpty()) {
      this.bannerColor = DecoratedBedItem.getBannerColor(this.banner);
    }
    CompoundNBT bannernbt = banner.getChildTag("BlockEntityTag");
    this.patterns = bannernbt != null ? bannernbt.getList("Patterns", 10).copy() : null;
    this.patternList = null;
    this.patternDataSet = true;
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 11, this.getUpdateTag());
  }

  @Nonnull
  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    this.read(this.getBlockState(), pkt.getNbtCompound());
  }

  public List<Pair<BannerPattern, DyeColor>> getPatternList() {
    if (this.patternList == null && this.patternDataSet) {
      this.patternList = BannerTileEntity.getPatternColorData(this.getBannerColor(), this.patterns);
    }
    return this.patternList;
  }

  public ItemStack getItem() {
    ItemStack itemstack = new ItemStack(DecoratedBedsRegistry.decoratedBedItem);
    CompoundNBT compound = itemstack.getOrCreateChildTag("BlockEntityTag");

    if (!this.bed.isEmpty()) {
      compound.put("BedStack", this.bed.write(new CompoundNBT()));
    }

    if (!this.banner.isEmpty()) {
      compound.put("BannerStack", this.banner.write(new CompoundNBT()));
    }
    return itemstack;
  }

  public DyeColor getBannerColor() {
    return this.bannerColor;
  }
}
