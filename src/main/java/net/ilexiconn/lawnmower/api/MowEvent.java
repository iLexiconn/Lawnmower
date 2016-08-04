package net.ilexiconn.lawnmower.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MowEvent extends Event {
    private Lawnmower lawnmower;
    private World world;
    private BlockPos pos;
    private Entity entity;
    private IBlockState lawn;
    private IBlockState grass;

    public MowEvent(Lawnmower lawnmower, World world, BlockPos pos, Entity entity, IBlockState lawn, IBlockState grass) {
        this.lawnmower = lawnmower;
        this.world = world;
        this.pos = pos;
        this.entity = entity;
        this.lawn = lawn;
        this.grass = grass;
    }

    public Lawnmower getLawnmower() {
        return lawnmower;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Entity getEntity() {
        return entity;
    }

    public IBlockState getLawn() {
        return lawn;
    }

    public void setLawn(IBlockState lawn) {
        this.lawn = lawn;
    }

    public IBlockState getGrass() {
        return grass;
    }
}
