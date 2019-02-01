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

package c4.cosmeticbeds.common.item;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.common.tileentity.TileEntityCosmeticBed;
import c4.cosmeticbeds.init.Registry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCosmeticBed extends ItemBed {

    public ItemCosmeticBed() {
        super();
        this.setRegistryName(CosmeticBeds.MODID, "cosmetic_bed");
        this.setTranslationKey(CosmeticBeds.MODID + ".cosmetic_bed");
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nullable EntityPlayer player, World worldIn, @Nullable BlockPos pos, @Nullable EnumHand
                                      hand, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else if (pos != null && player != null){
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(worldIn, pos);

            if (!flag) {
                pos = pos.up();
            }
            int i = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.byHorizontalIndex(i);
            BlockPos blockpos = pos.offset(enumfacing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(blockpos, facing, itemstack)) {
                IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
                boolean flag1 = iblockstate1.getBlock().isReplaceable(worldIn, blockpos);
                boolean flag2 = flag || worldIn.isAirBlock(pos);
                boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

                if (flag2 && flag3 && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)
                        && worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)) {
                    IBlockState iblockstate2 = Registry.COSMETIC_BED_BLOCK.getDefaultState().withProperty(BlockBed.OCCUPIED, false)
                            .withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                    worldIn.setBlockState(pos, iblockstate2, 10);
                    worldIn.setBlockState(blockpos, iblockstate2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundtype = iblockstate2.getBlock().getSoundType(iblockstate2, worldIn, pos, player);
                    worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    TileEntity tileentity = worldIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityCosmeticBed) {
                        ((TileEntityCosmeticBed)tileentity).setItemValues(itemstack);
                    }
                    TileEntity tileentity1 = worldIn.getTileEntity(pos);

                    if (tileentity1 instanceof TileEntityCosmeticBed) {
                        ((TileEntityCosmeticBed)tileentity1).setItemValues(itemstack);
                    }
                    worldIn.notifyNeighborsRespectDebug(pos, block, false);
                    worldIn.notifyNeighborsRespectDebug(blockpos, iblockstate1.getBlock(), false);

                    if (player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
                    }
                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        return new ItemStack(Items.BED, 1, itemStack.getMetadata());
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    protected boolean isInCreativeTab(CreativeTabs targetTab) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            tooltip.add(I18n.format("item.banner." + EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")) +
                    ".name"));

            for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound1.getInteger("Color"));
                BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound1.getString("Pattern"));

                if (bannerpattern != null) {
                    tooltip.add(I18n.format("item.banner." + bannerpattern.getFileName() + "." + enumdyecolor
                            .getTranslationKey()));
                }
            }
        }
    }

    public static ItemStack makeCosmeticBed(@Nullable NBTTagList patterns, EnumDyeColor bannerBase, int metadata) {
        ItemStack itemstack = new ItemStack(Registry.COSMETIC_BED_ITEM, 1, metadata);

        if (patterns != null && !patterns.isEmpty()) {
            NBTTagCompound compound = itemstack.getOrCreateSubCompound("BlockEntityTag");
            compound.setTag("Patterns", patterns.copy());
            compound.setInteger("Base", bannerBase.getDyeDamage());
        }
        return itemstack;
    }
}
