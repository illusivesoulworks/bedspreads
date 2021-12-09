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

package top.theillusivec4.bedspreads.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.bedspreads.BedspreadsMod;
import top.theillusivec4.bedspreads.common.recipe.BedAddPatternRecipe;
import top.theillusivec4.bedspreads.common.recipe.BedRemovePatternRecipe;

public class DecoratedBedsRegistry {

  private static final String ADD_PATTERN = "add_pattern";
  private static final String REMOVE_PATTERN = "remove_pattern";
  private static final String DECORATED_BED = "decorated_bed";

  public static final Block DECORATED_BED_BLOCK = new DecoratedBedBlock();
  public static final Item DECORATED_BED_ITEM = new DecoratedBedItem();
  public static final BlockEntityType<DecoratedBedBlockEntity> DECORATED_BED_TE =
      BlockEntityType.Builder.of(DecoratedBedBlockEntity::new,
          DecoratedBedsRegistry.DECORATED_BED_BLOCK).build(null);

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> evt) {
      DECORATED_BED_BLOCK.setRegistryName(BedspreadsMod.MODID, DECORATED_BED);
      evt.getRegistry().register(DECORATED_BED_BLOCK);
    }

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> evt) {
      DECORATED_BED_ITEM.setRegistryName(BedspreadsMod.MODID, DECORATED_BED);
      evt.getRegistry().register(DECORATED_BED_ITEM);
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<BlockEntityType<?>> evt) {
      DECORATED_BED_TE.setRegistryName(BedspreadsMod.MODID, DECORATED_BED);
      evt.getRegistry().register(DECORATED_BED_TE);
    }

    @SubscribeEvent
    public static void onRecipeRegistry(final RegistryEvent.Register<RecipeSerializer<?>> evt) {
      BedAddPatternRecipe.CRAFTING_ADD_PATTERN.setRegistryName(BedspreadsMod.MODID, ADD_PATTERN);
      BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN
          .setRegistryName(BedspreadsMod.MODID, REMOVE_PATTERN);
      evt.getRegistry().registerAll(BedAddPatternRecipe.CRAFTING_ADD_PATTERN,
          BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN);
    }
  }
}
