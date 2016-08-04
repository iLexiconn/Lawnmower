package net.ilexiconn.lawnmower;

import net.ilexiconn.lawnmower.api.LawnmowerAPI;
import net.ilexiconn.lawnmower.server.ServerProxy;
import net.ilexiconn.lawnmower.server.block.LawnBlock;
import net.ilexiconn.lawnmower.server.block.ZenSandBlock;
import net.ilexiconn.lawnmower.server.item.LawnmowerItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "lawnmower", name = "Lawnmower", version = Lawnmower.VERSION, dependencies = "required-after:llibrary@[" + Lawnmower.LLIBRARY_VERSION + ",)")
public class Lawnmower {
    public static final String VERSION = "1.0.0";
    public static final String LLIBRARY_VERSION = "1.5.1";

    @SidedProxy(serverSide = "net.ilexiconn.lawnmower.server.ServerProxy", clientSide = "net.ilexiconn.lawnmower.client.ClientProxy")
    public static ServerProxy PROXY;

    public static final LawnBlock LAWN = new LawnBlock();
    public static final ZenSandBlock ZEN_SAND = new ZenSandBlock();
    public static final Item LAWNMOWER = new LawnmowerItem();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        Lawnmower.PROXY.onPreInit();

        GameRegistry.register(Lawnmower.LAWN);
        GameRegistry.register(Lawnmower.ZEN_SAND);
        GameRegistry.register(Lawnmower.LAWNMOWER);

        LawnmowerAPI.INSTANCE.registerLawn(Blocks.GRASS, Lawnmower.LAWN);
        LawnmowerAPI.INSTANCE.registerLawn(Blocks.SAND, Lawnmower.ZEN_SAND);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        Lawnmower.PROXY.onInit();
    }
}