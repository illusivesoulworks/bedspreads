/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Bedspreads is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Bedspreads.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.bedspreads;

import com.illusivesoulworks.bedspreads.client.DecoratedBedRenderer;
import com.illusivesoulworks.bedspreads.client.DecoratedBedSpecialRenderer;
import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.ResourceLocation;

public class BedspreadsFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    SpecialModelRenderers.ID_MAPPER.put(
        ResourceLocation.fromNamespaceAndPath(BedspreadsConstants.MOD_ID, "decorated_bed"),
        DecoratedBedSpecialRenderer.Unbaked.MAP_CODEC);
    BlockEntityRenderers.register(BedspreadsRegistry.DECORATED_BED_BLOCK_ENTITY.get(),
                                  DecoratedBedRenderer::new);
  }
}
