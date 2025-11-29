package com.smushytaco.day_dream.mixin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(Level.class)
public interface WorldAccessor {
    @Accessor
    WritableLevelData getLevelData();
}