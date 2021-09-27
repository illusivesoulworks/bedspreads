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

package top.theillusivec4.bedspreads;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.bedspreads.client.renderer.DecoratedBedTileEntityRenderer;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;
import top.theillusivec4.bedspreads.mixin.MixinPointOfInterestType;

@Mod(BedspreadsMod.MODID)
public class BedspreadsMod {

  public static final String MODID = "bedspreads";

  public BedspreadsMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::completeLoad);
  }

  private void completeLoad(final FMLLoadCompleteEvent evt) {
    MixinPointOfInterestType poit = (MixinPointOfInterestType) PoiType.HOME;
    Set<BlockState> states =
        DecoratedBedsRegistry.DECORATED_BED_BLOCK.getStateDefinition().getPossibleStates().stream()
            .filter((state) ->
                state.getValue(BedBlock.PART) == BedPart.HEAD).collect(Collectors.toSet());
    states.forEach(
        state -> MixinPointOfInterestType.getPoit().put(state, PoiType.HOME));
    states.addAll(poit.getBlockStates());
    poit.setBlockStates(ImmutableSet.copyOf(states));
  }

  @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientProxy {

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers evt) {
      evt.registerBlockEntityRenderer(DecoratedBedsRegistry.DECORATED_BED_TE,
          DecoratedBedTileEntityRenderer::new);
    }

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre evt) {

      if (evt.getMap().location() == InventoryMenu.BLOCK_ATLAS) {

        for (BannerPattern pattern : BannerPattern.values()) {
          evt.addSprite(new ResourceLocation(MODID, "entity/" + pattern.getFilename()));
        }
        evt.addSprite(new ResourceLocation(MODID, "entity/bed_base"));
      }
    }
  }
}
