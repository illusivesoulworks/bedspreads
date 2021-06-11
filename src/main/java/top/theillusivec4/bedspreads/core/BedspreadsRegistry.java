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

package top.theillusivec4.bedspreads.core;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlock;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlockEntity;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedItem;

public class BedspreadsRegistry {

  public static final Block DECORATED_BED_BLOCK = new DecoratedBedBlock();
  public static final Item DECORATED_BED_ITEM = new DecoratedBedItem();
  public static final BlockEntityType<DecoratedBedBlockEntity> DECORATED_BED_BE =
      FabricBlockEntityTypeBuilder.create(DecoratedBedBlockEntity::new, DECORATED_BED_BLOCK).build();
}
