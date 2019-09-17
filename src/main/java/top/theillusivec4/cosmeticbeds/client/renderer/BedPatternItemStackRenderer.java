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

package top.theillusivec4.cosmeticbeds.client.renderer;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedTileEntity;

public class BedPatternItemStackRenderer extends ItemStackTileEntityRenderer {

  private final CosmeticBedTileEntity bed = new CosmeticBedTileEntity();

  @Override
  public void renderByItem(@Nonnull ItemStack itemStackIn) {
    this.bed.loadFromItemStack(itemStackIn);
    TileEntityRendererDispatcher.instance.renderAsItem(this.bed);
  }
}
