package edu.wpi.cs3733.D21.teamF.pathfinding;

public class UnorderedPair {
    private final int min;
    private final int max;

    /**
     * Makes a new UnorderedPair
     * @param a value 1
     * @param b value 2
     * @author Tony Vuolo (bdane)
     */
    public UnorderedPair(int a, int b) {
        if(a > b) {
            this.max = a;
            this.min = b;
        } else {
            this.min = a;
            this.max = b;
        }
    }

    /**
     * Gets the hashcode
     * @return the unique int representing this UnorderedPair
     */
    @Override
    public int hashCode() {
        int sum = this.min + this.max;
        return sum * (sum + 1) - this.min;
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == hashCode() && o instanceof UnorderedPair;
    }
}
