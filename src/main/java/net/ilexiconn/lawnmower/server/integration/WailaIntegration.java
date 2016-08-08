package net.ilexiconn.lawnmower.server.integration;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.ilexiconn.lawnmower.Lawnmower;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.List;

public class WailaIntegration implements Integration {
    @Override
    public void onInit() {
        FMLInterModComms.sendMessage("Waila", "register", "net.ilexiconn.lawnmower.server.integration.WailaIntegration.register");
    }

    @Override
    public String getID() {
        return "Waila";
    }

    @Override
    public String getName() {
        return "Waila";
    }

    @Optional.Method(modid = "Waila")
    public static void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new LawnmowerDataProvider(), Block.class);
    }

    public static class LawnmowerDataProvider implements IWailaDataProvider {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return accessor.getStack();
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            if (accessor.getBlock() == Lawnmower.LAWN) {
                int chance = 0;
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (accessor.getWorld().getBlockState(accessor.getPosition().offset(facing)).getBlock() == Blocks.GRASS) {
                        chance++;
                    }
                }
                chance *= accessor.getWorld().isRaining() ? 22 : 10 * 100 / 75;
                currenttip.add((chance >= 75 ? TextFormatting.RED.toString() : chance >= 50 ? TextFormatting.YELLOW.toString() : TextFormatting.GREEN.toString()) + chance + "% chance of growing back");
            } else if (accessor.getBlock() == Lawnmower.ZEN_SAND) {
                currenttip.add(TextFormatting.GREEN.toString() + "0% chance of growing back (it's sand...)");
            }

            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
            return tag;
        }
    }
}
