package net.ilexiconn.lawnmower.api;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import java.util.HashMap;
import java.util.Map;

public enum LawnmowerAPI {
    INSTANCE;

    private Map<Block, Lawn> lawnMap = new HashMap<>();

    public <T extends Block & Lawn> void registerLawn(Block grass, T lawn) {
        Preconditions.checkNotNull(grass, "Grass cannot be null!");
        Preconditions.checkNotNull(lawn, "Lawn cannot be null!");

        this.lawnMap.put(grass, lawn);
    }

    public boolean handleMow(Lawnmower lawnmower, World world, BlockPos pos, Entity entity) {
        IBlockState grass = world.getBlockState(pos);
        Block block = grass.getBlock();
        Lawn lawn = this.lawnMap.get(block);
        if (lawn == null) {
            return false;
        }

        if (entity instanceof EntityPlayer) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, grass, (EntityPlayer) entity);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }
        }

        IBlockState lawnState = lawn.getBlockState(world, pos, entity, grass);

        if (lawnState == null) {
            return false;
        }

        MowEvent mowEvent = new MowEvent(lawnmower, world, pos, entity, lawnState, grass);
        if (MinecraftForge.EVENT_BUS.post(mowEvent)) {
            return false;
        }

        lawnState = mowEvent.getLawn();
        world.setBlockState(pos, lawnState);

        pos = pos.up();
        if (world.getBlockState(pos).getBlock() instanceof IPlantable) {
            grass = world.getBlockState(pos);
            grass.getBlock().dropBlockAsItem(world, pos, grass, 0);
            world.setBlockToAir(pos);
        }

        return true;
    }
}
