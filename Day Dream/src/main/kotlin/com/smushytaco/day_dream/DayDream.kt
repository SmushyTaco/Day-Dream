package com.smushytaco.day_dream
import com.smushytaco.day_dream.configuration_support.ModConfiguration
import com.smushytaco.day_dream.mixin.ServerWorldAccessors
import com.smushytaco.day_dream.mixin.WorldAccessor
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.world.GameRules
object DayDream : ModInitializer {
    const val MOD_ID = "day_dream"
    lateinit var config: ModConfiguration
        private set
    override fun onInitialize() {
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
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
                (serverWorld as ServerWorldAccessors).wakeSleepingPlayers()
                if (serverWorld.gameRules.getBoolean(GameRules.DO_WEATHER_CYCLE)) (serverWorld as ServerWorldAccessors).resetWeather()
            }
            return@AllowSleepTime if (vanillaResult == serverWorld.isNight) ActionResult.SUCCESS else ActionResult.PASS
        })
    }
}