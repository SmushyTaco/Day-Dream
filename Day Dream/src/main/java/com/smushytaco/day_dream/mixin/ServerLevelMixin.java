package com.smushytaco.day_dream.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.smushytaco.day_dream.DayDream;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "longValue=24000"))
    private long hookTick(long original) {
        return DayDream.INSTANCE.getConfig().getCanSleepDuringTheDay() ? original / 2 : original;
    }
}