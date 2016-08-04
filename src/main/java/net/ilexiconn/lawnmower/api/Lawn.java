package net.ilexiconn.lawnmower.api;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Lawn {
    PropertyEnum<LawnType> TYPE = PropertyEnum.create("type", LawnType.class);

    IBlockState getBlockState(World world, BlockPos pos, Entity entity, IBlockState grass);
}
