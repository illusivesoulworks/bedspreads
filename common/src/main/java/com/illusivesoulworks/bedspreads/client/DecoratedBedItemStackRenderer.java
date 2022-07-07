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

package com.illusivesoulworks.bedspreads.client;

import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;
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

public class DecoratedBedItemStackRenderer {

  public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferIn,
                            int combinedLightIn, int combinedOverlayIn) {
    DecoratedBedBlockEntity bed = new DecoratedBedBlockEntity(BlockPos.ZERO,
        BedspreadsRegistry.DECORATED_BED_BLOCK.get().defaultBlockState());
    bed.loadFromItemStack(stack);
    Minecraft.getInstance().getBlockEntityRenderDispatcher()
        .renderItem(bed, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
  }
}
