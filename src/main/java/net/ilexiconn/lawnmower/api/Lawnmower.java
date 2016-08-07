package net.ilexiconn.lawnmower.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Lawnmower {
    default boolean onUpdateLawnmower(World world, Entity entity) {
        if (world.isRemote) {
            return false;
        }
        BlockPos pos = entity.getPosition().down();
        if (LawnmowerAPI.INSTANCE.isLawn(world, pos) && this.damageEntities()) {
            for (Entity target : world.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(entity.posX - 1.0F, entity.posY - 1.0F, entity.posZ - 1.0F, entity.posX + 1.0F, entity.posY + 1.0F, entity.posZ + 1.0F))) {
                target.attackEntityFrom(new EntityDamageSource("lawnmower", entity), 6.0F);
            }
        }
        return LawnmowerAPI.INSTANCE.handleMow(this, world, pos, entity);
    }

    default boolean damageEntities() {
        return false;
    }
}
