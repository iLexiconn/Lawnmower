package net.ilexiconn.lawnmower.client;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.client.sound.RustleSound;
import net.ilexiconn.lawnmower.server.entity.LawnmowerEntity;
import net.ilexiconn.lawnmower.server.message.EngineSoundMessage;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private Minecraft mc = Minecraft.getMinecraft();
    private ResourceLocation overlay = new ResourceLocation("lawnmower", "textures/gui/gorilla_overlay.png");
    private float overlayOpacity;
    private boolean hadLawnmower = false;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            boolean shouldDraw = this.mc.theWorld.getEntities(LawnmowerEntity.class, EntitySelectors.IS_ALIVE).isEmpty();
            this.overlayOpacity = ClientUtils.interpolate(this.overlayOpacity, shouldDraw ? 0.0F : 0.7F, shouldDraw ? 0.1F : 0.01F);
            if (overlayOpacity > 0.0F) {
                Minecraft mc = this.mc;
                mc.getTextureManager().bindTexture(this.overlay);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, this.overlayOpacity);
                ScaledResolution resolution = new ScaledResolution(mc);
                int x = resolution.getScaledWidth() - 130;
                int y = -20;
                int width = 310;
                int height = resolution.getScaledHeight() + 60;
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                vertexbuffer.pos(x, y + height, 0).tex(0, 1).endVertex();
                vertexbuffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
                vertexbuffer.pos(x + width, y, 0).tex(1, 0).endVertex();
                vertexbuffer.pos(x, y, 0).tex(0, 0).endVertex();
                tessellator.draw();
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LawnmowerEntity) {
            this.mc.getSoundHandler().playSound(new RustleSound((LawnmowerEntity) event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onSetRotationAngles(PlayerModelEvent.SetRotationAngles event) {
        EnumHandSide main = this.mc.gameSettings.mainHand;
        boolean rotateLeft = false;
        boolean rotateRight = false;
        boolean hasMainItem = false;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(hand);
            if (stack == null) {
                continue;
            }
            Item item = stack.getItem();
            if (hand == EnumHand.MAIN_HAND) {
                if (item == Lawnmower.LAWNMOWER) {
                    rotateLeft = rotateRight = true;
                } else {
                    hasMainItem = true;
                }
            } else if (hand == EnumHand.OFF_HAND) {
                if (item == Lawnmower.LAWNMOWER) {
                    if (main == EnumHandSide.LEFT) {
                        rotateRight = true;
                        if (!hasMainItem) {
                            rotateLeft = true;
                        }
                    } else if (main == EnumHandSide.RIGHT) {
                        rotateLeft = true;
                        if (!hasMainItem) {
                            rotateRight = true;
                        }
                    }
                } else {
                    if (main == EnumHandSide.LEFT) {
                        rotateRight = false;
                    } else if (main == EnumHandSide.RIGHT) {
                        rotateLeft = false;
                    }
                }
            }
        }

        if (rotateLeft) {
            event.getModel().bipedLeftArm.rotateAngleX = -0.5F;
            event.getModel().bipedLeftArm.rotateAngleY = 0.0F;
            event.getModel().bipedLeftArm.rotateAngleZ = 0.0F;
        }
        if (rotateRight) {
            event.getModel().bipedRightArm.rotateAngleX = -0.5F;
            event.getModel().bipedRightArm.rotateAngleY = 0.0F;
            event.getModel().bipedRightArm.rotateAngleZ = 0.0F;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }

        boolean flag = false;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = this.mc.thePlayer.getHeldItem(hand);
            flag = flag || (stack != null && stack.getItem() == Lawnmower.LAWNMOWER);
        }

        if (flag != this.hadLawnmower) {
            this.hadLawnmower = flag;
            if (flag) {
                Lawnmower.NETWORK_WRAPPER.sendToServer(new EngineSoundMessage(this.mc.thePlayer.getGameProfile().getId()));
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }

        boolean flag = false;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = this.mc.thePlayer.getHeldItem(hand);
            flag = flag || (stack != null && stack.getItem() == Lawnmower.LAWNMOWER);
        }

        event.setCanceled(flag && event.getButton() != -1);
    }
}
