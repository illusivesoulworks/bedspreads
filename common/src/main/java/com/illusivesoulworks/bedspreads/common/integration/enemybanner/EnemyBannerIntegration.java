package com.illusivesoulworks.bedspreads.common.integration.enemybanner;

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.enemybanner.BannerUtil;
import com.xiaohunao.enemybanner.EnemyBanner;
import com.xiaohunao.enemybanner.EntityBannerPattern;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;

public class EnemyBannerIntegration {

  public static boolean renderEntityBanner(PoseStack poseStack, ModelPart modelPart,
                                           MultiBufferSource bufferSource, int packedLight,
                                           int packedOverlay,
                                           List<Pair<Holder<BannerPattern>, DyeColor>> patterns,
                                           boolean isHead) {
    boolean flag = false;

    for (Pair<Holder<BannerPattern>, DyeColor> pattern : patterns) {
      BannerPattern bannerPattern = pattern.getFirst().value();
      Minecraft minecraft = Minecraft.getInstance();

      if (bannerPattern instanceof EntityBannerPattern entityPattern && minecraft.level != null
          && !isHead) {
        poseStack.pushPose();
        modelPart.translateAndRotate(poseStack);
        poseStack.translate(0.5D, -0.275D, 0.0D);
        poseStack.scale(0.525F, 0.525F, 0.525F);
        LivingEntity livingEntity =
            BannerUtil.createOrGetEntity(entityPattern.entityType, minecraft.level);
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        BannerUtil.renderEntity(entityRenderDispatcher, livingEntity, poseStack, bufferSource,
                                packedLight);
        poseStack.popPose();
        flag = true;
      }

      if (BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(bannerPattern)
          .is(EnemyBanner.FUNCTION_SILKS_TAG_KEY)) {
        String hashName = bannerPattern.getHashname();
        ResourceLocation resource = new ResourceLocation(BedspreadsConstants.MOD_ID,
                                                         "textures/entity/banner/enemybanner/"
                                                             + hashName + ".png");
        VertexConsumer basicFlagVertexconsumer =
            bufferSource.getBuffer(RenderType.entitySolid(resource));
        modelPart.render(poseStack, basicFlagVertexconsumer, packedLight, packedOverlay);
        flag = true;
      }

      if (BuiltInRegistries.BANNER_PATTERN.wrapAsHolder(bannerPattern)
          .is(EnemyBanner.COLOR_SILKS_TAG_KEY)) {
        String hashName = bannerPattern.getHashname();
        ResourceLocation bar = new ResourceLocation(BedspreadsConstants.MOD_ID,
                                                    "textures/entity/banner/enemybanner/" + hashName
                                                        + ".png");
        VertexConsumer barVertexConsumer =
            bufferSource.getBuffer(RenderType.entityTranslucent(bar));
        modelPart.render(poseStack, barVertexConsumer, packedLight, packedOverlay);
        flag = true;
      }
    }
    return flag;
  }
}
