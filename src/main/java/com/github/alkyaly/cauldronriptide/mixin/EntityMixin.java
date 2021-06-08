package com.github.alkyaly.cauldronriptide.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public abstract World getEntityWorld();

    @Inject(method = "isTouchingWaterOrRain", at = @At("TAIL"), cancellable = true)
    private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(info.getReturnValueZ() || isEntityInsideCauldron());
    }

    @Unique
    private boolean isEntityInsideCauldron() {
        BlockState state = getEntityWorld().getBlockState(getBlockPos());
        return state.isOf(Blocks.WATER_CAULDRON) && state.get(LeveledCauldronBlock.LEVEL) > 0;
    }
}
