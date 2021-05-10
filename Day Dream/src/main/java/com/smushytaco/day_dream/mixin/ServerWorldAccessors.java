package com.smushytaco.day_dream.mixin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;
@Mixin(ServerWorld.class)
public interface ServerWorldAccessors {
    @Accessor
    List<ServerPlayerEntity> getPlayers();
    @Accessor
    boolean getAllPlayersSleeping();
    @Accessor
    void setAllPlayersSleeping(boolean value);
    @Invoker("wakeSleepingPlayers")
    void wakeSleepingPlayers();
    @Invoker("resetWeather")
    void resetWeather();
}