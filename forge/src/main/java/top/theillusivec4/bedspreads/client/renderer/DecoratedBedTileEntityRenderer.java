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
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import top.theillusivec4.bedspreads.BedspreadsMod;
import top.theillusivec4.bedspreads.common.DecoratedBedTileEntity;

public class DecoratedBedTileEntityRenderer
    implements BlockEntityRenderer<DecoratedBedTileEntity> {

  private final ModelPart headPiece;
  private final ModelPart footPiece;

  public DecoratedBedTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    this.headPiece = ctx.bakeLayer(ModelLayers.BED_HEAD);
    this.footPiece = ctx.bakeLayer(ModelLayers.BED_FOOT);
  }

  public void render(@Nonnull DecoratedBedTileEntity tileEntityIn, float partialTicks,
                     @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn,
                     int combinedLightIn, int combinedOverlayIn) {
    List<Pair<BannerPattern, DyeColor>> list = tileEntityIn.getPatternList();
    Level world = tileEntityIn.getLevel();

    if (world != null) {
      BlockState blockstate = tileEntityIn.getBlockState();
      DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> icallbackwrapper =
          DoubleBlockCombiner
              .combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType,
                  BedBlock::getConnectedDirection,
                  ChestBlock.FACING, blockstate, world, tileEntityIn.getBlockPos(),
                  (p_228846_0_, p_228846_1_) -> false);
      int i = icallbackwrapper.apply(new BrightnessCombiner<>()).get(combinedLightIn);
      this.renderPiece(matrixStackIn, bufferIn,
          blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headPiece : this.footPiece,
          blockstate.getValue(BedBlock.FACING), i, combinedOverlayIn, false, list);
    } else {
      this.renderPiece(matrixStackIn, bufferIn, this.headPiece, Direction.SOUTH, combinedLightIn,
          combinedOverlayIn, false, list);
      this.renderPiece(matrixStackIn, bufferIn, this.footPiece, Direction.SOUTH, combinedLightIn,
          combinedOverlayIn, true, list);
    }
  }

  private void renderPiece(PoseStack matrixStack, MultiBufferSource buffer, ModelPart modelPart,
                           Direction direction, int light, int overlay, boolean p_228847_8_,
                           List<Pair<BannerPattern, DyeColor>> patterns) {
    matrixStack.pushPose();
    matrixStack.translate(0.0D, 0.5625D, p_228847_8_ ? -1.0D : 0.0D);
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
    matrixStack.translate(0.5D, 0.5D, 0.5D);
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F + direction.toYRot()));
    matrixStack.translate(-0.5D, -0.5D, -0.5D);
    Material material = new Material(InventoryMenu.BLOCK_ATLAS,
        new ResourceLocation(BedspreadsMod.MODID, "entity/bed_base"));

    if (patterns != null) {
      renderPatterns(matrixStack, buffer, light, overlay, modelPart, patterns);
    }
    VertexConsumer ivertexbuilder = material.buffer(buffer, RenderType::entityTranslucent);
    modelPart.render(matrixStack, ivertexbuilder, light, overlay);
    matrixStack.popPose();
  }

  public static void renderPatterns(PoseStack matrixStack, MultiBufferSource buffer, int light,
                                    int overlay, ModelPart modelRenderer,
                                    List<Pair<BannerPattern, DyeColor>> patterns) {

    for (int i = 0; i < 17 && i < patterns.size(); ++i) {
      Pair<BannerPattern, DyeColor> pair = patterns.get(i);
      float[] afloat = pair.getSecond().getTextureDiffuseColors();
      Material patternMaterial = new Material(InventoryMenu.BLOCK_ATLAS,
          new ResourceLocation(BedspreadsMod.MODID, "entity/" + pair.getFirst().getFilename()));
      modelRenderer
          .render(matrixStack,
              patternMaterial.buffer(buffer, RenderType::entityTranslucent),
              light, overlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }
  }
}