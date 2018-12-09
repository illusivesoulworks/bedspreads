package c4.cosmeticbeds.client.renderer;

import c4.cosmeticbeds.common.tileentity.TileEntityCosmeticBed;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BedPatternItemStackRenderer extends TileEntityItemStackRenderer {

    private final TileEntityCosmeticBed bed = new TileEntityCosmeticBed();

    @Override
    public void renderByItem(@Nonnull ItemStack itemStackIn) {
        this.bed.setItemValues(itemStackIn);
        TileEntityRendererDispatcher.instance.render(this.bed, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}
