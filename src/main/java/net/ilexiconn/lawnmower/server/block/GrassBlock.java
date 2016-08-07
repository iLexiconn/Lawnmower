package net.ilexiconn.lawnmower.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class GrassBlock extends Block implements IPlantable {
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 3);

    public static final AxisAlignedBB LEVEL_0_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    public static final AxisAlignedBB LEVEL_1_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    public static final AxisAlignedBB LEVEL_2_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    public static final AxisAlignedBB LEVEL_3_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    public GrassBlock() {
        super(Material.PLANTS);
        this.setRegistryName(new ResourceLocation("lawnmower", "grass"));
        this.setUnlocalizedName("grass");
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(GrassBlock.STAGE, 0));
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GrassBlock.STAGE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(GrassBlock.STAGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(GrassBlock.STAGE, meta);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int level = state.getValue(GrassBlock.STAGE);
        if (level == 0) {
            return GrassBlock.LEVEL_0_AABB;
        } else if (level == 1) {
            return GrassBlock.LEVEL_1_AABB;
        } else if (level == 2) {
            return GrassBlock.LEVEL_2_AABB;
        }
        return LEVEL_3_AABB;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            if (random.nextInt(20) == 19 && world.getLightFromNeighbors(pos) >= 4) {
                int level = state.getValue(GrassBlock.STAGE);
                if (level < 3) {
                    world.setBlockState(pos, state.withProperty(GrassBlock.STAGE, level + 1));
                }
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if (!world.getBlockState(pos.down()).isFullBlock()) {
            world.destroyBlock(pos, false);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) {
            return this.getDefaultState();
        }
        return state;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }
}
