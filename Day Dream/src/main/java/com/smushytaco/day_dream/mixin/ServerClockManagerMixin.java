package com.smushytaco.day_dream.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.smushytaco.day_dream.DayDream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minecraft.world.clock.ServerClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ServerClockManager.class)
public abstract class ServerClockManagerMixin {
    @WrapOperation(method = "lambda$moveToTimeMarker$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/clock/ClockTimeMarker;resolveTimeToMoveTo(J)J"))
    private static long hookTick(ClockTimeMarker instance, long totalTicks, Operation<Long> original, @Local(name = "timeMarkerId") ResourceKey<ClockTimeMarker> timeMarkerId) {
        long result = original.call(instance, totalTicks);
        if (!DayDream.INSTANCE.getConfig().getCanSleepDuringTheDay() || !ClockTimeMarkers.WAKE_UP_FROM_SLEEP.equals(timeMarkerId)) return result;
        long remainder = Math.floorMod(totalTicks, 12000L);
        long next12000 = remainder == 0L ? totalTicks + 12000L : totalTicks + (12000L - remainder);
        return result - totalTicks > 12000L ? next12000 : result;
    }
}