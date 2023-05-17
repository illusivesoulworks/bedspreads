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
import com.illusivesoulworks.bedspreads.platform.services.IPlatformRegistry;
import com.illusivesoulworks.bedspreads.registry.RegistryObject;
import com.illusivesoulworks.bedspreads.registry.RegistryProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class QuiltPlatformRegistry implements IPlatformRegistry {

  @Override
  public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey,
                                        String modId) {
    return new Provider<T>(modId, resourceKey);
  }

  @Override
  public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(
      BiFunction<BlockPos, BlockState, T> builder, Block... blocks) {
    return FabricBlockEntityTypeBuilder.create(builder::apply, blocks).build(null);
  }

  @Override
  public DecoratedBedItem getItem() {
    return new DecoratedBedItem();
  }

  @Override
  public Holder<PoiType> getPoiType(ResourceKey<PoiType> key) {
    return BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(key);
  }

  private static class Provider<T> implements RegistryProvider<T> {
    private final String modId;
    private final Registry<T> registry;

    private final Set<RegistryObject<T>> entries = new HashSet<>();
    private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

    @SuppressWarnings({"unchecked"})
    private Provider(String modId, ResourceKey<? extends Registry<T>> key) {
      this.modId = modId;
      final Registry<?> reg = BuiltInRegistries.REGISTRY.get(key.location());

      if (reg == null) {
        throw new RuntimeException("Registry with name " + key.location() + " was not found!");
      }
      registry = (Registry<T>) reg;
    }

    private Provider(String modId, Registry<T> registry) {
      this.modId = modId;
      this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
      final ResourceLocation rl = new ResourceLocation(modId, name);
      final I obj = Registry.register(registry, rl, supplier.get());
      final RegistryObject<I> ro = new RegistryObject<I>() {
        final ResourceKey<I> key =
            ResourceKey.create((ResourceKey<? extends Registry<I>>) registry.key(), rl);

        @Override
        public ResourceKey<I> getResourceKey() {
          return key;
        }

        @Override
        public ResourceLocation getId() {
          return rl;
        }

        @Override
        public I get() {
          return obj;
        }

        @Override
        public Holder<I> asHolder() {
          return (Holder<I>) registry.get((ResourceKey<T>) this.key);
        }
      };
      entries.add((RegistryObject<T>) ro);
      return ro;
    }

    @Override
    public Collection<RegistryObject<T>> getEntries() {
      return entriesView;
    }

    @Override
    public String getModId() {
      return modId;
    }
  }
}
