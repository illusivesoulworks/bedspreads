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

package top.theillusivec4.bedspreads.loader.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlockEntity;

public class DecoratedBedItemRenderer implements DynamicItemRenderer {

  @Override
  public void render(ItemStack stack, Mode mode, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    DecoratedBedBlockEntity bed = new DecoratedBedBlockEntity();
    bed.loadFromItemStack(stack);
    BlockEntityRenderDispatcher.INSTANCE
        .renderEntity(bed, matrices, vertexConsumers, light, overlay);
  }
}
