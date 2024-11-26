package com.smushytaco.day_dream
import io.wispforest.owo.config.annotation.*
@Modmenu(modId = DayDream.MOD_ID)
@Config(name = DayDream.MOD_ID, wrapperName = "ModConfig")
@Suppress("UNUSED")
class ModConfiguration {
    @JvmField
    var canSleepDuringTheDay = true
}