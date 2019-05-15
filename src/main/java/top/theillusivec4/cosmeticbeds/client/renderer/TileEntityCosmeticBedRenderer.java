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

package top.theillusivec4.cosmeticbeds.client.renderer;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBed;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.cosmeticbeds.client.BedTextures;
import top.theillusivec4.cosmeticbeds.common.TileEntityCosmeticBed;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;

public class TileEntityCosmeticBedRenderer extends TileEntityRenderer<TileEntityCosmeticBed> {

    private static final ResourceLocation[] TEXTURES = Arrays.stream(EnumDyeColor.values())
            .sorted(Comparator.comparingInt(EnumDyeColor::getId))
            .map((dyecolor) -> new ResourceLocation("textures/entity/bed/" + dyecolor.getTranslationKey() + ".png"))
            .toArray((ResourceLocation[]::new));
    private final ModelBed model = new ModelBed();

    public void render(TileEntityCosmeticBed tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            ResourceLocation resourcelocation = this.getPatternResourceLocation(tileEntityIn);

            if (resourcelocation != null) {
                this.bindTexture(resourcelocation);
            } else {
                resourcelocation = TEXTURES[tileEntityIn.getBedColor().getId()];

                if (resourcelocation != null) {
                    this.bindTexture(resourcelocation);
                }
            }
        }

        if (tileEntityIn.hasWorld()) {
            IBlockState iblockstate = tileEntityIn.getBlockState();
            this.renderPiece(iblockstate.get(BlockBed.PART) == BedPart.HEAD, x, y, z, iblockstate.get(BlockBed.HORIZONTAL_FACING));
        } else {
            this.renderPiece(true, x, y, z, EnumFacing.SOUTH);
            this.renderPiece(false, x, y, z - 1.0D, EnumFacing.SOUTH);
        }

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

    }

    private void renderPiece(boolean isHead, double x, double y, double z, EnumFacing enumfacing) {
        this.model.preparePiece(isHead);
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + 0.5625F, (float)z);
        GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translatef(0.5F, 0.5F, 0.5F);
        GlStateManager.rotatef(180.0F + enumfacing.getHorizontalAngle(), 0.0F, 0.0F, 1.0F);
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        GlStateManager.enableRescaleNormal();
        this.model.render();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    @Nullable
    private ResourceLocation getPatternResourceLocation(TileEntityCosmeticBed bed) {
        return BedTextures.BED_DESIGNS.getResourceLocation(bed.getPatternResourceLocation(), bed.getPatternList(), bed.getColorList());
    }
}