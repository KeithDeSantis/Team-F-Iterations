package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.Arrays;

public class Permutation {
    private final int[] indices;
    private final int[] permutations;
    private boolean hasCycled;

    /**
     * Creates a new Permutation
     * @param length the length of the permutation
     * @author Tony Vuolo (bdane)
     */
    public Permutation(int length) {
        this.indices = new int[length];
        for(int i = 0; i < length; i++) {
            this.indices[i] = i;
        }
        this.permutations = new int[length];
        this.hasCycled = true;
    }

    /**
     * Returns the arrangement of these Indices
     * @return this.indices
     * @author Tony Vuolo (bdane)
     */
    public int[] getPermutation() {
        return this.indices;
    }

    /**
     * Determines whether this Permutation is in the starting orientation
     * @return this.hasCycled
     * @author Tony Vuolo
     */
    public boolean hasCycled() {
        return this.hasCycled;
    }

    /**
     * Makes the next permutation
     * @author Tony Vuolo
     */
    public void makeNextPermutation() {
        boolean isReverseSorted = true;
        for(int i = 0; i < this.permutations.length; i++) {
            if (this.permutations[i] != i) {
                isReverseSorted = false;
                i = this.permutations.length;
            }
        }
        if(isReverseSorted) {
            for(int i = 0; i < this.indices.length; i++) {
                this.indices[i] = i;
            }
            Arrays.fill(this.permutations, 0);
            this.hasCycled = true;
        } else {
            int index = 1;
            while(this.permutations[index] == index) {
                index++;
            }
            this.permutations[index]++;
            int proxy = this.indices[0];
            this.indices[0] = this.indices[index];
            this.indices[index] = proxy;
            for(int i = 1; i < index; i++) {
                this.permutations[i] = 0;
            }
            this.hasCycled = false;
        }
    }

    /**
     * Converts this Permutation to a printable format
     * @return this Permutation as a String
     * @author Tony Vuolo
     */
    public String toString() {
        return toString(this.indices);
    }

    /**
     * Converts this Permutation to a printable format
     * @param num the array of numbers
     * @return this Permutation as a String
     * @author Tony Vuolo
     */
    public String toString(int[] num) {
        StringBuilder builder = new StringBuilder();
        for(int i : num) {
            builder.append(", ").append(i);
        }
        return "[" + (num.length > 0 ? builder.substring(2) : "") + "]";
    }
}
