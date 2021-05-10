package com.smushytaco.day_dream.mixin;
import com.smushytaco.day_dream.DayDream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityCanSleep {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
    private boolean hookTick(World world) {
        if (!DayDream.INSTANCE.getConfig().getCanSleepDuringTheDay()) return world.isDay();
        ServerWorld serverWorld = (ServerWorld) world;
        boolean isNight = serverWorld.isNight();
        serverWorld.updateSleepingPlayers();
        if (((ServerWorldAccessors) serverWorld).getAllPlayersSleeping() && ((ServerWorldAccessors) serverWorld).getPlayers().stream().noneMatch((player) -> !player.isSpectator() && !player.isSleepingLongEnough())) {
            ((ServerWorldAccessors) serverWorld).setAllPlayersSleeping(false);
            if (serverWorld.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                long l = ((WorldAccessor) world).getProperties().getTimeOfDay() + 12000L;
                serverWorld.setTimeOfDay(l - l % 12000L);
            }
            ((ServerWorldAccessors) serverWorld).wakeSleepingPlayers();
            if (serverWorld.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
                ((ServerWorldAccessors) serverWorld).resetWeather();
            }
        }
        return isNight != serverWorld.isNight();
    }
}