package net.ilexiconn.lawnmower.server.block;

import net.ilexiconn.lawnmower.api.Lawn;
import net.ilexiconn.lawnmower.api.LawnType;
import net.minecraft.block.BlockSand;
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

public class ZenSandBlock extends BlockSand implements Lawn {
    public ZenSandBlock() {
        this.setRegistryName(new ResourceLocation("lawnmower", "zen_sand"));
        this.setUnlocalizedName("zen_sand");
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND).withProperty(Lawn.TYPE, LawnType.LIGHT));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockSand.VARIANT, Lawn.TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.values()[(meta >> 1) & 1]).withProperty(Lawn.TYPE, LawnType.values()[meta & 1]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(BlockSand.VARIANT).ordinal() & 1) << 1 | (state.getValue(Lawn.TYPE).ordinal() & 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return LawnBlock.LAWN_AABB;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Blocks.SAND, 1, world.getBlockState(pos).getValue(BlockSand.VARIANT).ordinal());
    }

    @Override
    public IBlockState getBlockState(World world, BlockPos pos, Entity entity, IBlockState grass) {
        return this.getDefaultState().withProperty(BlockSand.VARIANT, grass.getValue(BlockSand.VARIANT)).withProperty(Lawn.TYPE, LawnType.values()[Math.floorMod(pos.getX(), 2)]);
    }
}
