package net.ilexiconn.lawnmower;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.ilexiconn.lawnmower.api.LawnmowerAPI;
import net.ilexiconn.lawnmower.server.ServerProxy;
import net.ilexiconn.lawnmower.server.block.GrassBlock;
import net.ilexiconn.lawnmower.server.block.LawnBlock;
import net.ilexiconn.lawnmower.server.block.ZenSandBlock;
import net.ilexiconn.lawnmower.server.entity.LawnmowerEntity;
import net.ilexiconn.lawnmower.server.integration.Integration;
import net.ilexiconn.lawnmower.server.integration.TOPIntegration;
import net.ilexiconn.lawnmower.server.integration.WailaIntegration;
import net.ilexiconn.lawnmower.server.item.LawnmowerItem;
import net.ilexiconn.lawnmower.server.recipe.LawnmowerRecipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = "lawnmower", name = "Lawnmower", version = Lawnmower.VERSION, dependencies = "required-after:llibrary@[" + Lawnmower.LLIBRARY_VERSION + ",)")
public class Lawnmower {
    public static final String VERSION = "1.0.0";
    public static final String LLIBRARY_VERSION = "1.5.1";
    public static final String TOP_VERSION = "1.0.15-28";
    public static final String WAILA_VERSION = "1.7.0-B3";

    @SidedProxy(serverSide = "net.ilexiconn.lawnmower.server.ServerProxy", clientSide = "net.ilexiconn.lawnmower.client.ClientProxy")
    public static ServerProxy PROXY;
    public static Logger LOGGER = LogManager.getLogger("Lawnmower");

    public static final LawnBlock LAWN = new LawnBlock();
    public static final ZenSandBlock ZEN_SAND = new ZenSandBlock();
    public static final Item LAWNMOWER = new LawnmowerItem();
    public static final Block GRASS = new GrassBlock();

    public static final List<Integration> INTEGRATIONS = new ArrayList<>();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        Lawnmower.PROXY.onPreInit();

        GameRegistry.register(Lawnmower.LAWN);
        GameRegistry.register(Lawnmower.ZEN_SAND);
        GameRegistry.register(Lawnmower.LAWNMOWER);
        GameRegistry.register(Lawnmower.GRASS);

        GameRegistry.addRecipe(new ItemStack(Lawnmower.LAWNMOWER), " SI", "WWW", "BIB", 'S', new ItemStack(Blocks.STONE, 1, 0), 'I', Blocks.IRON_BARS, 'W', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 0), 'B', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15));
        GameRegistry.addRecipe(new LawnmowerRecipe());

        EntityRegistry.registerModEntity(LawnmowerEntity.class, "lawnmower", 0, this, 80, 1, true);

        LawnmowerAPI.INSTANCE.registerLawn(Blocks.GRASS, Lawnmower.LAWN);
        LawnmowerAPI.INSTANCE.registerLawn(Blocks.SAND, Lawnmower.ZEN_SAND);

        Lawnmower.INTEGRATIONS.add(new TOPIntegration());
        Lawnmower.INTEGRATIONS.add(new WailaIntegration());
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        Lawnmower.PROXY.onInit();
    }

    @Mod.EventHandler
    public void onIMC(FMLInterModComms.IMCEvent event) {
        event.getMessages().stream().filter(message -> message.key.equalsIgnoreCase("api")).forEach(message -> {
            Optional<Function<LawnmowerAPI, Void>> value = message.getFunctionValue(LawnmowerAPI.class, Void.class);
            if (value.isPresent()) {
                value.get().apply(LawnmowerAPI.INSTANCE);
            }
        });
    }
}