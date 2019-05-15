package top.theillusivec4.cosmeticbeds;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.cosmeticbeds.client.renderer.TileEntityCosmeticBedRenderer;
import top.theillusivec4.cosmeticbeds.common.BlockCosmeticBed;
import top.theillusivec4.cosmeticbeds.common.CosmeticBedsRegistry;
import top.theillusivec4.cosmeticbeds.common.ItemCosmeticBed;
import top.theillusivec4.cosmeticbeds.common.TileEntityCosmeticBed;
import top.theillusivec4.cosmeticbeds.common.recipe.BedAddPatternRecipe;
import top.theillusivec4.cosmeticbeds.common.recipe.BedRemovePatternRecipe;

@Mod(CosmeticBeds.MODID)
public class CosmeticBeds {

    public static final String MODID = "cosmeticbeds";

    public CosmeticBeds() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
    }

    private void setup(final FMLCommonSetupEvent evt) {
        RecipeSerializers.register(BedAddPatternRecipe.CRAFTING_ADD_PATTERN);
        RecipeSerializers.register(BedRemovePatternRecipe.CRAFTING_REMOVE_PATTERN);
    }

    private void clientSetup(final FMLClientSetupEvent evt) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCosmeticBed.class, new TileEntityCosmeticBedRenderer());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> evt) {
            evt.getRegistry().register(new BlockCosmeticBed());
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> evt) {
            evt.getRegistry().register(new ItemCosmeticBed());
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> evt) {
            evt.getRegistry().register(CosmeticBedsRegistry.COSMETIC_BED_TE);
        }
    }
}
