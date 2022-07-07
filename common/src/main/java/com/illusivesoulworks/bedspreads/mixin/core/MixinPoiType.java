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

package com.illusivesoulworks.bedspreads.mixin.core;

import com.illusivesoulworks.bedspreads.mixin.MixinHooks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoiType.class)
public class MixinPoiType {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("RETURN"), method = "is", cancellable = true)
  private void bedspreads$is(BlockState state, CallbackInfoReturnable<Boolean> cir) {

    if (!cir.getReturnValue()) {
      cir.setReturnValue(MixinHooks.containsDecoratedBed((PoiType) (Object) this, state));
    }
  }
}
