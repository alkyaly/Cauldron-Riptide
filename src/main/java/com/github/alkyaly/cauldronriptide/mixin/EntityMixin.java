package com.github.alkyaly.cauldronriptide.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public abstract boolean isTouchingWater();

    @Shadow protected abstract boolean isBeingRainedOn();

    @Shadow public abstract World getEntityWorld();

    @Inject(method = "isTouchingWaterOrRain", at = @At("HEAD"), cancellable = true)
    private void isTouchingWaterOrRain(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(isTouchingWater() || isBeingRainedOn() || isEntityInsideCauldron());
    }

    private boolean isEntityInsideCauldron() {
        BlockState state = getEntityWorld().getBlockState(getBlockPos());
        return state.isOf(Blocks.CAULDRON) && state.get(CauldronBlock.LEVEL) > 0; // ← Fix that when 1.17 deploys
    }
}
