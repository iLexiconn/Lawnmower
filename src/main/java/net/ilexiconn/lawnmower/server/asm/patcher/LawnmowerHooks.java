package net.ilexiconn.lawnmower.server.asm.patcher;

import net.ilexiconn.lawnmower.Lawnmower;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LawnmowerHooks {
    public static void onRandomTick(Block block, World world, BlockPos pos, IBlockState state, Random random) {
        block.randomTick(world, pos, state, random);
        if (!world.isRemote && block == Blocks.GRASS && Lawnmower.CONFIG.growWeeds) {
            if (world.isAirBlock(pos.up()) && world.getLightFromNeighbors(pos.up()) >= 4) {
                if (random.nextInt(15) == 14) {
                    world.setBlockState(pos.up(), Lawnmower.GRASS.getDefaultState());
                }
            }
        }
    }
}
