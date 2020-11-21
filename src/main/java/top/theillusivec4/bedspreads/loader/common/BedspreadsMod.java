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

package top.theillusivec4.bedspreads.loader.common;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;
import top.theillusivec4.bedspreads.core.Bedspreads;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;
import top.theillusivec4.bedspreads.core.recipe.BedAddPatternRecipe;
import top.theillusivec4.bedspreads.core.recipe.BedRemovePatternRecipe;
import top.theillusivec4.bedspreads.loader.mixin.PointOfInterestTypeMixin;

public class BedspreadsMod implements ModInitializer {

  private static final String ADD_PATTERN = Bedspreads.MODID + ":add_pattern";
  private static final String REMOVE_PATTERN = Bedspreads.MODID + ":remove_pattern";

  private static final String DECORATED_BED = Bedspreads.MODID + ":decorated_bed";

  @Override
  public void onInitialize() {
    // Recipe Serializers
    Registry.register(Registry.RECIPE_SERIALIZER, ADD_PATTERN,
        BedAddPatternRecipe.CRAFTING_ADD_PATTERN);
    Registry.register(Registry.RECIPE_SERIALIZER, REMOVE_PATTERN,
        BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN);

    // Block, Item, and Block Entity
    Registry.register(Registry.BLOCK, DECORATED_BED, BedspreadsRegistry.DECORATED_BED_BLOCK);
    Registry.register(Registry.ITEM, DECORATED_BED, BedspreadsRegistry.DECORATED_BED_ITEM);
    Registry
        .register(Registry.BLOCK_ENTITY_TYPE, DECORATED_BED, BedspreadsRegistry.DECORATED_BED_BE);

    // Villager POIT
    PointOfInterestTypeMixin poit = (PointOfInterestTypeMixin) PointOfInterestType.HOME;
    Set<BlockState> states =
        BedspreadsRegistry.DECORATED_BED_BLOCK.getStateManager().getStates().stream()
            .filter((state) ->
                state.get(BedBlock.PART) == BedPart.HEAD).collect(Collectors.toSet());
    states.forEach(
        state -> PointOfInterestTypeMixin.getPoit().put(state, PointOfInterestType.HOME));
    states.addAll(poit.getBlockStates());
    poit.setBlockStates(ImmutableSet.copyOf(states));
  }
}
