package net.ilexiconn.lawnmower.client.render;

import net.ilexiconn.lawnmower.server.entity.LawnmowerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LawnmowerRenderer extends RenderEntityItem {
    public LawnmowerRenderer(RenderManager manager) {
        super(manager, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public int transformModelCount(EntityItem itemIn, double x, double y, double z, float bob, IBakedModel model) {
        GlStateManager.translate((float) x, (float) y + 0.4F, (float) z);
        GlStateManager.rotate(180.0F - itemIn.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        return 1;
    }

    public static class Factory implements IRenderFactory<LawnmowerEntity> {
        @Override
        public Render<? super LawnmowerEntity> createRenderFor(RenderManager manager) {
            return new LawnmowerRenderer(manager);
        }
    }
}
