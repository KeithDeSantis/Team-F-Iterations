package edu.wpi.fuchsiafalcons.pathfinding;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final List<Edge> edges;
    private final HashMap<String, Vertex> vertices;

    /**
     * Creates a new Graph
     * @author Tony Vuolo
     */
    public Graph() {
        this.edges = new LinkedList<>();
        this.vertices = new HashMap<>();
    }

    /**
     * Adds an Edge to this Graph
     * @param edge the additive Edge
     * @return true if both endpoint Vertices are already contained in the Graph, else false
     * @author Tony Vuolo
     */
    public boolean addEdge(Edge edge) {
        this.edges.add(edge);
        Vertex[] endpoints = edge.getVertices();
        return this.vertices.get(endpoints[0].getID()) != null && this.vertices.get(endpoints[1].getID()) != null;
    }

    /**
     * Adds a Vertex to this Graph
     * @param vertex the additive Vertex
     * @author Tony Vuolo
     */
    public void addVertex(Vertex vertex) {
        this.vertices.put(vertex.getID(), vertex);
    }

    /**
     * Determines whether this Graph contains a specific Edge
     * @param edge the comparator Edge
     * @return true if the comparator Edge is contained in this Graph, else false
     * @author Tony Vuolo
     */
    public boolean contains(Edge edge) {
        for(Edge query : this.edges) {
            if(query.equals(edge)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to get the list of vertices.
     * @return An array containing all of our vertices.
     * @author Alex Friedman (ahf), Tony Vuolo
     */
    public List<Vertex> getVertices() {
        return vertices.values().stream().collect(Collectors.toList());
    }

    /**
     * USed to get a vertex from its ID.
     * @param vertexID The ID of the vertex
     * @return The vertex
     * @author Alex Friedman (ahf), Tony Vuolo
     */
    public Vertex getVertex(String vertexID)
    {
        return this.vertices.get(vertexID);
    }

    /**
     * Determines whether this Graph contains a specific Vertex
     * @param vertex the comparator Vertex
     * @return true if the comparator Vertex is contained in this Graph, else false
     * @author Tony Vuolo
     */
    public boolean contains(Vertex vertex) {
        for(String key : this.vertices.keySet()) {
            Vertex query = this.vertices.get(key);
            if(query.equals(vertex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the path of least weight between two Vertices using an A* algorithm
     * @param a the start Vertex
     * @param b the endpoint Vertex
     * @return the List of Vertices spanning the path of least weight from Vertex a to Vertex b
     * @author Tony Vuolo
     */
    public Path getPath(Vertex a, Vertex b) {
        if (!contains(a) || !contains(b)) {
            return null;
        }
        List<Vertex> exhaustedVertices = new LinkedList<>();
        HashMap<String, Double> weights = new HashMap<>();
        HashMap<String, Vertex> linkages = new HashMap<>();
        Vertex proxy = a;
        weights.put(proxy.getID(), 0.0);
        boolean continueGraphSearchIteration = !proxy.equals(b);
        while(continueGraphSearchIteration) {
            exhaustedVertices.add(proxy);
            for(Edge edge : proxy.getEdges()) {
                Vertex neighbor = proxy.getNeighbor(edge);
                if(doesNotContain(exhaustedVertices, neighbor)) {
                    Vertex prev = linkages.get(neighbor.getID());
                    Double dijkstraWeight = weights.get(proxy.getID()) + edge.getWeight();
                    if(prev != null) {
                        if(weights.get(neighbor.getID()) > dijkstraWeight) {
                            weights.replace(neighbor.getID(), dijkstraWeight);
                            linkages.replace(neighbor.getID(), proxy);
                        }
                    } else {
                        weights.put(neighbor.getID(), dijkstraWeight);
                        linkages.put(neighbor.getID(), proxy);
                    }
                }
            }
            Vertex next = null;
            for(String key : weights.keySet()) {
                Vertex vertex = this.vertices.get(key);
                if((doesNotContain(exhaustedVertices, vertex))) {
                    if(next == null) {
                        next = vertex;
                    } else if(weights.get(vertex.getID()) + vertex.heuristic(b) < weights.get(next.getID()) + next.heuristic(b)) {
                        next = vertex;
                    }
                }
            }
            if(next == null) {
                continueGraphSearchIteration = false;
            } else {
                proxy = next;
                if(proxy.equals(b)) {
                    continueGraphSearchIteration = false;
                }
            }
        }
//        List<Vertex> pathList = null;
//        if(proxy.equals(b)) {
//            pathList = new LinkedList<>();
//            boolean continueBackwardPathGeneration = true;
//            while(continueBackwardPathGeneration) {
//                proxy = linkages.get(proxy.getID());
//                if (proxy == null) {
//                    continueBackwardPathGeneration = false;
//                } else {
//                    pathList.add(0, proxy);
//                }
//            }
//        }
//        Path path = new Path();
//        if(pathList == null) {
//            path = null;
//        } else {
//            for(Vertex vertex : pathList) {
//                path.addVertexToPath(vertex, 0);
//            }
//            path.addVertexToPath(b, weights.get(b.getID()));
//        }
//        return path == null ? null : path.asList();

        Path path = null;
        if(proxy.equals(b)) {
            path = new Path();
            path.addVertexToPath(b, weights.get(b.getID()));
            Vertex cursor = linkages.get(b.getID());
            while(cursor != null) {
                path.insertVertexInPath(0, cursor, 0);
                cursor = linkages.get(cursor.getID());
            }
        }
        return path;
    }


    /**
     * Performs Depth-First Search on adjacency matrix
     * @param a vertex to start from
     * @return ArrayList of resultant path
     */
    public Path DFS(Vertex a, Vertex b) {

        if (contains(a) && contains(b)) { //FIXME: NULL CHECK
            final Path path = DFS_Recur(a, b, new Path());

            return path;
        }

        return null;
    }

    //FIXME: COMMENT
    private Path DFS_Recur(Vertex curr, Vertex end, Path path)
    {
        if(curr == null) //FIXME: DO MORE NULL CHECKS, END SHOULDN'T BE ABLE TO BE NULL ANYWAYS
            return null;

        path.addVertexToPath(curr, 0.0);
        //path.add(curr);

        if(curr == end)
            return path;

        Path acceptedPath = null;

        for(Edge e : curr.getEdges())
        {
            final Vertex n = curr.getNeighbor(e);

            if(!path.contains(n))
            {
                //FIXME: Use more as a stack?
                //final Path duplicated = path.clone();
                //duplicated.addVertexToPath(n, e.getWeight());
                path.addVertexToPath(n, e.getWeight());

                final Path currPath = DFS_Recur(n, end, path); //duplicated);

                //FIXME: DO BETTER HEURISTIC CHECK ON PATH SIZES TO ENSURE WE DON'T TEST ALL. MAKE PATH CLASS
                if(currPath != null) {
                    if (acceptedPath == null)
                        acceptedPath = currPath.clone();
                    else if (currPath.getPathCost() < acceptedPath.getPathCost())//currPath.size() < acceptedPath.size())
                    {
                        acceptedPath = currPath.clone();
                    }
                }
                path.removeVertexFromPath(n, e.getWeight());
            }
        }

        return acceptedPath;
    }

    /**
     * Determines if a given List contains a specific Vertex
     * @param list the given List
     * @param vertex the comparator Vertex
     * @return true if some value in the List is congruent to the Vertex, else false
     * @author Tony Vuolo
     */
    private boolean doesNotContain(List<Vertex> list, Vertex vertex) {
        for(Vertex query : list) {
            if(query.equals(vertex)) {
                return false;
            }
        }
        return true;
    }
}
