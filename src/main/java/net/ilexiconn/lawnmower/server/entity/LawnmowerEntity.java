package net.ilexiconn.lawnmower.server.entity;

import net.ilexiconn.lawnmower.api.Lawnmower;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class LawnmowerEntity extends EntityItem implements Lawnmower {
    public LawnmowerEntity(World world) {
        super(world);
        this.setPickupDelay(40);
        this.setSize(0.6F, 0.7F);
    }

    public LawnmowerEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
        this.setPickupDelay(40);
        this.setSize(0.6F, 0.7F);
    }

    @Override
    public void onUpdate() {
        this.onUpdateLawnmower(this.worldObj, this);
        this.motionY += 0.03999999910593033D;
        if (this.worldObj.getHeight(new BlockPos(this)).getY() + 4 > this.getPosition().getY()) {
            this.motionY += 0.01D;
        }
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)) * 0.2F;
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)) * 0.2F;
        if (this.ticksExisted % 20 == 0) {
            this.rotationYaw += ((float) (Math.random() * 60.0D) * (this.rand.nextBoolean() ? 1 : -1));
        }
        super.onUpdate();
    }

    @Override
    public void searchForOtherItemsNearby() {

    }
}
