package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.HashMap;

public class Edge {
    private final Vertex a, b;
    private double weight;

    private final HashMap<String, Integer> floorLayerMap;

    /**
     * Creates a new Edge
     * @param a one endpoint Vertex
     * @param b the other endpoint Vertex
     * @author Tony Vuolo
     */
    public Edge(Vertex a, Vertex b) {
        this.a = a;
        this.b = b;
        this.weight = a.EuclideanDistance(b);
        this.floorLayerMap = new HashMap<>();

        //Add the edge to each vertex.
        a.addEdge(this);
        b.addEdge(this);

        setComputedWeight();
    }

    /**
     * Computes the proper weight of this Edge
     * @author Tony Vuolo (bdane)
     */
    private void setComputedWeight() {
        floorLayerMap.put("L2", 0);
        floorLayerMap.put("L1", 1);
        floorLayerMap.put("GG", 2);
        floorLayerMap.put("01", 3);
        floorLayerMap.put("02", 4);
        floorLayerMap.put("03", 5);
        if(this.a.getID().length() == 10) {
            final int LENGTH = this.a.getID().length();
            final int FLOOR_DISTANCE = Math.abs(this.floorLayerMap.get(this.a.getID().substring(LENGTH - 2)) -
                    this.floorLayerMap.get(this.b.getID().substring(LENGTH - 2)));
            if(this.a.getID().contains("STAI") && this.b.getID().contains("STAI")) {
                this.weight = 15 * FLOOR_DISTANCE;
            } else if(this.a.getID().contains("ELEV") && this.b.getID().contains("ELEV")) {
                this.weight = 10 * FLOOR_DISTANCE;
            } else {
                this.weight = a.EuclideanDistance(b);
            }
        } else {
            this.weight = a.EuclideanDistance(b);
        }
    }

    /**
     * Gets the Vertices this Edge spans
     * @return {this.a, this.b}
     * @author Tony Vuolo
     */
    public Vertex[] getVertices() {
        return new Vertex[]{this.a, this.b};
    }

    /**
     * Gets the weight of this Edge
     * @return this.weight
     * @author Tony Vuolo
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Checks if a Vertex is an endpoint of this Edge
     * @param vertex the comparator Vertex
     * @return true if the Vertex is this.a or this.b, else false
     * @author Tony Vuolo
     */
    public boolean isEndpoint(Vertex vertex) {
        return this.a.getID().equals(vertex.getID()) || this.b.getID().equals(vertex.getID());
    }

    /**
     * Determines if two Edges are equal
     * @param edge the comparator Edge
     * @return true if this Edge and the comparator Edge share the same endpoints
     * @author Tony Vuolo
     */
    public boolean equals(Edge edge) {
        if(edge != null) {
            boolean checkEnds = (this.a.getID().equals(edge.a.getID())) && (this.b.getID().equals(edge.b.getID()));
            boolean checkEndsReverse = (this.a.getID().equals(edge.b.getID())) && (this.b.getID().equals(edge.a.getID()));
            return checkEnds || checkEndsReverse;
        }
        return false;
    }
}
