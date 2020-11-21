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

package top.theillusivec4.bedspreads;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.bedspreads.client.renderer.DecoratedBedTileEntityRenderer;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;
import top.theillusivec4.bedspreads.mixin.PointOfInterestTypeMixin;

@Mod(Bedspreads.MODID)
public class Bedspreads {

  public static final String MODID = "bedspreads";

  public Bedspreads() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::completeLoad);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ClientRegistry.bindTileEntityRenderer(DecoratedBedsRegistry.DECORATED_BED_TE,
        DecoratedBedTileEntityRenderer::new);
  }

  private void completeLoad(final FMLLoadCompleteEvent evt) {
    PointOfInterestTypeMixin poit = (PointOfInterestTypeMixin) PointOfInterestType.HOME;
    Set<BlockState> states =
        DecoratedBedsRegistry.DECORATED_BED_BLOCK.getStateContainer().getValidStates().stream()
            .filter((state) ->
                state.get(BedBlock.PART) == BedPart.HEAD).collect(Collectors.toSet());
    states.forEach(
        state -> PointOfInterestTypeMixin.getPoit().put(state, PointOfInterestType.HOME));
    states.addAll(poit.getBlockStates());
    poit.setBlockStates(ImmutableSet.copyOf(states));
  }

  @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientProxy {

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre evt) {

      if (evt.getMap().getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {

        for (BannerPattern pattern : BannerPattern.values()) {
          evt.addSprite(new ResourceLocation(MODID, "entity/" + pattern.getFileName()));
        }
        evt.addSprite(new ResourceLocation(MODID, "entity/bed_base"));
      }
    }
  }
}
