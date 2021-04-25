package edu.wpi.cs3733.D21.teamF.pathfinding;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphTest {

    /**
     * An instance of Graph initialized with our data that we will run tests with.
     *
     * Created by Alex Friedman
     * @see Graph
     */
    private Graph AstarGraph;

    private Graph BFSGraph;

    private Graph DFSGraph;

    /**
     * Stores the edges in the graph.
     *
     * Created by Alex Friedman (ahf)
     * @see Graph
     * @see Edge
     */
    private List<Edge> edges;

    /**
     * Stores the vertices in the graph.
     *
     * Created by Alex Friedman
     * @see Graph
     * @see Vertex
     */
    private HashMap<String, Vertex> vertices;

    /**
     * Setup test suite.
     */
    @BeforeAll
    public void setUp() throws Exception {

        /*
         * Alex Friedman (ahf) - This section is used to initialize the Graph with our
         * data as well as initialize our copy of the vertex and edges storing datastructures
         * so that we have easy access later.
         */
        final String edgesCSV = "DFSEdges.csv";
        final String nodesCSV = "DFSNodes.csv";

        AstarGraph = GraphLoader.load(nodesCSV, edgesCSV);
        DFSGraph = GraphLoader.load(nodesCSV, edgesCSV);
        BFSGraph = GraphLoader.load(nodesCSV, edgesCSV);

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field edgesField = AstarGraph.getClass().getDeclaredField("edges");
        final Field verticesField = AstarGraph.getClass().getDeclaredField("vertices");
        //Set the fields to be accessible
        edgesField.setAccessible(true);
        verticesField.setAccessible(true);
        //Initialize our local lists
        edges = (List<Edge>) edgesField.get(AstarGraph);
        vertices = (HashMap<String, Vertex>) verticesField.get(AstarGraph);

    }

    @AfterAll
    public static void cleanup() {
    }

    //FIXME: TEST GETTERS/SETTERS?



    /**
     * This function tests Graph.contains(Vertex)
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see Vertex
     */
    @Test
    public void testContainsVertex() {
        /*
         * Iterate through our list of vertices and make sure that the graph
         * contains them all (as we know the graph does).
         */
        for (Vertex v : vertices.values()) {
            assertTrue(AstarGraph.contains(v));
        }
    }

    /**
     * This tests Graph.contains(Edge)
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see Edge
     */
    @Test
    public void testContainsEdge() {
        /*
         * Iterate through the list of edges that we know are all in the graph, and
         * make sure that Graph.contains(Edge) correctly identifies them.
         */
        for (Edge e : edges) {
            assertTrue(AstarGraph.contains(e));
        }
    }

    /**
     * Tests isEndpoint, getNeighbor, and equals methods [Vertex and Edge]
     *
     * @author Tony Vuolo
     */
    @Test
    public void testEdgeConnector() {
        Vertex v0 = new Vertex("0", 0, 0, ""), v1 = new Vertex("1", 3, 4, "");

        // asserts that the Vertices have no added Edges
        assertEquals(0, v0.getEdges().size());
        assertEquals(0, v1.getEdges().size());

        Edge edge0 = new Edge(v0, v1), edge1 = new Edge(v1, v0), edge2 = new Edge(v0, v0);

        // asserts that the Vertices have added all Edges above
        assertEquals(4, v0.getEdges().size());
        assertEquals(2, v1.getEdges().size());

        // asserts that v0 is an endpoint of edge0
        assertTrue(edge0.isEndpoint(v0));
        // asserts that v1 is an endpoint of edge0
        assertTrue(edge0.isEndpoint(v1));

        // asserts that v1 is a neighbor of v0
        assertTrue(v0.getNeighbor(edge0).equals(v1));
        // asserts that v0 is a neighbor of v1
        assertTrue(v1.getNeighbor(edge0).equals(v0));

        double[] coordinatesV0 = v0.getCoordinates(), coordinatesV1 = v1.getCoordinates();
        // asserts that the coordinates are called correctly
        assertEquals(0, (int)coordinatesV0[0]);
        assertEquals(0, (int)coordinatesV0[1]);
        assertEquals(3, (int)coordinatesV1[0]);
        assertEquals(4, (int)coordinatesV1[1]);

        // asserts that a non-null Vertex and null Vertex register as unequal
        assertFalse(v0.equals(null));

        // asserts that the edge weights and v0-v1 distance are approximately equal to the Euclidean distance
        assertEquals(5, (int) (v0.EuclideanDistance(v1) + 0.1));
        assertEquals(5, (int) (edge0.getWeight() + 0.1));
        assertEquals(5, (int) (edge1.getWeight() + 0.1));
        assertEquals(0, (int) (edge2.getWeight() + 0.1));

        // asserts that edge0 is equal to edge1
        assertTrue(edge0.equals(edge1));
        // asserts that edge0 is not equal to edge2
        assertFalse(edge0.equals(edge2));

        v0.addEdge(edge0);
        v1.addEdge(edge0);
        // asserts that the Vertices have one additional Edge each
        assertEquals(v0.getEdges().size(), 5);
        assertEquals(v1.getEdges().size(), 3);
    }

    /**
     * This runs a basic test of DFS by making sure that the DFS algorithm is able
     * to path find along edges. As DFS does not guarantee that the shortest path is
     * found first, having this work in all cases is a decent DFS test, but more
     * should be written.
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see Edge
     * @see Vertex
     */
    @Test
    public void testDFSPathEdges() {
        //Tests that we always get the shortest path along an edge.
        for (Edge e : edges) {
            final Vertex v0 = e.getVertices()[0];
            final Vertex v1 = e.getVertices()[1];

            assertDoesNotThrow(() -> BFSGraph.getPath(v0, v1));

            final Path path = BFSGraph.getPath(v0, v1);

            assertNotNull(path);

            assertEquals(2, path.length());

            assertEquals(path.get(0), v0);
            assertEquals(path.get(1), v1);
        }
    }

    /**
     * This runs a simple test of DFS to make sure when inputting null vertex, it's
     * able to avoid error and return null
     * @author ZheCheng Song
     * @see Graph
     * @see Edge
     * @see Vertex
     */
    @Test
    public void testDFSPathNullVertex() {
        final Edge e = edges.get(0);
        final Vertex v0 = e.getVertices()[0];
        final Vertex v1 = e.getVertices()[1];

        assertNull(BFSGraph.getPath(null, v1));
        assertNull(BFSGraph.getPath(v0, null));
        assertNull(BFSGraph.getPath(null, null));
    }

    /**
     * This runs a simple test on DFS to find the shortest path (least nodes), with three nodes in a loop,
     * check to see if the path always only contains two node
     * @author ZheCheng Song
     * @see Graph
     * @see Edge
     * @see Vertex
     */
    @Test
    public void testDFSPathShortest() {
        final Vertex v0 = new Vertex("T0", 0, 0, "");
        final Vertex v1 = new Vertex("T1", 0, 1, "");
        final Vertex v2 = new Vertex("T2", 1, 0, "");
        final Edge e0 = new Edge(v0, v1);
        final Edge e1 = new Edge(v1, v2);
        final Edge e2 = new Edge(v2, v0);

        DFSGraph.addVertex(v0);
        DFSGraph.addVertex(v1);
        DFSGraph.addVertex(v2);
        DFSGraph.addEdge(e0);
        DFSGraph.addEdge(e1);
        DFSGraph.addEdge(e2);

        Path path;

        path = DFSGraph.getPath(v0, v1);
        assertEquals(2, path.length());
        path = DFSGraph.getPath(v1, v0);
        assertEquals(2, path.length());
        path = DFSGraph.getPath(v1, v2);
        assertEquals(2, path.length());
        path = DFSGraph.getPath(v2, v1);
        assertEquals(2, path.length());
        path = DFSGraph.getPath(v0, v2);
        assertEquals(2, path.length());
        path = DFSGraph.getPath(v2, v0);
        assertEquals(2, path.length());
    }

    /**
     * Tests longer chains in DFS.
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see Edge
     * @see Vertex
     */
    @Test
    public void testDFSGraphChain() {
        //This //No path should be able to be found here.
        Path path = runDFS(vertices.get("GEXIT001L1"), vertices.get("CCONF002L1"));
        assertNull(path);

        //THis should be a branching path as described here;
        path = runDFS(vertices.get("CHALL007L1"), vertices.get("CCONF001L1"));
        assertNotNull(path);
        assertEquals(8, path.length());

        assertEquals(vertices.get("CHALL007L1").getID(), path.get(0).getID());
        assertEquals(vertices.get("CHALL008L1").getID(), path.get(1).getID());
        assertEquals(vertices.get("WELEV00KL1").getID(), path.get(2).getID());
        assertEquals(vertices.get("CHALL009L1").getID(), path.get(3).getID());
        assertEquals(vertices.get("CHALL010L1").getID(), path.get(4).getID());
        assertEquals(vertices.get("CREST003L1").getID(), path.get(5).getID());
        assertEquals(vertices.get("CHALL015L1").getID(), path.get(6).getID());
        assertEquals(vertices.get("CCONF001L1").getID(), path.get(7).getID());

        //FIXME: CHECK HEURISTIC!!!!
    }


    /**
     * As we know DFS works, we can simply test A* against it to verify that A* works.
     * @author Alex Friedman (ahf)
     */
    @Test
    public void testAStarWithDFS() {
        //tests every possibility b/c why not? we have a small dataset

        for (Vertex start : vertices.values()) {
            for (Vertex end : vertices.values()) {
                if (! start.equals(end)) {

                    assertDoesNotThrow(() -> DFSGraph.getPath(start, end));
                    final Path dfs = DFSGraph.getPath(start, end);

                    assertDoesNotThrow(() -> AstarGraph.getPath(start, end));
                    final Path aStar = AstarGraph.getPath(start, end);//.asList();

                    if(dfs != null && aStar != null) {
                        Iterator<Vertex> dfsListIterator = dfs.iterator(),
                                aStarListIterator = aStar.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            Vertex dfsElement = dfsListIterator.next(), aStarElement = aStarListIterator.next();
                            assertTrue(dfsElement.equals(aStarElement));
                        }
                        assertFalse(dfsListIterator.hasNext() || aStarListIterator.hasNext());
                    } else {
                        assertTrue(dfs == null && aStar == null);
                    }
                }
            }
        }
    }

    /**
     * Simple null test for A*
     * @author ZheCheng Song
     */
    @Test
    public void testAStarNullVertex() {
        final Edge e = edges.get(0);
        final Vertex v0 = e.getVertices()[0];
        final Vertex v1 = e.getVertices()[1];

        assertNull(AstarGraph.getPath(null, v1));
        assertNull(AstarGraph.getPath(v0, null));
        assertNull(AstarGraph.getPath(null, null));
    }

    /**
     * Runs a DFS test between start and end, verifies NullPointerException wasn't thrown
     *
     * @param start The start vertex
     * @param end   The end vertex
     * @return The path between the two
     * @author Alex Friedman (ahf)
     */
    @Test
    private Path runDFS(Vertex start, Vertex end) //FIXME: DO BETTER, methodize better & use in more places?
    {
        assertDoesNotThrow(() -> DFSGraph.getPath(start, end));

        return DFSGraph.getPath(start, end);
    }

    /**
     * Verifies that the DoublyLinkedHashSet correctly adds elements at specific indices
     * @author Tony Vuolo
     */
    @Test
    public void testDoublyLinkedHashMap() {
        Vertex v0 = new Vertex("CCONF", 3, 7, "");
        Vertex v1 = new Vertex("CDEPT", 3, 7, "");
        Vertex v2 = new Vertex("CHALL", 3, 7, "");
        Vertex v3 = new Vertex("CLABS", 3, 7, "");

        DoublyLinkedHashSet<Vertex> map = new DoublyLinkedHashSet<>();
        map.insert(0, v0);
        map.insert(0, v1);
        map.insert(0, v2);
        map.insert(0, v3);

        for(Vertex v : map) {
            assertTrue(map.containsKey(v));
        }
    }

}
