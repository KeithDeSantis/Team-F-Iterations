package edu.wpi.fuchsiafalcons.pathfinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Vertex {
    private final String ID;
    private final List<Edge> edges;
    private final double x, y;

    private final String floor;

    /**
     * Creates a new Vertex
     * @param ID the given ID String for the node
     * @author Tony Vuolo
     */
    public Vertex(String ID, int x, int y, String floor) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.edges = new LinkedList<>();

        this.floor = floor;
    }


    public String getFloor() {
        return floor;
    }

    /**
     * Gets the ID number of this Vertex
     * @return this.ID
     * @author Tony Vuolo
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Adds an Edge to this Vertex
     * @param edge the additive Edge
     * @author Tony Vuolo
     */
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    /**
     * Gets the List of Edges incident to this Vertex
     * @return this.edges
     * @author Tony Vuolo
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Returns the coordinates of this Vertex
     * @return {this.x, this.y}
     * @author Tony Vuolo
     */
    public double[] getCoordinates() {
        return new double[]{this.x, this.y};
    }

    /**
     * Finds the other Vertex incident to a specific Edge
     * @param edge the target Edge
     * @return the opposite Vertex incident to this Edge
     * @author Tony Vuolo
     */
    public Vertex getNeighbor(Edge edge) {
        Vertex[] endpoints = edge.getVertices();
        return endpoints[equals(endpoints[1]) ? 0 : 1];
    }

    /**
     * Finds the Euclidean Distance between two Vertices.
     * @param vertex the comparator Vertex
     * @return the sum of the squared distances between the x- and y-coordinates of the two Vertices
     * @author Tony Vuolo
     */
    public double EuclideanDistance(Vertex vertex) {
        double[] coordinates = vertex.getCoordinates();
        return Math.sqrt(Math.pow(coordinates[0] - this.x, 2) + Math.pow(coordinates[1] - this.y, 2));
    }

    /**
     * Determines whether two vertices are equal
     * @param vertex the comparator Vertex
     * @return true if the Vertices share the same ID, else false
     */
    public boolean equals(Vertex vertex) {
        return vertex != null && this.ID.equals(vertex.getID());
    }

    /**
     * Finds the heuristic estimate of the distance from this Vertex to another Vertex
     * @param vertex the endpoint goal Vertex
     * @return the heuristic weight of the "distance" from this Vertex to the endpoint Vertex
     * @author Tony Vuolo
     */
    public double heuristic(Vertex vertex) {
        return EuclideanDistance(vertex); //just the Euclidean distance for now
    }

    /**
     * Converts this Vertex to a printable format
     * @return this.ID
     */
    public String toString() {
        return this.ID;
    }


    /**
     * Used to get the x-coordinate of the vertex
     * @return the x-coordinate of this vertex.
     * @author Alex Friedman (ahf)
     */
    public double getX() {
        return x;
    }

    /**
     * Used to get the y-coordinate of the vertex
     * @return the y-coordinate of this vertex.
     * @author Alex Friedman (ahf)
     */
    public double getY() {
        return y;
    }
}
