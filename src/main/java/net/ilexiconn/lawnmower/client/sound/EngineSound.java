package net.ilexiconn.lawnmower.client.sound;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.api.LawnmowerAPI;
import net.ilexiconn.lawnmower.client.ClientProxy;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EngineSound extends MovingSound {
    private EntityPlayer player;
    private float volumeGoal;

    public EngineSound(EntityPlayer player) {
        super(ClientProxy.ENGINE, SoundCategory.AMBIENT);
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.000001F;
        this.volumeGoal = 0.000001F;
    }

    @Override
    public void update() {
        if (this.player.isDead) {
            this.donePlaying = true;
            return;
        }

        this.volume = ClientUtils.interpolate(this.volume, this.volumeGoal, 0.5F);

        boolean flag = false;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = this.player.getHeldItem(hand);
            flag = flag || (stack != null && stack.getItem() == Lawnmower.LAWNMOWER);
        }

        if (!flag) {
            this.donePlaying = true;
            return;
        }

        this.xPosF = (float) this.player.posX;
        this.yPosF = (float) this.player.posY;
        this.zPosF = (float) this.player.posZ;
        double motionX = this.player.posX - this.player.prevPosX;
        double motionZ = this.player.posZ - this.player.prevPosZ;
        float speed = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        if (LawnmowerAPI.INSTANCE.isLawn(this.player.worldObj, this.player.getPosition().down()) && (double) speed >= 0.01D && this.player.onGround) {
            this.volumeGoal = 0.000001F + MathHelper.clamp_float(speed, 0.0F, 0.5F) * 0.7F;
        } else {
            this.volumeGoal = 0.000001F;
        }
    }
}
