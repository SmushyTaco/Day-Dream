package com.smushytaco.day_dream.mixin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;
@Mixin(ServerWorld.class)
public interface ServerWorldAccessors {
    @Accessor
    List<ServerPlayerEntity> getPlayers();
    @Accessor
    SleepManager getSleepManager();
    @Invoker("wakeSleepingPlayers")
    void wakeSleepingPlayers();
    @Invoker("resetWeather")
    void resetWeather();
}