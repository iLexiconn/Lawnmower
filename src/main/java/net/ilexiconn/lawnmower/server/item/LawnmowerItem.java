package net.ilexiconn.lawnmower.server.item;

import net.ilexiconn.lawnmower.Lawnmower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class LawnmowerItem extends Item implements net.ilexiconn.lawnmower.api.Lawnmower {
    public static final EnumDyeColor[] COLORS = EnumDyeColor.values();

    public LawnmowerItem() {
        this.setRegistryName(new ResourceLocation("lawnmower", "lawnmower"));
        this.setUnlocalizedName("lawnmower");
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < LawnmowerItem.COLORS.length; i++) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Color", i);
            ItemStack stack = new ItemStack(Lawnmower.LAWNMOWER, 1, 0);
            stack.setTagCompound(compound);
            list.add(stack);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected) {
            this.onUpdateLawnmower(world, entity, stack);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return /*I18n.translateToLocal("item.fireworksCharge." + LawnmowerItem.COLORS[stack.getTagCompound().getInteger("Color")].getUnlocalizedName()) + " " +*/ I18n.translateToLocal("item.lawnmower.name");
    }

    @Override
    public boolean damageEntities() {
        return true;
    }
}
