package com.smushytaco.day_dream
import com.smushytaco.day_dream.mixin.ServerWorldAccessors
import com.smushytaco.day_dream.mixin.WorldAccessor
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.GameRules
object DayDream : ModInitializer {
    const val MOD_ID = "day_dream"
    private val config = ModConfig.createAndLoad()
    override fun onInitialize() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(EntitySleepEvents.AllowSleepTime { player, _, vanillaResult ->
            if (!config.canSleepDuringTheDay) return@AllowSleepTime InteractionResult.PASS
            val serverWorld = player.level() as ServerLevel
            serverWorld.updateSleepingPlayerList()
            val sleepPercentage = serverWorld.gameRules.getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE)
            val sleepManager = (serverWorld as ServerWorldAccessors).sleepStatus
            if (sleepManager.areEnoughSleeping(sleepPercentage) && sleepManager.areEnoughDeepSleeping(sleepPercentage, (serverWorld as ServerWorldAccessors).players)) {
                if (serverWorld.gameRules.getBoolean(GameRules.RULE_DAYLIGHT)) {
                    val l = (serverWorld as WorldAccessor).levelData.dayTime + 12000L
                    serverWorld.dayTime = l - l % 12000L
                }
                (serverWorld as ServerWorldAccessors).invokeWakeUpAllPlayers()
                if (serverWorld.gameRules.getBoolean(GameRules.RULE_WEATHER_CYCLE)) (serverWorld as ServerWorldAccessors).invokeResetWeatherCycle()
            }
            return@AllowSleepTime if (vanillaResult == serverWorld.isDarkOutside) InteractionResult.SUCCESS else InteractionResult.PASS
        })
    }
}