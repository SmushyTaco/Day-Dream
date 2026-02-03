package com.smushytaco.day_dream.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.day_dream.DayDream;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(BedRule.Rule.class)
public abstract class BedRuleMixin {
    @WrapOperation(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDarkOutside()Z"))
    private boolean hookCanSleep(Level instance, Operation<Boolean> original) {
        return DayDream.INSTANCE.getConfig().getCanSleepDuringTheDay() || original.call(instance);
    }
}