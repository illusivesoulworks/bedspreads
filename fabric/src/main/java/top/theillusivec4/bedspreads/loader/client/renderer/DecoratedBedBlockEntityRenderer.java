package top.theillusivec4.bedspreads.loader.client.renderer;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.core.Bedspreads;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlockEntity;

public class DecoratedBedBlockEntityRenderer
    implements BlockEntityRenderer<DecoratedBedBlockEntity> {

  private final ModelPart bedHead;
  private final ModelPart bedFoot;

  public DecoratedBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    this.bedHead = ctx.getLayerModelPart(EntityModelLayers.BED_HEAD);
    this.bedFoot = ctx.getLayerModelPart(EntityModelLayers.BED_FOOT);
  }

  public void render(DecoratedBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack,
                     VertexConsumerProvider vertexConsumerProvider, int i, int j) {
    List<Pair<BannerPattern, DyeColor>> list = bedBlockEntity.getPatternList();
    World world = bedBlockEntity.getWorld();

    if (world != null) {
      BlockState blockState = bedBlockEntity.getCachedState();
      DoubleBlockProperties.PropertySource<? extends BedBlockEntity> propertySource =
          DoubleBlockProperties.toPropertySource(
              BlockEntityType.BED, BedBlock::getBedPart, BedBlock::getOppositePartDirection,
              ChestBlock.FACING, blockState, world, bedBlockEntity.getPos(),
              (worldAccess, blockPos) -> false);
      int k = (propertySource.apply(new LightmapCoordinatesRetriever<>())).get(i);
      this.renderPart(matrixStack, vertexConsumerProvider,
          blockState.get(BedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot,
          blockState.get(BedBlock.FACING), k, j, false, list);
    } else {
      this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.SOUTH, i, j,
          false, list);
      this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.SOUTH, i, j,
          true, list);
    }
  }

  private void renderPart(MatrixStack matrix, VertexConsumerProvider vertexConsumers,
                          ModelPart modelPart, Direction direction, int light, int overlay,
                          boolean isFoot, List<Pair<BannerPattern, DyeColor>> patterns) {
    matrix.push();
    matrix.translate(0.0D, 0.5625D, isFoot ? -1.0D : 0.0D);
    matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
    matrix.translate(0.5D, 0.5D, 0.5D);
    matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F + direction.asRotation()));
    matrix.translate(-0.5D, -0.5D, -0.5D);
    SpriteIdentifier material = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
        new Identifier(Bedspreads.MOD_ID, "entity/bed_base"));

    if (patterns != null) {
      renderCanvas(matrix, vertexConsumers, light, overlay, modelPart, patterns);
    }
    VertexConsumer vertexConsumer =
        material.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent);
    modelPart.render(matrix, vertexConsumer, light, overlay);
    matrix.pop();
  }

  public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                  int light, int overlay, ModelPart canvas,
                                  List<Pair<BannerPattern, DyeColor>> patterns) {

    for (int i = 0; i < 17 && i < patterns.size(); ++i) {
      Pair<BannerPattern, DyeColor> pair = patterns.get(i);
      float[] fs = pair.getSecond().getColorComponents();
      SpriteIdentifier patternMaterial =
          new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
              new Identifier(Bedspreads.MOD_ID, "entity/" + pair.getFirst().getName()));
      canvas.render(matrices,
          patternMaterial.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent),
          light, overlay, fs[0], fs[1], fs[2], 1.0F);
    }
  }
}
