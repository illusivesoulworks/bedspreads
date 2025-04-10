package com.illusivesoulworks.bedspreads.common.integration.glowingbanners;

import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;
import me.ultrusmods.glowingbanners.component.BannerGlowComponent;
import me.ultrusmods.glowingbanners.registry.GlowBannersDataComponents;

public class GlowingBannersIntegration {

  public static boolean isGlowing(DecoratedBedBlockEntity bedBlockEntity, int layer) {
    BannerGlowComponent glow =
        bedBlockEntity.components().get(GlowBannersDataComponents.BANNER_GLOW);

    if (glow != null) {
      return glow.shouldAllGlow() || glow.isLayerGlowing(layer);
    }
    return false;
  }
}
