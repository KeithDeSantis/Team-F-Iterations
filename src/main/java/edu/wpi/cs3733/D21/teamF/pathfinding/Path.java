package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.*;

/**
 * This class is used to store the path between two Vertices. This is also
 * used pathfinding.
 *
 * @author Alex Friedman (ahf)
 */
public class Path implements Iterable<Vertex> {

    /**
     *  Used to track the vertices we have visited and the order we visited them in.
     *
     * Created by Alex Friedman (ahf)
     */
    private final DoublyLinkedHashSet<Vertex> vertices;

    /**
     * Used to store the weight of the path.
     * Created by Alex Friedman (ahf)
     */
    private double pathCost;

    /**
     * Constructs a path object.
     * @author Alex Friedman (ahf)
     */
    public Path()
    {
        this.vertices = new DoublyLinkedHashSet<>();
        this.pathCost = 0;
    }

    /**
     * Adds the given vertex to this path.
     * @param vertex The vertex to add to the path
     * @param pathWeight The cost of the path //FIXME: REMOVE
     * @return The new weight of the path, or -1 if there was an issue.
     * @author Alex Friedman (ahf)
     */
    public double addVertexToPath(Vertex vertex, double pathWeight)
    {
        //Adds to the LinkedHashSet
        if(this.vertices.add(vertex))
        {
            //We added the vertex to the path //TODO: Check that the path is valid
            return (this.pathCost += pathWeight);
        }

        return -1.0;
    }

    /**
     * Adds the given vertex to this path.
     * @param vertex The vertex to add to the path
     * @param pathWeight The cost of the path //FIXME: REMOVE
     * @return The new weight of the path, or -1 if there was an issue.
     * @author Alex Friedman (ahf)
     */
    public double insertVertexInPath(int index, Vertex vertex, double pathWeight)
    {
        //Adds to the LinkedHashSet
        if(this.vertices.insert(index, vertex))
        {
            //We added the vertex to the path //TODO: Check that the path is valid
            return (this.pathCost += pathWeight);
        }

        return -1.0;
    }

    /**
     * Removes the given vertex from this path.
     * @param vertex The vertex to remove from the path
     * @param pathWeight The cost of the path //FIXME: REMOVE
     * @return The new weight of the path, or -1 if there was an issue.
     * @author Alex Friedman (ahf)
     */
    public double removeVertexFromPath(Vertex vertex, double pathWeight)
    {
        //FIXME: MAKE SURE WE DO EVERYTHING IN ORDER

        if(this.vertices.remove(vertex))
        {
            return (this.pathCost -= pathWeight);
        }

        //Vertex was not in the path
        return -1.0;
    }

    /**
     * Gets the vertices in this path in order in a list.
     * @return The List of vertices traveled to.
     * @author Alex Friedman (ahf)
     */
    public List<Vertex> asList()
    {
        return new LinkedList<>(this.vertices.asList());
    }


    /**
     * Overrides the clone method so that it actually works.
     * @author Alex Friedman (ahf)
     */
    @Override
    public Path clone() {
        final Path duplicate = new Path();

        for(Vertex v : this.vertices)
            duplicate.addVertexToPath(v, 0.0);
        duplicate.pathCost = this.pathCost;

        return duplicate;
    }

    /**
     * Determines if a vertex is already in our path.
     * @param vertex The vertex to test.
     * @return true if the vertex is in the path, and false if it is not.
     * @author Alex Friedman (ahf)
     */
    public boolean contains(Vertex vertex) { return this.vertices.containsKey(vertex); }

    /**
     * Links two paths together
     * @param path the augend path
     * @author Tony Vuolo (bdane)
     */
    public void concatenate(Path path) {
        if(path != null) {
            if(path.vertices.getHead() != null && this.vertices.getTail() != null) {
                if(this.vertices.getTail().equals(path.vertices.getHead())) {
                    removeVertexFromPath(this.vertices.getTail(), 0);
                }
            }
            this.vertices.concatenate(path.vertices);
            this.pathCost += path.pathCost;
        }
    }


    /**
     * Gets the current cost of the path
     * @return The cost of the path.
     * @author Alex Friedman (ahf)
     */
    public double getPathCost() { return this.pathCost; }

    /**
     * Converts this path to a printable format
     * @return this Path as a String
     */
    @Override
    public String toString() {
        return "Path{" +
                "vertices=" + vertices +
                ", pathCost=" + pathCost +
                '}';
    }

    /**
     * Returns an iterator over elements in Path.
     * @return an Iterator.
     */
    @Override
    public Iterator<Vertex> iterator() {
        return this.vertices.iterator();
    }

    /**
     * Returns the first Vertex in this Path
     * @return the first Vertex in this.vertices
     * @author Tony Vuolo (bdane)
     */
    public Vertex getStart() {
        return this.vertices.getHead();
    }

    /**
     * Returns the last Vertex in this Path
     * @return ths last Vertex in this.vertices
     * @author Tony Vuolo (bdane)
     */
    public Vertex getEnd() {
        return this.vertices.getTail();
    }

    /**
     * Finds the length of this Path, equal to the number of vertices
     * @return the size of this.vertices
     */
    public int length() {
        return this.vertices.size();
    }

    /**
     * Gets the Vertex at a specified index in this Path
     * @param index the target index
     * @return the target Vertex
     */
    public Vertex get(int index) {
        return this.vertices.getIndex(index);
    }
}
