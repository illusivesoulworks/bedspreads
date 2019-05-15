package top.theillusivec4.cosmeticbeds.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import top.theillusivec4.cosmeticbeds.common.TileEntityCosmeticBed;

import javax.annotation.Nonnull;

public class BedPatternItemStackRenderer extends TileEntityItemStackRenderer {

    private final TileEntityCosmeticBed bed = new TileEntityCosmeticBed();

    @Override
    public void renderByItem(@Nonnull ItemStack itemStackIn) {
        this.bed.loadFromItemStack(itemStackIn);
        TileEntityRendererDispatcher.instance.renderAsItem(this.bed);
    }
}
