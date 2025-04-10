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

import com.illusivesoulworks.bedspreads.BedspreadsCommonMod;
import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;
import com.illusivesoulworks.bedspreads.common.integration.glowingbanners.GlowingBannersIntegration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
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
    BannerPatternLayers list = blockEntity.getPatternList();
    Level world = blockEntity.getLevel();

    if (world != null) {
      BlockState blockstate = blockEntity.getBlockState();
      DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> icallbackwrapper =
          DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType,
                                                  BedBlock::getConnectedDirection,
                                                  ChestBlock.FACING, blockstate, world,
                                                  blockEntity.getBlockPos(),
                                                  (levelAccessor, blockPos) -> false);
      int i = icallbackwrapper.apply(new BrightnessCombiner<>()).get(light);
      this.renderPiece(poseStack, buffer,
                       blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headPiece :
                           this.footPiece,
                       blockstate.getValue(BedBlock.FACING), i, overlay, false, list, blockEntity);
    } else {
      this.renderPiece(poseStack, buffer, this.headPiece, Direction.SOUTH, light, overlay, false,
                       list, blockEntity);
      this.renderPiece(poseStack, buffer, this.footPiece, Direction.SOUTH, light, overlay, true,
                       list, blockEntity);
    }
  }

  private static int getLight(int light, int layer, DecoratedBedBlockEntity bedBlockEntity) {

    if (BedspreadsCommonMod.isGlowingBannersLoaded && GlowingBannersIntegration.isGlowing(
        bedBlockEntity, layer)) {
      return 15728880;
    }
    return light;
  }

  private void renderPiece(PoseStack poseStack, MultiBufferSource buffer, ModelPart modelPart,
                           Direction direction, int light, int overlay, boolean isHead,
                           BannerPatternLayers patterns, DecoratedBedBlockEntity blockEntity) {
    poseStack.pushPose();
    poseStack.translate(0.0D, 0.5625D, isHead ? -1.0D : 0.0D);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.translate(0.5D, 0.5D, 0.5D);
    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
    poseStack.translate(-0.5D, -0.5D, -0.5D);
    Material material = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(
        BedspreadsConstants.MOD_ID, "entity/bed_base"));

    if (patterns != null) {
      renderPatterns(poseStack, buffer, light, overlay, modelPart, patterns, blockEntity);
    }
    VertexConsumer ivertexbuilder = material.buffer(buffer, RenderType::entityTranslucent);
    modelPart.render(poseStack, ivertexbuilder, light, overlay);
    poseStack.popPose();
  }

  public static void renderPatterns(PoseStack poseStack, MultiBufferSource buffer, int light,
                                    int overlay, ModelPart modelRenderer,
                                    BannerPatternLayers patterns,
                                    DecoratedBedBlockEntity blockEntity) {
    Material baseMaterial = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(
        BedspreadsConstants.MOD_ID, "entity/banner/minecraft/base"));
    int newLight = getLight(light, -1, blockEntity);
    modelRenderer.render(poseStack,
                         baseMaterial.buffer(buffer, RenderType::entityTranslucent),
                         newLight, overlay, blockEntity.getBannerColor().getTextureDiffuseColor());
    List<BannerPatternLayers.Layer> layers = patterns.layers();

    for (int i = 0; i < 16 && i < layers.size(); ++i) {
      BannerPatternLayers.Layer layer = layers.get(i);
      Holder<BannerPattern> pattern = layer.pattern();
      int color = layer.color().getTextureDiffuseColor();
      String path = "entity/" + pattern.unwrapKey().map(key -> {
        ResourceLocation loc = key.location();
        return loc.getNamespace() + "/" + loc.getPath();
      }).orElse("minecraft/base");
      Material patternMaterial = new Material(Sheets.BANNER_SHEET,
                                              ResourceLocation.fromNamespaceAndPath(
                                                  BedspreadsConstants.MOD_ID, path));
      TextureAtlasSprite sprite = patternMaterial.sprite();
      ResourceLocation resourceLocation = sprite.contents().name();

      if (resourceLocation != MissingTextureAtlasSprite.getLocation()) {
        newLight = getLight(light, i, blockEntity);
        modelRenderer.render(poseStack,
                             patternMaterial.buffer(buffer, RenderType::entityTranslucent),
                             newLight, overlay, color);
      }
    }
  }
}
