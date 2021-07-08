package org.incendo.interfaces.example.kotlin

import org.bukkit.Material

public enum class SelectionOptions(
    public val index: Int,
    public val material: Material,
    public val art: Array<Pair<Int, Int>>
) {
    ONE(
        1,
        Material.IRON_BLOCK,
        arrayOf(
            4 to 0,
            5 to 0,
            5 to 1,
            5 to 2,
            5 to 3,
            5 to 4,
        )),
    TWO(
        2,
        Material.GOLD_BLOCK,
        arrayOf(
            3 to 0,
            3 to 0,
            5 to 1,
            2 to 2,
            3 to 3,
            4 to 4,
        ))
}
