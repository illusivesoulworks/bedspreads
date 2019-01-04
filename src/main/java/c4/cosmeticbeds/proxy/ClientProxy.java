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

package c4.cosmeticbeds.proxy;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.client.renderer.BedPatternItemStackRenderer;
import c4.cosmeticbeds.client.renderer.TileEntityCosmeticBedRenderer;
import c4.cosmeticbeds.common.tileentity.TileEntityCosmeticBed;
import c4.cosmeticbeds.init.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = CosmeticBeds.MODID, value = Side.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void init(FMLInitializationEvent evt) {
        Registry.COSMETIC_BED_ITEM.setTileEntityItemStackRenderer(new BedPatternItemStackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCosmeticBed.class, new TileEntityCosmeticBedRenderer());
    }

    @SubscribeEvent
    public static void onModelLoad(ModelRegistryEvent evt) {

        for (int i = 0; i < EnumDyeColor.values().length; i++) {
            ModelLoader.setCustomModelResourceLocation(Registry.COSMETIC_BED_ITEM, i, new ModelResourceLocation(Registry
                    .COSMETIC_BED_ITEM.getRegistryName(), "inventory"));
        }
    }
}
