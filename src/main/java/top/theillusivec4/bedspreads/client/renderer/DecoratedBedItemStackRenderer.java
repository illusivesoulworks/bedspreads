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

package top.theillusivec4.bedspreads.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import top.theillusivec4.bedspreads.common.DecoratedBedTileEntity;

public class DecoratedBedItemStackRenderer extends ItemStackTileEntityRenderer {

  @Override
  public void func_239207_a_(@Nonnull ItemStack itemStackIn,
      @Nonnull ItemCameraTransforms.TransformType transformType, @Nonnull MatrixStack matrixStackIn,
      @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
    DecoratedBedTileEntity bed = new DecoratedBedTileEntity();
    bed.loadFromItemStack(itemStackIn);
    TileEntityRendererDispatcher.instance
        .renderItem(bed, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
  }
}
