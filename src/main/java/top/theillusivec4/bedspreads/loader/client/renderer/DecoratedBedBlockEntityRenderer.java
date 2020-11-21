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

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.DoubleBlockProperties.PropertySource;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.core.Bedspreads;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlock;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlockEntity;

public class DecoratedBedBlockEntityRenderer extends BlockEntityRenderer<DecoratedBedBlockEntity> {

  private final ModelPart headPiece;
  private final ModelPart footPiece;
  private final ModelPart[] legPieces = new ModelPart[4];

  public DecoratedBedBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
    this.headPiece = new ModelPart(64, 64, 0, 0);
    this.headPiece.addCuboid(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
    this.footPiece = new ModelPart(64, 64, 0, 22);
    this.footPiece.addCuboid(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
    this.legPieces[0] = new ModelPart(64, 64, 50, 0);
    this.legPieces[1] = new ModelPart(64, 64, 50, 6);
    this.legPieces[2] = new ModelPart(64, 64, 50, 12);
    this.legPieces[3] = new ModelPart(64, 64, 50, 18);
    this.legPieces[0].addCuboid(0.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[1].addCuboid(0.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[2].addCuboid(-16.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[3].addCuboid(-16.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[0].pitch = ((float) Math.PI / 2F);
    this.legPieces[1].pitch = ((float) Math.PI / 2F);
    this.legPieces[2].pitch = ((float) Math.PI / 2F);
    this.legPieces[3].pitch = ((float) Math.PI / 2F);
    this.legPieces[0].roll = 0.0F;
    this.legPieces[1].roll = ((float) Math.PI / 2F);
    this.legPieces[2].roll = ((float) Math.PI * 1.5F);
    this.legPieces[3].roll = (float) Math.PI;
  }

  @Override
  public void render(DecoratedBedBlockEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    List<Pair<BannerPattern, DyeColor>> list = entity.getPatternList();
    World world = entity.getWorld();

    if (world != null) {
      BlockState blockstate = entity.getCachedState();
      PropertySource<DecoratedBedBlockEntity> propertySource = DoubleBlockProperties
          .toPropertySource(BedspreadsRegistry.DECORATED_BED_BE, BedBlock::getBedPart,
              BedBlock::getOppositePartDirection, ChestBlock.FACING, blockstate, world,
              entity.getPos(), (worldAccess, blockPos) -> false);
      int k = propertySource.apply(new LightmapCoordinatesRetriever<>()).get(light);
      this.renderPiece(matrices, vertexConsumers, blockstate.get(BedBlock.PART) == BedPart.HEAD,
          blockstate.get(DecoratedBedBlock.FACING), k, overlay, false, list);
    } else {
      this.renderPiece(matrices, vertexConsumers, true, Direction.SOUTH, light, overlay, false,
          list);
      this.renderPiece(matrices, vertexConsumers, false, Direction.SOUTH, light, overlay, true,
          list);
    }
  }

  public static void renderPatterns(MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider, int light, int overlay,
      ModelPart modelRenderer, SpriteIdentifier material,
      List<Pair<BannerPattern, DyeColor>> patterns) {
    modelRenderer.render(matrixStack,
        material.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityTranslucent),
        light, overlay);

    for (int i = 0; i < 17 && i < patterns.size(); ++i) {
      Pair<BannerPattern, DyeColor> pair = patterns.get(i);
      float[] afloat = pair.getSecond().getColorComponents();
      SpriteIdentifier patternMaterial = new SpriteIdentifier(
          PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
          new Identifier(Bedspreads.MODID, "entity/" + pair.getFirst().getName()));
      modelRenderer.render(matrixStack, patternMaterial
              .getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityTranslucent), light,
          overlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }
  }

  private void renderPiece(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
      boolean isHead, Direction direction, int light, int overlay, boolean p_228847_8_,
      List<Pair<BannerPattern, DyeColor>> patterns) {
    this.headPiece.visible = isHead;
    this.footPiece.visible = !isHead;
    this.legPieces[0].visible = !isHead;
    this.legPieces[1].visible = isHead;
    this.legPieces[2].visible = !isHead;
    this.legPieces[3].visible = isHead;
    matrixStack.push();
    matrixStack.translate(0.0D, 0.5625D, p_228847_8_ ? -1.0D : 0.0D);
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
    matrixStack.translate(0.5D, 0.5D, 0.5D);
    matrixStack
        .multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F + direction.asRotation()));
    matrixStack.translate(-0.5D, -0.5D, -0.5D);
    SpriteIdentifier material = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
        new Identifier(Bedspreads.MODID, "entity/bed_base"));

    if (patterns != null) {
      renderPatterns(matrixStack, vertexConsumerProvider, light, overlay, this.headPiece, material,
          patterns);
      renderPatterns(matrixStack, vertexConsumerProvider, light, overlay, this.footPiece, material,
          patterns);
    }
    VertexConsumer ivertexbuilder = material
        .getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
    this.legPieces[0].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[1].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[2].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[3].render(matrixStack, ivertexbuilder, light, overlay);
    matrixStack.pop();
  }
}