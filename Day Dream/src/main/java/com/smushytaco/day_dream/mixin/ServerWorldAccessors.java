package com.smushytaco.day_dream.mixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;
@Mixin(ServerLevel.class)
public interface ServerWorldAccessors {
    @Accessor
    List<ServerPlayer> getPlayers();
    @Accessor
    SleepStatus getSleepStatus();
    @Invoker
    void invokeWakeUpAllPlayers();
    @Invoker
    void invokeResetWeatherCycle();
}