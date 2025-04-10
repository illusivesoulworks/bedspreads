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

import com.illusivesoulworks.bedspreads.client.DecoratedBedBlockEntityRenderer;
import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = BedspreadsConstants.MOD_ID, dist = Dist.CLIENT)
public class BedspreadsNeoForgeClientMod {

  public BedspreadsNeoForgeClientMod(IEventBus eventBus) {
    eventBus.addListener(BedspreadsNeoForgeClientMod::entityRenderers);
  }

  public static void entityRenderers(final EntityRenderersEvent.RegisterRenderers evt) {
    evt.registerBlockEntityRenderer(BedspreadsRegistry.DECORATED_BED_BLOCK_ENTITY.get(),
                                    DecoratedBedBlockEntityRenderer::new);
  }
}
