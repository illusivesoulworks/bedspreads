package com.illusivesoulworks.bedspreads.common.integration.glowingbanners;

import com.illusivesoulworks.bedspreads.common.DecoratedBedBlockEntity;

public class GlowingBannersIntegration {

  public static boolean isGlowing(DecoratedBedBlockEntity bedBlockEntity) {
    return bedBlockEntity.getBanner().getOrCreateTagElement("BlockEntityTag")
        .getBoolean("isGlowing");
  }
}
