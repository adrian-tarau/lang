package net.microfalx.lang;

import java.util.Comparator;

/**
 * An interface for an object which can be sorted by its priority.
 */
public interface Prioritizable {

    int HIGH = Integer.MIN_VALUE;
    int DEFAULT = 0;
    int LOW = Integer.MAX_VALUE;

    /**
     * Returns the priority.
     *
     * @return the priority
     */
    default int getPriority() {
        return DEFAULT;
    }

    Comparator<Prioritizable> COMPARATOR = new Comparator<Prioritizable>() {
        public int compare(Prioritizable p1, Prioritizable p2) {
            return Integer.compare(p1.getPriority(), p2.getPriority());
        }
    };

}
