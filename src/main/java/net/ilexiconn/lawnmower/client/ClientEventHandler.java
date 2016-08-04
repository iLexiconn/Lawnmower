package net.ilexiconn.lawnmower.client;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onSetRotationAngles(PlayerModelEvent.SetRotationAngles event) {
        if (event.getEntityPlayer() == this.mc.thePlayer && this.mc.thePlayer.getHeldItemMainhand() != null && this.mc.thePlayer.getHeldItemMainhand().getItem() == Lawnmower.LAWNMOWER) {
            boolean left = true, right = true;
            if (this.mc.thePlayer.getHeldItemOffhand() != null) {
                EnumHandSide main = this.mc.gameSettings.mainHand;
                if (main == EnumHandSide.RIGHT) {
                    left = false;
                } else {
                    right = false;
                }
            }
            if (right) {
                event.getModel().bipedRightArm.rotateAngleX = -0.5F;
                event.getModel().bipedRightArm.rotateAngleY = 0.0F;
                event.getModel().bipedRightArm.rotateAngleZ = 0.0F;
            }
            if (left) {
                event.getModel().bipedLeftArm.rotateAngleX = -0.5F;
                event.getModel().bipedLeftArm.rotateAngleY = 0.0F;
                event.getModel().bipedLeftArm.rotateAngleZ = 0.0F;
            }
        }
    }
}
