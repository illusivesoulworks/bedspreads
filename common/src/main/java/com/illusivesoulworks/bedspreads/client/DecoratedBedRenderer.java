package com.illusivesoulworks.bedspreads.client;

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.illusivesoulworks.bedspreads.common.BedspreadsData;
import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.phys.Vec3;

public class DecoratedBedRenderer implements BlockEntityRenderer<DecoratedBedBlockEntity> {

  private final Model headModel;
  private final Model footModel;

  public DecoratedBedRenderer(BlockEntityRendererProvider.Context context) {
    this(context.getModelSet());
  }

  public DecoratedBedRenderer(EntityModelSet modelSet) {
    this.headModel =
        new Model.Simple(modelSet.bakeLayer(ModelLayers.BED_HEAD), RenderType::entitySolid);
    this.footModel =
        new Model.Simple(modelSet.bakeLayer(ModelLayers.BED_FOOT), RenderType::entitySolid);
  }

  public void render(
      DecoratedBedBlockEntity blockEntity, float tickDelta, @Nonnull PoseStack poseStack,
      @Nonnull MultiBufferSource bufferSource, int light, int overlay, @Nonnull Vec3 vec3
  ) {
    Level level = blockEntity.getLevel();

    if (level != null) {
      Material material = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(
          BedspreadsConstants.MOD_ID, "entity/bed_base"));
      BlockState blockstate = blockEntity.getBlockState();
      DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborcombineresult =
          DoubleBlockCombiner.combineWithNeigbour(
              BlockEntityType.BED,
              BedBlock::getBlockType,
              BedBlock::getConnectedDirection,
              ChestBlock.FACING,
              blockstate,
              level,
              blockEntity.getBlockPos(),
              (levelAccessor, blockPos) -> false
          );
      int i = neighborcombineresult.apply(new BrightnessCombiner<>()).get(light);
      this.renderPiece(
          material,
          blockEntity.getBannerColor().getTextureDiffuseColor(),
          blockEntity.getPatternList(),
          poseStack,
          bufferSource,
          blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headModel : this.footModel,
          blockstate.getValue(BedBlock.FACING),
          i,
          overlay,
          false
      );
    }
  }

  public void renderInHand(BedspreadsData data, PoseStack poseStack, MultiBufferSource bufferSource,
                           int packedLight, int packedOverlay) {
    Material material = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(
        BedspreadsConstants.MOD_ID, "entity/bed_base"));
    BannerPatternLayers patterns =
        data.banner().getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
    int color = DyeColor.WHITE.getTextureDiffuseColor();

    if (data.banner().getItem() instanceof BannerItem bannerItem) {
      color = bannerItem.getColor().getTextureDiffuseColor();
    }
    this.renderPiece(material, color, patterns, poseStack, bufferSource, this.headModel,
                     Direction.SOUTH, packedLight, packedOverlay, false);
    this.renderPiece(material, color, patterns, poseStack, bufferSource, this.footModel,
                     Direction.SOUTH, packedLight, packedOverlay, true);
  }

  private void renderPiece(
      Material material,
      int color,
      BannerPatternLayers patterns,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      Model model,
      Direction direction,
      int packedLight,
      int packedOverlay,
      boolean isFeet
  ) {
    poseStack.pushPose();
    poseStack.translate(0.0F, 0.5625F, isFeet ? -1.0F : 0.0F);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
    poseStack.translate(-0.5F, -0.5F, -0.5F);

    if (patterns != null) {
      renderPatterns(poseStack, bufferSource, packedLight, packedOverlay, model, color, patterns);
    }
    VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entitySolid);
    model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
    poseStack.popPose();
  }

  private void renderPatterns(PoseStack poseStack, MultiBufferSource buffer, int light,
                              int overlay, Model modelRenderer, int color,
                              BannerPatternLayers patterns) {
    Material baseMaterial = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(
        BedspreadsConstants.MOD_ID, "entity/banner/minecraft/base"));
    modelRenderer.renderToBuffer(poseStack,
                                 baseMaterial.buffer(buffer, RenderType::entitySolid), light,
                                 overlay, color);
    List<BannerPatternLayers.Layer> layers = patterns.layers();

    for (int i = 0; i < 16 && i < layers.size(); ++i) {
      BannerPatternLayers.Layer layer = layers.get(i);
      Holder<BannerPattern> pattern = layer.pattern();
      int patternColor = layer.color().getTextureDiffuseColor();
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
        modelRenderer.renderToBuffer(poseStack,
                                     patternMaterial.buffer(buffer, RenderType::entityNoOutline),
                                     light, overlay, patternColor);
      }
    }
  }
}
