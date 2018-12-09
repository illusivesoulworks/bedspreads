/*
 * Copyright (C) 2018  C4
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

package c4.cosmeticbeds.common.tileentity;

import c4.cosmeticbeds.common.item.ItemCosmeticBed;
import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityCosmeticBed extends TileEntityBed {

    private EnumDyeColor baseColor = EnumDyeColor.BLACK;
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public void setItemValues(ItemStack stack) {
        super.setItemValues(stack);
        this.patterns = null;
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            this.patterns = nbttagcompound.getTagList("Patterns", 10).copy();
        }
        this.baseColor = TileEntityBanner.getColor(stack);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.baseColor = EnumDyeColor.byDyeDamage(compound.getInteger("Base"));
        this.patterns = compound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Base", this.baseColor.getDyeDamage());

        if (this.patterns != null) {
            compound.setTag("Patterns", this.patterns);
        }
        return compound;
    }

    @SideOnly(Side.CLIENT)
    public EnumDyeColor getBaseColor() {
        return this.baseColor;
    }

    @SideOnly(Side.CLIENT)
    public List<BannerPattern> getPatternList() {
        this.initializePatternData();
        return this.patternList;
    }

    @SideOnly(Side.CLIENT)
    public List<EnumDyeColor> getColorList() {
        this.initializePatternData();
        return this.colorList;
    }

    @SideOnly(Side.CLIENT)
    public String getPatternResourceLocation() {
        this.initializePatternData();
        return this.patternResourceLocation;
    }

    @SideOnly(Side.CLIENT)
    private void initializePatternData() {

        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {

            if (!this.patternDataSet) {
                this.patternResourceLocation = "";
            } else {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(BannerPattern.BASE);
                this.colorList.add(this.baseColor);
                this.patternResourceLocation = "b" + this.baseColor.getDyeDamage();

                if (this.patterns != null) {
                    StringBuilder builder = new StringBuilder(this.patternResourceLocation);

                    for (int i = 0; i < this.patterns.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                        BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound.getString("Pattern"));

                        if (bannerpattern != null) {
                            this.patternList.add(bannerpattern);
                            int j = nbttagcompound.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(j));
                            builder.append(bannerpattern.getHashname());
                            builder.append(j);
                        }
                    }
                    this.patternResourceLocation = builder.toString();
                }
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return ItemCosmeticBed.makeCosmeticBed(this.patterns, this.getColor().getMetadata());
    }
}
