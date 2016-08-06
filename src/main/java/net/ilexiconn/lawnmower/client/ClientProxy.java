package net.ilexiconn.lawnmower.client;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.api.Lawn;
import net.ilexiconn.lawnmower.api.LawnType;
import net.ilexiconn.lawnmower.client.render.LawnmowerRenderer;
import net.ilexiconn.lawnmower.server.ServerProxy;
import net.ilexiconn.lawnmower.server.entity.LawnmowerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final SoundEvent ENGINE = new SoundEvent(new ResourceLocation("lawnmower", "engine"));
    public static final SoundEvent RUSTLE = new SoundEvent(new ResourceLocation("lawnmower", "rustle"));

    @Override
    public void onPreInit() {
        super.onPreInit();

        GameRegistry.register(ClientProxy.ENGINE, new ResourceLocation("lawnmower", "engine"));
        GameRegistry.register(ClientProxy.RUSTLE, new ResourceLocation("lawnmower", "rustle"));

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(LawnmowerEntity.class, new LawnmowerRenderer.Factory());

        ModelLoader.setCustomModelResourceLocation(Lawnmower.LAWNMOWER, 0, new ModelResourceLocation("lawnmower:lawnmower", "inventory"));
    }

    @Override
    public void onInit() {
        super.onInit();

        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return ColorizerGrass.getGrassColor(0.5D, 1.0D);
            }
            int color = BiomeColorHelper.getGrassColorAtPos(world, pos);
            LawnType type = state.getValue(Lawn.TYPE);
            int red = (color) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = (color >> 16) & 0xFF;
            if (type == LawnType.LIGHT) {
                red += 18;
                green += 18;
                blue += 18;
            } else {
                red -= 22;
                green -= 22;
                blue -= 22;
            }
            return ((blue & 0xFF) << 16) | ((green & 0xFF) << 8) | (red & 0xFF);
        }, Lawnmower.LAWN);
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            LawnType type = state.getValue(Lawn.TYPE);
            return type == LawnType.LIGHT ? 0xFFFFFF : 0xDDDDDD;
        }, Lawnmower.ZEN_SAND);
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return ColorizerGrass.getGrassColor(0.5D, 1.0D);
            } else {
                return BiomeColorHelper.getGrassColorAtPos(world, pos);
            }
        }, Lawnmower.GRASS);

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        itemColors.registerItemColorHandler((stack, tintIndex) -> {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound == null || !compound.hasKey("Color")) {
                return 0xFFFFFF;
            }
            return compound.getInteger("Color");
        }, Lawnmower.LAWNMOWER);

        int amount = 100 - new Random().nextInt(100);
        ProgressManager.ProgressBar bar = ProgressManager.push("Lawnmower", amount);
        for (int i = 0; i < amount; i++) {
            bar.step("Rustling jimmies");
            try {
                Thread.sleep(2500 / amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ProgressManager.pop(bar);
    }
}
