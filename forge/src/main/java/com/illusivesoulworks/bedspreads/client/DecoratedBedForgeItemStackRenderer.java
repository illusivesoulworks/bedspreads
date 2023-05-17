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

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DecoratedBedForgeItemStackRenderer extends BlockEntityWithoutLevelRenderer {

  public DecoratedBedForgeItemStackRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher,
                                            EntityModelSet entityModelSet) {
    super(blockEntityRenderDispatcher, entityModelSet);
  }

  @Override
  public void renderByItem(@Nonnull ItemStack stack,
                           @Nonnull ItemDisplayContext displayContext,
                           @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer,
                           int light, int overlay) {
    DecoratedBedItemStackRenderer.render(stack, poseStack, buffer, light, overlay);
  }
}
