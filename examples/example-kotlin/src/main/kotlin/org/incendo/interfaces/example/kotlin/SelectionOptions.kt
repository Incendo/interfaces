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
            5 to 0,
            6 to 0,
            6 to 1,
            6 to 2,
            6 to 3,
            6 to 4,
        )),
    TWO(
        2,
        Material.GOLD_BLOCK,
        arrayOf(5 to 0, 6 to 0, 4 to 1, 7 to 1, 6 to 2, 5 to 3, 4 to 4, 5 to 4, 6 to 4, 7 to 4)),
    THREE(
        3,
        Material.DIAMOND_BLOCK,
        arrayOf(
            4 to 0,
            5 to 0,
            6 to 0,
            7 to 0,
            7 to 1,
            4 to 2,
            5 to 2,
            6 to 2,
            7 to 2,
            7 to 3,
            4 to 4,
            5 to 4,
            6 to 4,
            7 to 4))
}
