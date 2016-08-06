package net.ilexiconn.lawnmower.server.recipe;

import net.ilexiconn.lawnmower.server.item.LawnmowerItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LawnmowerRecipe implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        ItemStack lawnmower = null;
        List<ItemStack> dyes = new ArrayList<>();

        for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                if (stack.getItem() instanceof LawnmowerItem) {
                    if (lawnmower != null) {
                        return false;
                    }
                    lawnmower = stack;
                } else {
                    if (stack.getItem() != Items.DYE) {
                        return false;
                    }
                    dyes.add(stack);
                }
            }
        }

        return lawnmower != null && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack result = null;
        int[] rgb = new int[3];
        int color = 0;
        int itemCount = 0;
        LawnmowerItem lawnmower = null;

        for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);

            if (stack != null) {
                if (stack.getItem() instanceof LawnmowerItem) {
                    lawnmower = (LawnmowerItem) stack.getItem();

                    if (result != null) {
                        return null;
                    }

                    result = stack.copy();
                    result.stackSize = 1;

                    if (stack.getTagCompound() != null) {
                        int currentColor = stack.getTagCompound().getInteger("Color");
                        float r = (float) (currentColor >> 16 & 255) / 255.0F;
                        float g = (float) (currentColor >> 8 & 255) / 255.0F;
                        float b = (float) (currentColor & 255) / 255.0F;
                        color = (int) ((float) color + Math.max(r, Math.max(g, b)) * 255.0F);
                        rgb[0] = (int) ((float) rgb[0] + r * 255.0F);
                        rgb[1] = (int) ((float) rgb[1] + g * 255.0F);
                        rgb[2] = (int) ((float) rgb[2] + b * 255.0F);
                        itemCount++;
                    }
                } else {
                    if (stack.getItem() != Items.DYE) {
                        return null;
                    }

                    float[] colorRGB = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(stack.getMetadata()));
                    int r = (int) (colorRGB[0] * 255.0F);
                    int g = (int) (colorRGB[1] * 255.0F);
                    int b = (int) (colorRGB[2] * 255.0F);
                    color += Math.max(r, Math.max(g, b));
                    rgb[0] += r;
                    rgb[1] += g;
                    rgb[2] += b;
                    itemCount++;
                }
            }
        }

        if (lawnmower == null) {
            return null;
        } else {
            int r = rgb[0] / itemCount;
            int g = rgb[1] / itemCount;
            int b = rgb[2] / itemCount;
            float amount = (float) color / (float) itemCount;
            float wat = (float) Math.max(r, Math.max(g, b));
            r = (int) ((float) r * amount / wat);
            g = (int) ((float) g * amount / wat);
            b = (int) ((float) b * amount / wat);
            int colorResult = (r << 8) + g;
            colorResult = (colorResult << 8) + b;
            NBTTagCompound compound = result.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
                result.setTagCompound(compound);
            }
            compound.setInteger("Color", colorResult);
            return result;
        }
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventory) {
        ItemStack[] aitemstack = new ItemStack[inventory.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}