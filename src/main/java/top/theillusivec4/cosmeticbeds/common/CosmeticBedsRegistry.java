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

package top.theillusivec4.cosmeticbeds.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.cosmeticbeds.CosmeticBeds;

@ObjectHolder(CosmeticBeds.MODID)
public class CosmeticBedsRegistry {

  @ObjectHolder("cosmetic_bed")
  public static final Block COSMETIC_BED_BLOCK = null;

  @ObjectHolder("cosmetic_bed")
  public static final Item COSMETIC_BED_ITEM = null;

  @ObjectHolder("cosmetic_bed")
  public static final TileEntityType<CosmeticBedTileEntity> COSMETIC_BED_TE = null;
}
