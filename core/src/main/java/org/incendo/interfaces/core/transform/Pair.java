package org.incendo.interfaces.core.transform;

public class Pair<U, V> {

    private final U first;
    private final V second;

    /**
     * Creates a new pair consisting of two elements.
     *
     * @param first the first element
     * @param second the second element
     */
    public Pair(final U first, final V second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element in this pair.
     *
     * @return the first element
     */
    public U getFirst() {
        return this.first;
    }

    /**
     * Returns the second element in this pair.
     *
     * @return the second element
     */
    public V getSecond() {
        return this.second;
    }

}
