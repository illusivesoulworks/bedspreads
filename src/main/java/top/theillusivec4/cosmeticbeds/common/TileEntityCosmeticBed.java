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

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityCosmeticBed extends TileEntity {

    private ItemStack bed = ItemStack.EMPTY;
    private ItemStack banner = ItemStack.EMPTY;
    private EnumDyeColor bedColor = EnumDyeColor.WHITE;
    private EnumDyeColor bannerColor = EnumDyeColor.WHITE;
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public TileEntityCosmeticBed() {
        super(CosmeticBedsRegistry.COSMETIC_BED_TE);
    }

    public void loadFromItemStack(ItemStack stack) {
        this.patterns = null;
        NBTTagCompound nbttagcompound = stack.getChildTag("BlockEntityTag");

        if (nbttagcompound != null) {
            this.bed = ItemStack.read(nbttagcompound.getCompound("BedStack"));
            this.banner = ItemStack.read(nbttagcompound.getCompound("BannerStack"));

            if (!this.bed.isEmpty()) {
                this.bedColor = ItemCosmeticBed.getBedColor(this.bed);
            }

            if (!this.banner.isEmpty()) {
                this.bannerColor = ItemCosmeticBed.getBannerColor(this.banner);
            }
        }
        NBTTagCompound bannernbt = banner.getChildTag("BlockEntityTag");

        if (bannernbt != null && bannernbt.contains("Patterns", 9)) {
            this.patterns = bannernbt.getList("Patterns", 10).copy();
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
    }

    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);

        if (!this.bed.isEmpty()) {
            compound.put("BedStack", this.bed.write(new NBTTagCompound()));
        }

        if (!this.banner.isEmpty()) {
            compound.put("BannerStack", this.banner.write(new NBTTagCompound()));
        }
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.bed = compound.contains("BedStack") ? ItemStack.read(compound.getCompound("BedStack")) : ItemStack.EMPTY;

        if (!this.bed.isEmpty()) {
            this.bedColor = ItemCosmeticBed.getBedColor(this.bed);
        }
        this.banner = compound.contains("BannerStack") ? ItemStack.read(compound.getCompound("BannerStack")) : ItemStack.EMPTY;

        if (!this.banner.isEmpty()) {
            this.bannerColor = ItemCosmeticBed.getBannerColor(this.banner);
        }
        NBTTagCompound bannernbt = banner.getChildTag("BlockEntityTag");
        this.patterns = bannernbt != null ? bannernbt.getList("Patterns", 10).copy() : null;
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 11, this.getUpdateTag());
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.write(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        this.read(pkt.getNbtCompound());
    }

    public List<BannerPattern> getPatternList() {
        this.initializeBannerData();
        return this.patternList;
    }

    public List<EnumDyeColor> getColorList() {
        this.initializeBannerData();
        return this.colorList;
    }

    public String getPatternResourceLocation() {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    private void initializeBannerData() {

        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {

            if (!this.patternDataSet) {
                this.patternResourceLocation = "";
            } else {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                EnumDyeColor enumdyecolor = this.getBannerColor();

                if (enumdyecolor == null) {
                    this.patternResourceLocation = "banner_missing";
                } else {
                    this.patternList.add(BannerPattern.BASE);
                    this.colorList.add(enumdyecolor);
                    this.patternResourceLocation = "b" + enumdyecolor.getId();

                    if (this.patterns != null) {
                        for(int i = 0; i < this.patterns.size(); ++i) {
                            NBTTagCompound nbttagcompound = this.patterns.getCompound(i);
                            BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound.getString("Pattern"));
                            if (bannerpattern != null) {
                                this.patternList.add(bannerpattern);
                                int j = nbttagcompound.getInt("Color");
                                this.colorList.add(EnumDyeColor.byId(j));
                                this.patternResourceLocation = this.patternResourceLocation + bannerpattern.getHashname() + j;
                            }
                        }
                    }
                }

            }
        }
    }

    public ItemStack getItem(IBlockState state) {
        ItemStack itemstack = new ItemStack(CosmeticBedsRegistry.COSMETIC_BED_ITEM);
        NBTTagCompound compound = itemstack.getOrCreateChildTag("BlockEntityTag");

        if (!this.bed.isEmpty()) {
            compound.put("BedStack", this.bed.write(new NBTTagCompound()));
        }

        if (!this.banner.isEmpty()) {
            compound.put("BannerStack", this.banner.write(new NBTTagCompound()));
        }
        return itemstack;
    }

    public EnumDyeColor getBedColor() {
        return this.bedColor;
    }

    public EnumDyeColor getBannerColor() {
        return this.bannerColor;
    }
}
