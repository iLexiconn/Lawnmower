package net.ilexiconn.lawnmower.client;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.api.Lawn;
import net.ilexiconn.lawnmower.api.LawnType;
import net.ilexiconn.lawnmower.server.ServerProxy;
import net.ilexiconn.lawnmower.server.item.LawnmowerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    @Override
    public void onPreInit() {
        for (int i = 0; i < LawnmowerItem.COLORS.length; i++) {
            ModelLoader.setCustomModelResourceLocation(Lawnmower.LAWNMOWER, i, new ModelResourceLocation("lawnmower:lawnmower_" + LawnmowerItem.COLORS[i].getName(), "inventory"));
        }
    }

    @Override
    public void onInit() {
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
                red -= 18;
                green -= 18;
                blue -= 18;
            }
            return ((blue & 0xFF) << 16) | ((green & 0xFF) << 8) | (red & 0xFF);
        }, Lawnmower.LAWN);
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            LawnType type = state.getValue(Lawn.TYPE);
            return type == LawnType.LIGHT ? 0xFFFFFF : 0xDDDDDD;
        }, Lawnmower.ZEN_SAND);
    }
}
