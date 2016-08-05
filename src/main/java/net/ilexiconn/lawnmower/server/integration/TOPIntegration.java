package net.ilexiconn.lawnmower.server.integration;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.*;
import net.ilexiconn.lawnmower.Lawnmower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPIntegration implements Integration {
    @Override
    public void onInit() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "net.ilexiconn.lawnmower.server.integration.TOPIntegration$GetTheOneProbe");
    }

    @Override
    public String getID() {
        return "theoneprobe";
    }

    @Override
    public String getName() {
        return "TheOneProbe";
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            theOneProbe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return "lawnmower";
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState state, IProbeHitData data) {
                    if (state.getBlock() == Lawnmower.LAWN) {
                        int chance = 0;
                        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                            if (world.getBlockState(data.getPos().offset(facing)).getBlock() == Blocks.GRASS) {
                                chance++;
                            }
                        }
                        chance *= world.isRaining() ? 22 : 10 * 100 / 75;
                        probeInfo.text((chance >= 75 ? TextFormatting.RED.toString() : chance >= 50 ? TextFormatting.YELLOW.toString() : TextFormatting.GREEN.toString()) + chance + "% chance of growing back");
                    } else if (state.getBlock() == Lawnmower.ZEN_SAND) {
                        probeInfo.text(TextFormatting.GREEN.toString() + "0% chance of growing back (it's sand...)");
                    }
                }
            });
            return null;
        }
    }
}
