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

package top.theillusivec4.cosmeticbeds;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.cosmeticbeds.client.renderer.CosmeticBedTileEntityRenderer;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedBlock;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedItem;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedsRegistry;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedTileEntity;
import top.theillusivec4.cosmeticbeds.common.recipe.BedAddPatternRecipe;
import top.theillusivec4.cosmeticbeds.common.recipe.BedRemovePatternRecipe;

@Mod(CosmeticBeds.MODID)
public class CosmeticBeds {

  public static final String MODID = "cosmeticbeds";

  private static final String ADD_PATTERN = "add_pattern";
  private static final String REMOVE_PATTERN = "remove_pattern";

  public CosmeticBeds() {
  }

  @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientProxy {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent evt) {
      ClientRegistry.bindTileEntitySpecialRenderer(CosmeticBedTileEntity.class,
          new CosmeticBedTileEntityRenderer());
    }
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> evt) {
      evt.getRegistry().register(new CosmeticBedBlock());
    }

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> evt) {
      evt.getRegistry().register(new CosmeticBedItem());
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> evt) {
      evt.getRegistry().register(TileEntityType.Builder
          .create(CosmeticBedTileEntity::new, CosmeticBedsRegistry.COSMETIC_BED_BLOCK).build(null)
          .setRegistryName("cosmetic_bed"));
    }

    @SubscribeEvent
    public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> evt) {
      BedAddPatternRecipe.CRAFTING_ADD_PATTERN.setRegistryName(MODID, ADD_PATTERN);
      BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN.setRegistryName(MODID, REMOVE_PATTERN);
      evt.getRegistry().registerAll(BedAddPatternRecipe.CRAFTING_ADD_PATTERN,
          BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN);
    }
  }
}
