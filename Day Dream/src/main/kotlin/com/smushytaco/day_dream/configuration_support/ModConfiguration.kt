package com.smushytaco.day_dream.configuration_support
import com.smushytaco.day_dream.DayDream
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
@Config(name = DayDream.MOD_ID)
class ModConfiguration: ConfigData {
    @Comment("Default value is yes. If set to yes you'll be able to sleep during the day to make it night. If set to no you won't be able to.")
    val canSleepDuringTheDay = true
}