package com.smushytaco.day_dream
import com.smushytaco.day_dream.mixin.ServerWorldAccessors
import com.smushytaco.day_dream.mixin.WorldAccessor
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.world.GameRules
object DayDream : ModInitializer {
    const val MOD_ID = "day_dream"
    private val config = ModConfig.createAndLoad()
    override fun onInitialize() {
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(EntitySleepEvents.AllowSleepTime { player, _, vanillaResult ->
            if (!config.canSleepDuringTheDay) return@AllowSleepTime ActionResult.PASS
            val serverWorld = player.world as ServerWorld
            serverWorld.updateSleepingPlayers()
            val sleepPercentage = serverWorld.gameRules.getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE)
            val sleepManager = (serverWorld as ServerWorldAccessors).sleepManager
            if (sleepManager.canSkipNight(sleepPercentage) && sleepManager.canResetTime(sleepPercentage, (serverWorld as ServerWorldAccessors).players)) {
                if (serverWorld.gameRules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                    val l = (serverWorld as WorldAccessor).properties.timeOfDay + 12000L
                    serverWorld.timeOfDay = l - l % 12000L
                }
                (serverWorld as ServerWorldAccessors).invokeWakeSleepingPlayers()
                if (serverWorld.gameRules.getBoolean(GameRules.DO_WEATHER_CYCLE)) (serverWorld as ServerWorldAccessors).invokeResetWeather()
            }
            return@AllowSleepTime if (vanillaResult == serverWorld.isNight) ActionResult.SUCCESS else ActionResult.PASS
        })
    }
}