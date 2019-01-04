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

package c4.cosmeticbeds.init;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.common.block.BlockCosmeticBed;
import c4.cosmeticbeds.common.item.ItemCosmeticBed;
import c4.cosmeticbeds.common.recipe.BedPatternRecipe;
import c4.cosmeticbeds.common.recipe.BedUnpatternRecipe;
import c4.cosmeticbeds.common.tileentity.TileEntityCosmeticBed;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = CosmeticBeds.MODID)
@GameRegistry.ObjectHolder(value = CosmeticBeds.MODID)
public class Registry {

    @GameRegistry.ObjectHolder("cosmetic_bed")
    public static final Block COSMETIC_BED_BLOCK = null;

    @GameRegistry.ObjectHolder("cosmetic_bed")
    public static final Item COSMETIC_BED_ITEM = null;

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
        evt.getRegistry().registerAll(new BedPatternRecipe(), new BedUnpatternRecipe());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        evt.getRegistry().register(new ItemCosmeticBed());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        evt.getRegistry().register(new BlockCosmeticBed());
        GameRegistry.registerTileEntity(TileEntityCosmeticBed.class, new ResourceLocation(CosmeticBeds.MODID,
                "cosmetic_bed_te"));
    }
}
