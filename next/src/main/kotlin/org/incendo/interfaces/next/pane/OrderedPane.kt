package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element

public abstract class OrderedPane(
    internal val ordering: List<Int>,
) : Pane() {
    override fun get(
        row: Int,
        column: Int,
    ): Element? = super.get(orderedRow(row), column)

    override fun set(
        row: Int,
        column: Int,
        value: Element,
    ): Unit = super.set(orderedRow(row), column, value)

    override fun has(
        row: Int,
        column: Int,
    ): Boolean = super.has(orderedRow(row), column)

    private fun orderedRow(row: Int) = ordering[row]
}
