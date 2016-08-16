package net.ilexiconn.lawnmower.server.item;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.server.entity.LawnmowerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class LawnmowerItem extends Item implements net.ilexiconn.lawnmower.api.Lawnmower {
    public LawnmowerItem() {
        this.setRegistryName(new ResourceLocation("lawnmower", "lawnmower"));
        this.setUnlocalizedName("lawnmower");
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxDamage(2048);
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        boolean flag = false;
        for (EnumHand hand : EnumHand.values()) {
            ItemStack held = ((EntityLivingBase) entity).getHeldItem(hand);
            flag = flag || (stack != null && ItemStack.areItemStacksEqual(held, stack));
        }

        if (flag && this.onUpdateLawnmower(world, entity)) {
            stack.damageItem(1, (EntityLivingBase) entity);
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return Lawnmower.CONFIG.flyingLawnmower;
    }

    @Override
    public Entity createEntity(World world, Entity entity, ItemStack stack) {
        Entity lawnmower = new LawnmowerEntity(world, entity.posX, entity.posY, entity.posZ, stack);
        lawnmower.motionX = entity.motionX;
        lawnmower.motionY = entity.motionY;
        lawnmower.motionZ = entity.motionZ;
        String thrower = ((EntityItem) entity).getThrower();
        if (thrower != null) {
            EntityPlayer player = world.getPlayerEntityByName(thrower);
            lawnmower.rotationYaw = player.getRotationYawHead();
        }
        return lawnmower;
    }

    @Override
    public boolean damageEntities() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        int color = Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, 0);
        tooltip.add(String.format("#%06X", (0xFFFFFF & color)));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}
