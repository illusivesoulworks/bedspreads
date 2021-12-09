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

package top.theillusivec4.bedspreads.loader.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockApplyCallback;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.core.Bedspreads;
import top.theillusivec4.bedspreads.core.BedspreadsRegistry;
import top.theillusivec4.bedspreads.loader.client.renderer.DecoratedBedBlockEntityRenderer;
import top.theillusivec4.bedspreads.loader.client.renderer.DecoratedBedItemRenderer;
import top.theillusivec4.bedspreads.loader.common.DecoratedBedBlockEntity;

public class BedspreadsClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientPickBlockApplyCallback.EVENT.register((player, result, stack) -> {

      if (result.getType() == Type.BLOCK) {
        World world = player.world;
        BlockEntity blockEntity = world.getBlockEntity(new BlockPos(result.getPos()));
        return blockEntity instanceof DecoratedBedBlockEntity
            ? ((DecoratedBedBlockEntity) blockEntity).getItem() : stack;
      }
      return stack;
    });
    ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        .register((spriteAtlasTexture, registry) -> {
          for (BannerPattern pattern : BannerPattern.values()) {
            registry.register(new Identifier(Bedspreads.MOD_ID, "entity/" + pattern.getName()));
          }
          registry.register(new Identifier(Bedspreads.MOD_ID, "entity/bed_base"));
        });
    BlockEntityRendererRegistry.register(BedspreadsRegistry.DECORATED_BED_BE,
        DecoratedBedBlockEntityRenderer::new);
    BuiltinItemRendererRegistry.INSTANCE
        .register(BedspreadsRegistry.DECORATED_BED_ITEM, new DecoratedBedItemRenderer());
  }
}
