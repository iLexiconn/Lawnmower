package net.ilexiconn.lawnmower.server.block;

import net.ilexiconn.lawnmower.Lawnmower;
import net.ilexiconn.lawnmower.api.Lawn;
import net.ilexiconn.lawnmower.api.LawnType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class LawnBlock extends BlockGrass implements Lawn {
    public static final AxisAlignedBB LAWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    public LawnBlock() {
        this.setRegistryName(new ResourceLocation("lawnmower", "lawn"));
        this.setUnlocalizedName("lawn");
        this.setCreativeTab(null);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.6F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockGrass.SNOWY, false).withProperty(Lawn.TYPE, LawnType.LIGHT));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            if (world.getLight(pos.up()) < 4 && world.getBlockLightOpacity(pos.up()) > 2) {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            } else if (world.getLight(pos.up()) >= 9) {
                int chance = 0;
                for (int xOff = -1; xOff <= 1; xOff++) {
                    for (int yOff = -1; yOff <= 1; yOff++) {
                        if (xOff != yOff) {
                            if (world.getBlockState(pos.east(xOff).south(yOff)).getBlock() == Blocks.GRASS) {
                                chance++;
                            }
                        }
                    }
                }
                if (chance != 0 && random.nextInt(64 / chance) == 0) {
                    world.setBlockState(pos, Blocks.GRASS.getDefaultState());
                }
            }
        }
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return LawnBlock.LAWN_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockGrass.SNOWY, Lawn.TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(Lawn.TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(Lawn.TYPE, LawnType.values()[meta]);
    }

    @Override
    public IBlockState getBlockState(World world, BlockPos pos, Entity entity, IBlockState grass) {
        return Lawnmower.LAWN.getDefaultState().withProperty(Lawn.TYPE, LawnType.values()[pos.getX() % 2]);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Blocks.GRASS);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
