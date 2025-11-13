execute store result score $airship_civ_config.play_dead_manager.state airship_civ_config.global run data get entity @w PlayDeadManager.State
execute at @w as @w[y=-55,dy=1] run function airship_civ_configure:witherstorm/teleport_above_void
execute if score $airship_civ_config.play_dead_manager.state airship_civ_config.global matches 1 at @w run fill ~-50 -48 ~-50 ~50 -48 ~50 minecraft:barrier keep
execute if score $airship_civ_config.play_dead_manager.state airship_civ_config.global matches 3 at @w run fill ~-60 -48 ~-60 ~60 -48 ~60 minecraft:air replace minecraft:barrier