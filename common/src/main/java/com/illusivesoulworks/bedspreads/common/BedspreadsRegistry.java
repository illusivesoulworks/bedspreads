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

package com.illusivesoulworks.bedspreads.common;

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.illusivesoulworks.bedspreads.common.recipe.AddPatternRecipe;
import com.illusivesoulworks.bedspreads.common.recipe.RemovePatternRecipe;
import com.illusivesoulworks.bedspreads.platform.Services;
import com.illusivesoulworks.bedspreads.registry.RegistryObject;
import com.illusivesoulworks.bedspreads.registry.RegistryProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BedspreadsRegistry {

  private static final String ADD_PATTERN = "add_pattern";
  private static final String REMOVE_PATTERN = "remove_pattern";
  private static final String DECORATED_BED = "decorated_bed";

  public static final RegistryProvider<Block> BLOCKS =
      RegistryProvider.get(BuiltInRegistries.BLOCK, BedspreadsConstants.MOD_ID);
  public static final RegistryProvider<Item> ITEMS =
      RegistryProvider.get(BuiltInRegistries.ITEM, BedspreadsConstants.MOD_ID);
  public static final RegistryProvider<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
      RegistryProvider.get(BuiltInRegistries.BLOCK_ENTITY_TYPE, BedspreadsConstants.MOD_ID);
  public static final RegistryProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS =
      RegistryProvider.get(BuiltInRegistries.RECIPE_SERIALIZER, BedspreadsConstants.MOD_ID);

  public static final RegistryObject<Block> DECORATED_BED_BLOCK =
      BLOCKS.register(DECORATED_BED, DecoratedBedBlock::new);
  public static final RegistryObject<Item> DECORATED_BED_ITEM =
      ITEMS.register(DECORATED_BED, Services.REGISTRY::getItem);
  public static final RegistryObject<BlockEntityType<DecoratedBedBlockEntity>>
      DECORATED_BED_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(DECORATED_BED,
      () -> Services.REGISTRY.createBlockEntityType(DecoratedBedBlockEntity::new,
          DECORATED_BED_BLOCK.get()));
  public static final RegistryObject<RecipeSerializer<?>> ADD_PATTERN_RECIPE =
      RECIPE_SERIALIZERS.register(ADD_PATTERN, () -> AddPatternRecipe.CRAFTING_ADD_PATTERN);
  public static final RegistryObject<RecipeSerializer<?>> REMOVE_PATTERN_RECIPE =
      RECIPE_SERIALIZERS.register(REMOVE_PATTERN,
          () -> RemovePatternRecipe.CRAFTING_REMOVE_PATTERN);

  public static void init() {
    // NO-OP
  }
}
