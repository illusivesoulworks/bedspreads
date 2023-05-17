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

package com.illusivesoulworks.bedspreads.mixin;

import com.illusivesoulworks.bedspreads.common.BedspreadsRegistry;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class MixinHooks {

  private static final Set<BlockState> DECORATED_BED_STATES = new HashSet<>();

  private static void initBlockStates() {

    if (DECORATED_BED_STATES.isEmpty()) {
      DECORATED_BED_STATES.addAll(
          BedspreadsRegistry.DECORATED_BED_BLOCK.get().getStateDefinition().getPossibleStates()
              .stream().filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD)
              .collect(Collectors.toSet()));
    }
  }

  public static boolean containsDecoratedBed(PoiType poiType, BlockState state) {
    initBlockStates();
    Holder<PoiType> poiTypeHolder =
        BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolder(PoiTypes.HOME).orElse(null);

    if (poiTypeHolder != null && poiType == poiTypeHolder.value()) {
      return DECORATED_BED_STATES.contains(state);
    }
    return false;
  }

  public static Optional<Holder<PoiType>> containsDecoratedBed(BlockState state) {
    initBlockStates();
    Holder<PoiType> poiTypeHolder =
        BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolder(PoiTypes.HOME).orElse(null);

    if (poiTypeHolder != null && DECORATED_BED_STATES.contains(state)) {
      return Optional.of(poiTypeHolder);
    }
    return Optional.empty();
  }
}
