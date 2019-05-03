package gg.rsmod.plugins.content.items.amuletofglory

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport

val GLORY = arrayOf(
        Items.AMULET_OF_GLORY1, Items.AMULET_OF_GLORY2, Items.AMULET_OF_GLORY3,
        Items.AMULET_OF_GLORY4, Items.AMULET_OF_GLORY5, Items.AMULET_OF_GLORY6
)

GLORY.forEach { glory ->
    on_equipment_option(glory, option = "Edgeville") {
        player.queue {
            teleport(Tile(x = 3086, z = 3500, height = 0)) //Coordinates of edgeville
        }
    }

    on_equipment_option(glory, option = "Karamja") {
        player.queue {
            teleport(Tile(x = 2917, z = 3176, height = 0)) //Coordinates of karamja
        }
    }

    on_equipment_option(glory, option = "Draynor Village") {
        player.queue {
            teleport(Tile(x = 3103, z = 3249, height = 0)) //Coordinates of draynor village
        }
    }

    on_equipment_option(glory, option = "Al kharid") {
        player.queue {
            teleport(Tile(x = 3292, z = 3165, height = 0)) //Coordinates of draynor village
        }
    }
}

suspend fun QueueTask.teleport(tile : Tile) {
    if (player.canTeleport(TeleportType.MODERN)) {
        world.spawn(AreaSound(tile, id = 200, radius = 10, volume = 1))
        player.teleport(tile, TeleportType.MODERN)
        player.equipment[EquipmentType.AMULET.id] = Item(set(player))
        player.message(message(player))
    }
}

fun set(player: Player): Int {
    return when {
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY6) -> Items.AMULET_OF_GLORY5
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> Items.AMULET_OF_GLORY4
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> Items.AMULET_OF_GLORY3
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> Items.AMULET_OF_GLORY2
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> Items.AMULET_OF_GLORY1
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> Items.AMULET_OF_GLORY
        else -> Items.AMULET_OF_GLORY
    }
}

fun message(player: Player): String {
    return when {
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> "<col=7f007f>Your amulet has five charges left.</col>"
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> "<col=7f007f>Your amulet has four charges left.</col>"
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> "<col=7f007f>Your amulet has three charges left.</col>"
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> "<col=7f007f>Your amulet has two charges left.</col>"
        player.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> "<col=7f007f>Your amulet has one charge left.</col>"
        else -> "<col=7f007f>Your amulet has no charges left.</col>"
    }
}

fun Pawn.teleport(tile : Tile, teleportType: TeleportType) {
    queue(TaskPriority.STRONG) {
        resetInteractions()
        clearHits()

        lock = LockState.FULL_WITH_DAMAGE_IMMUNITY

        animate(teleportType.animation)
        teleportType.graphic?.let {
            graphic(it)
        }

        wait(teleportType.teleportDelay)

        moveTo(tile)

        teleportType.endAnimation?.let {
            animate(it)
        }

        teleportType.endGraphic?.let {
            graphic(it)
        }

        teleportType.endAnimation?.let {
            val def = world.definitions.get(AnimDef::class.java, it)
            wait(def.cycleLength)
        }

        animate(-1)
        unlock()
    }
}
