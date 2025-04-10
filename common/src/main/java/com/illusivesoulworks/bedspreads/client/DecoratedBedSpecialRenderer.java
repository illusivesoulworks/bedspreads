package com.illusivesoulworks.bedspreads.client;

import com.illusivesoulworks.bedspreads.common.BedspreadsData;
import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nonnull;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DecoratedBedSpecialRenderer implements SpecialModelRenderer<BedspreadsData> {

  private final DecoratedBedRenderer bedRenderer;

  public DecoratedBedSpecialRenderer(DecoratedBedRenderer bedRenderer) {
    this.bedRenderer = bedRenderer;
  }

  @Override
  public void render(BedspreadsData data, @Nonnull ItemDisplayContext itemDisplayContext,
                     @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource,
                     int light, int overlay, boolean flag) {
    this.bedRenderer.renderInHand(data, poseStack, bufferSource, light, overlay);
  }

  @Nullable
  @Override
  public BedspreadsData extractArgument(@Nonnull ItemStack stack) {
    return stack.getOrDefault(BedspreadsRegistry.BEDSPREADS_DATA.get(), BedspreadsData.EMPTY);
  }

  public record Unbaked() implements SpecialModelRenderer.Unbaked {

    public static final DecoratedBedSpecialRenderer.Unbaked INSTANCE =
        new DecoratedBedSpecialRenderer.Unbaked();
    public static final MapCodec<DecoratedBedSpecialRenderer.Unbaked>
        MAP_CODEC = MapCodec.unit(INSTANCE);

    @Nonnull
    @Override
    public MapCodec<DecoratedBedSpecialRenderer.Unbaked> type() {
      return MAP_CODEC;
    }

    @Override
    public SpecialModelRenderer<?> bake(@Nonnull EntityModelSet modelSet) {
      return new DecoratedBedSpecialRenderer(new DecoratedBedRenderer(modelSet));
    }
  }
}
