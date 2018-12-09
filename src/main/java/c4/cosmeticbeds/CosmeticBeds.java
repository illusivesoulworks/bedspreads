package c4.cosmeticbeds;

import c4.cosmeticbeds.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(   modid = CosmeticBeds.MODID,
        name = CosmeticBeds.NAME,
        version = CosmeticBeds.VERSION)
public class CosmeticBeds
{
    public static final String MODID = "cosmeticbeds";
    public static final String NAME = "Cosmetic Beds";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @SidedProxy(clientSide = "c4.cosmeticbeds.proxy.ClientProxy", serverSide = "c4.cosmeticbeds.proxy.ServerProxy")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        proxy.preInit(evt);
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        proxy.init(evt);
    }
}
