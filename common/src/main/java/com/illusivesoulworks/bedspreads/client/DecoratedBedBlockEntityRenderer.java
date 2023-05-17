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

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
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

public class DecoratedBedBlockEntityRenderer
    implements BlockEntityRenderer<DecoratedBedBlockEntity> {

  private final ModelPart headPiece;
  private final ModelPart footPiece;

  public DecoratedBedBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    this.headPiece = ctx.bakeLayer(ModelLayers.BED_HEAD);
    this.footPiece = ctx.bakeLayer(ModelLayers.BED_FOOT);
  }

  public void render(@Nonnull DecoratedBedBlockEntity blockEntity, float partialTicks,
                     @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer,
                     int light, int overlay) {
    List<Pair<Holder<BannerPattern>, DyeColor>> list = blockEntity.getPatternList();
    Level world = blockEntity.getLevel();

    if (world != null) {
      BlockState blockstate = blockEntity.getBlockState();
      DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> icallbackwrapper =
          DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType,
              BedBlock::getConnectedDirection, ChestBlock.FACING, blockstate, world,
              blockEntity.getBlockPos(), (p_228846_0_, p_228846_1_) -> false);
      int i = icallbackwrapper.apply(new BrightnessCombiner<>()).get(light);
      this.renderPiece(poseStack, buffer,
          blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headPiece : this.footPiece,
          blockstate.getValue(BedBlock.FACING), i, overlay, false, list);
    } else {
      this.renderPiece(poseStack, buffer, this.headPiece, Direction.SOUTH, light, overlay, false,
          list);
      this.renderPiece(poseStack, buffer, this.footPiece, Direction.SOUTH, light, overlay, true,
          list);
    }
  }

  private void renderPiece(PoseStack poseStack, MultiBufferSource buffer, ModelPart modelPart,
                           Direction direction, int light, int overlay, boolean isHead,
                           List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {
    poseStack.pushPose();
    poseStack.translate(0.0D, 0.5625D, isHead ? -1.0D : 0.0D);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.translate(0.5D, 0.5D, 0.5D);
    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
    poseStack.translate(-0.5D, -0.5D, -0.5D);
    Material material = new Material(Sheets.BANNER_SHEET,
        new ResourceLocation(BedspreadsConstants.MOD_ID, "entity/bed_base"));

    if (patterns != null) {
      renderPatterns(poseStack, buffer, light, overlay, modelPart, patterns);
    }
    VertexConsumer ivertexbuilder = material.buffer(buffer, RenderType::entityTranslucent);
    modelPart.render(poseStack, ivertexbuilder, light, overlay);
    poseStack.popPose();
  }

  public static void renderPatterns(PoseStack poseStack, MultiBufferSource buffer, int light,
                                    int overlay, ModelPart modelRenderer,
                                    List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {

    for (int i = 0; i < 17 && i < patterns.size(); ++i) {
      Pair<Holder<BannerPattern>, DyeColor> pair = patterns.get(i);
      float[] afloat = pair.getSecond().getTextureDiffuseColors();
      Material patternMaterial = new Material(Sheets.BANNER_SHEET,
          new ResourceLocation(BedspreadsConstants.MOD_ID,
              "entity/" + pair.getFirst().unwrapKey().map(key -> {
                ResourceLocation loc = key.location();
                return loc.getNamespace() + "/" + loc.getPath();
              }).orElse("minecraft/base")));
      modelRenderer.render(poseStack, patternMaterial.buffer(buffer, RenderType::entityTranslucent),
          light, overlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }
  }
}
