package net.ilexiconn.lawnmower.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Lawnmower {
    default void onUpdateLawnmower(World world, Entity entity) {
        this.onUpdateLawnmower(world, entity, null);
    }

    default void onUpdateLawnmower(World world, Entity entity, ItemStack stack) {
        if (world.isRemote) {
            return;
        }
        BlockPos pos = entity.getPosition().down();
        if (LawnmowerAPI.INSTANCE.handleMow(this, world, pos, entity)) {
            if (this.damageEntities()) {
                for (Entity target : world.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(entity.posX - 1.0F, entity.posY - 1.0F, entity.posZ - 1.0F, entity.posX + 1.0F, entity.posY + 1.0F, entity.posZ + 1.0F))) {
                    target.attackEntityFrom(new EntityDamageSource("lawnmower", entity), 6.0F);
                }
            }
        }
    }

    default boolean damageEntities() {
        return false;
    }
}
