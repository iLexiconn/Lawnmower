package net.ilexiconn.lawnmower.server.asm.patcher;

import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class LawnmowerRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        patchClass(WorldServer.class)
            .patchMethod("updateBlocks", void.class)
                .apply(Patch.REPLACE_NODE, at(At.METHOD, "func_180645_a", World.class, BlockPos.class, IBlockState.class, Random.class, void.class), method -> {
                    method.method(INVOKESTATIC, LawnmowerHooks.class, "onRandomTick", Block.class, World.class, BlockPos.class, IBlockState.class, Random.class, void.class);
                }).pop();
    }
}
