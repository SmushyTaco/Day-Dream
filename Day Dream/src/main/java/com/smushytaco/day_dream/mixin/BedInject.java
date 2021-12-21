package com.smushytaco.day_dream.mixin;
import com.smushytaco.day_dream.DayDream;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(BedBlock.class)
public abstract class BedInject {
    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;"), cancellable = true)
    private void hookOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!DayDream.INSTANCE.getConfig().getCanSleepDuringTheDay()) return;
        player.trySleep(pos).ifLeft((sleepFailureReason) -> {
            if (sleepFailureReason != null && sleepFailureReason != PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW) {
                player.sendMessage(sleepFailureReason.getMessage(), true);
            }
            if (sleepFailureReason == PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW) player.sleep(pos);
        });
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}