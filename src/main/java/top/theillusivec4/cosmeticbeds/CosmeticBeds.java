/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of Cosmetic Beds, a mod made for Minecraft.
 *
 * Cosmetic Beds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cosmetic Beds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cosmetic Beds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cosmeticbeds;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.cosmeticbeds.client.renderer.CosmeticBedTileEntityRenderer;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedsRegistry;

@Mod(CosmeticBeds.MODID)
public class CosmeticBeds {

  public static final String MODID = "cosmeticbeds";

  public CosmeticBeds() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ClientRegistry.bindTileEntityRenderer(CosmeticBedsRegistry.cosmeticBedTe,
        CosmeticBedTileEntityRenderer::new);
  }

  @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientProxy {

    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre evt) {

      if (evt.getMap().getBasePath() == AtlasTexture.LOCATION_BLOCKS_TEXTURE) {

        for (BannerPattern pattern : BannerPattern.values()) {
          evt.addSprite(new ResourceLocation(MODID, "entity/" + pattern.getFileName()));
        }
      }
    }
  }
}
