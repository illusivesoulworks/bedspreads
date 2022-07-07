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

package com.illusivesoulworks.bedspreads.platform;

import com.illusivesoulworks.bedspreads.common.DecoratedBedItem;
import com.illusivesoulworks.bedspreads.common.item.DecoratedBedForgeItem;
import com.illusivesoulworks.bedspreads.platform.services.IPlatformRegistry;
import com.illusivesoulworks.bedspreads.registry.RegistryObject;
import com.illusivesoulworks.bedspreads.registry.RegistryProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgePlatformRegistry implements IPlatformRegistry {

  @Override
  public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey,
                                        String modId) {
    final Optional<? extends ModContainer> containerOpt = ModList.get().getModContainerById(modId);

    if (containerOpt.isEmpty()) {
      throw new NullPointerException("Cannot find mod container for id " + modId);
    }
    final ModContainer cont = containerOpt.get();

    if (cont instanceof FMLModContainer fmlModContainer) {
      final DeferredRegister<T> register = DeferredRegister.create(resourceKey, modId);
      register.register(fmlModContainer.getEventBus());
      return new Provider<>(modId, register);
    } else {
      throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
    }
  }

  @SuppressWarnings("all")
  @Override
  public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
    return BlockEntityType.Builder.of(builder::apply, blocks).build(null);
  }

  @Override
  public DecoratedBedItem getItem() {
    return new DecoratedBedForgeItem();
  }

  @Override
  public Holder<PoiType> getPoiType(ResourceKey<PoiType> key) {
    return ForgeRegistries.POI_TYPES.getHolder(key).orElse(null);
  }

  private static class Provider<T> implements RegistryProvider<T> {
    private final String modId;
    private final DeferredRegister<T> registry;

    private final Set<RegistryObject<T>> entries = new HashSet<>();
    private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

    private Provider(String modId, DeferredRegister<T> registry) {
      this.modId = modId;
      this.registry = registry;
    }

    @Override
    public String getModId() {
      return modId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
      final net.minecraftforge.registries.RegistryObject<I> obj = registry.register(name, supplier);
      final RegistryObject<I> ro = new RegistryObject<>() {

        @Override
        public ResourceKey<I> getResourceKey() {
          return obj.getKey();
        }

        @Override
        public ResourceLocation getId() {
          return obj.getId();
        }

        @Override
        public I get() {
          return obj.get();
        }

        @Override
        public Holder<I> asHolder() {
          return obj.getHolder().orElseThrow();
        }
      };
      entries.add((RegistryObject<T>) ro);
      return ro;
    }

    @Override
    public Set<RegistryObject<T>> getEntries() {
      return entriesView;
    }
  }
}
