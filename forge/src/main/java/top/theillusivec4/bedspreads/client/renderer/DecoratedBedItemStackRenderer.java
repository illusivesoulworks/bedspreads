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

package top.theillusivec4.bedspreads.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.bedspreads.common.DecoratedBedTileEntity;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;

public class DecoratedBedItemStackRenderer extends BlockEntityWithoutLevelRenderer {

  public DecoratedBedItemStackRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher,
                                       EntityModelSet pEntityModelSet) {
    super(pBlockEntityRenderDispatcher, pEntityModelSet);
  }

  @Override
  public void renderByItem(@Nonnull ItemStack itemStackIn,
                           @Nonnull ItemTransforms.TransformType transformType,
                           @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn,
                           int combinedLightIn, int combinedOverlayIn) {
    DecoratedBedTileEntity bed = new DecoratedBedTileEntity(BlockPos.ZERO,
        DecoratedBedsRegistry.DECORATED_BED_BLOCK.defaultBlockState());
    bed.loadFromItemStack(itemStackIn);
    Minecraft.getInstance().getBlockEntityRenderDispatcher()
        .renderItem(bed, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
  }
}
