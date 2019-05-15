package top.theillusivec4.cosmeticbeds.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.cosmeticbeds.CosmeticBeds;

@ObjectHolder(CosmeticBeds.MODID)
public class CosmeticBedsRegistry {

    @ObjectHolder("cosmetic_bed")
    public static final Block COSMETIC_BED_BLOCK = null;

    @ObjectHolder("cosmetic_bed")
    public static final Item COSMETIC_BED_ITEM = null;

    public static final TileEntityType<TileEntityCosmeticBed> COSMETIC_BED_TE;

    static {
        COSMETIC_BED_TE = TileEntityType.Builder.create(TileEntityCosmeticBed::new).build(null);
        COSMETIC_BED_TE.setRegistryName("cosmetic_bed");
    }
}
