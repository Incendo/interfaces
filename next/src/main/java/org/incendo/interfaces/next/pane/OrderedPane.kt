package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element

public abstract class OrderedPane(
    private val ordering: List<Int>
) : Pane() {

    override fun get(column: Int, row: Int): Element? {
        return super.get(column, orderedRow(row))
    }

    override fun set(column: Int, row: Int, value: Element) {
        return super.set(column, orderedRow(row), value)
    }

    override fun has(column: Int, row: Int): Boolean {
        return super.has(column, orderedRow(row))
    }

    private fun orderedRow(row: Int) = ordering[row]
}
