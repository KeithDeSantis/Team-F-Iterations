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
    private Graph graph;

    /**
     * Stores the edges in the graph.
     *
     * Created by Alex Friedman (ahf)
     * @see Graph
     * @see EdgeEntry
     */
    private List<EdgeEntry> edges;

    /**
     * Stores the vertices in the graph.
     *
     * Created by Alex Friedman
     * @see Graph
     * @see NodeEntry
     */
    private HashMap<String, NodeEntry> vertices;

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
        graph = GraphLoader.load(nodesCSV, edgesCSV);

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field edgesField = graph.getClass().getDeclaredField("edges");
        final Field verticesField = graph.getClass().getDeclaredField("vertices");
        //Set the fields to be accessible
        edgesField.setAccessible(true);
        verticesField.setAccessible(true);
        //Initialize our local lists
        edges = (List<EdgeEntry>) edgesField.get(graph);
        vertices = (HashMap<String, NodeEntry>) verticesField.get(graph);

    }

    @AfterAll
    public static void cleanup() {
    }

    //FIXME: TEST GETTERS/SETTERS?



    /**
     * This function tests Graph.contains(Vertex)
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see NodeEntry
     */
    @Test
    public void testContainsVertex() {
        /*
         * Iterate through our list of vertices and make sure that the graph
         * contains them all (as we know the graph does).
         */
        for (NodeEntry v : vertices.values()) {
            assertTrue(graph.contains(v));
        }
    }

    /**
     * This tests Graph.contains(Edge)
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see EdgeEntry
     */
    @Test
    public void testContainsEdge() {
        /*
         * Iterate through the list of edges that we know are all in the graph, and
         * make sure that Graph.contains(Edge) correctly identifies them.
         */
        for (EdgeEntry e : edges) {
            assertTrue(graph.contains(e));
        }
    }

    /**
     * Tests isEndpoint, getNeighbor, and equals methods [Vertex and Edge]
     *
     * @author Tony Vuolo
     */
    @Test
    public void testEdgeConnector() {
        NodeEntry v0 = new NodeEntry("0", 0, 0, ""), v1 = new NodeEntry("1", 3, 4, "");

        // asserts that the Vertices have no added Edges
        assertEquals(0, v0.getEdges().size());
        assertEquals(0, v1.getEdges().size());

        EdgeEntry edge0 = new EdgeEntry(v0, v1), edge1 = new EdgeEntry(v1, v0), edge2 = new EdgeEntry(v0, v0);

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
     * @see EdgeEntry
     * @see NodeEntry
     */
    @Test
    public void testDFSPathEdges() {
        graph.setPathfindingAlgorithm("dfs");

        //Tests that we always get the shortest path along an edge.
        for (EdgeEntry e : edges) {
            final NodeEntry v0 = e.getVertices()[0];
            final NodeEntry v1 = e.getVertices()[1];

            assertDoesNotThrow(() -> graph.getPath(v0, v1));

            final Path path = graph.getPath(v0, v1);

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
     * @see EdgeEntry
     * @see NodeEntry
     */
    @Test
    public void testDFSPathNullVertex() {
        graph.setPathfindingAlgorithm("dfs");
        final EdgeEntry e = edges.get(0);
        final NodeEntry v0 = e.getVertices()[0];
        final NodeEntry v1 = e.getVertices()[1];

        assertNull(graph.getPath(null, v1));
        assertNull(graph.getPath(v0, null));
        assertNull(graph.getPath(null, null));
    }

    /**
     * This runs a simple test on DFS to find the shortest path (least nodes), with three nodes in a loop,
     * check to see if the path always only contains two node
     * @author ZheCheng Song
     * @see Graph
     * @see EdgeEntry
     * @see NodeEntry
     */
    @Test
    public void testDFSPathShortest() {
        final NodeEntry v0 = new NodeEntry("T0", 0, 0, "");
        final NodeEntry v1 = new NodeEntry("T1", 0, 1, "");
        final NodeEntry v2 = new NodeEntry("T2", 1, 0, "");
        final EdgeEntry e0 = new EdgeEntry(v0, v1);
        final EdgeEntry e1 = new EdgeEntry(v1, v2);
        final EdgeEntry e2 = new EdgeEntry(v2, v0);

        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(e0);
        graph.addEdge(e1);
        graph.addEdge(e2);

        Path path;

        graph.setPathfindingAlgorithm("dfs");
        path = graph.getPath(v0, v1);
        assertEquals(2, path.length());
        path = graph.getPath(v1, v0);
        assertEquals(2, path.length());
        path = graph.getPath(v1, v2);
        assertEquals(2, path.length());
        path = graph.getPath(v2, v1);
        assertEquals(2, path.length());
        path = graph.getPath(v0, v2);
        assertEquals(2, path.length());
        path = graph.getPath(v2, v0);
        assertEquals(2, path.length());
    }

    /**
     * Tests longer chains in DFS.
     * @author Alex Friedman (ahf)
     * @see Graph
     * @see EdgeEntry
     * @see NodeEntry
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

        assertEquals(vertices.get("CHALL007L1"), path.get(0));
        assertEquals(vertices.get("CHALL008L1"), path.get(1));
        assertEquals(vertices.get("WELEV00KL1"), path.get(2));
        assertEquals(vertices.get("CHALL009L1"), path.get(3));
        assertEquals(vertices.get("CHALL010L1"), path.get(4));
        assertEquals(vertices.get("CREST003L1"), path.get(5));
        assertEquals(vertices.get("CHALL015L1"), path.get(6));
        assertEquals(vertices.get("CCONF001L1"), path.get(7));

        //FIXME: CHECK HEURISTIC!!!!
    }


    /**
     * As we know DFS works, we can simply test A* against it to verify that A* works.
     * @author Alex Friedman (ahf)
     */
    @Test
    public void testAStarWithDFS() {
        //tests every possibility b/c why not? we have a small dataset

        for (NodeEntry start : vertices.values()) {
            for (NodeEntry end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("dfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("a*");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path aStar = graph.getPath(start, end);//.asList();

                    if(dfs != null && aStar != null) {
                        Iterator<NodeEntry> dfsListIterator = dfs.iterator(),
                                aStarListIterator = aStar.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            NodeEntry dfsElement = dfsListIterator.next(), aStarElement = aStarListIterator.next();
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
        graph.setPathfindingAlgorithm("a*");
        final EdgeEntry e = edges.get(0);
        final NodeEntry v0 = e.getVertices()[0];
        final NodeEntry v1 = e.getVertices()[1];

        assertNull(graph.getPath(null, v1));
        assertNull(graph.getPath(v0, null));
        assertNull(graph.getPath(null, null));
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
    private Path runDFS(NodeEntry start, NodeEntry end) //FIXME: DO BETTER, methodize better & use in more places?
    {
        graph.setPathfindingAlgorithm("dfs");
        assertDoesNotThrow(() -> graph.getPath(start, end));

        return graph.getPath(start, end);
    }

    /**
     * Verifies that the DoublyLinkedHashSet correctly adds elements at specific indices
     * @author Tony Vuolo
     */
    @Test
    public void testDoublyLinkedHashMap() {
        NodeEntry v0 = new NodeEntry("CCONF", 3, 7, "");
        NodeEntry v1 = new NodeEntry("CDEPT", 3, 7, "");
        NodeEntry v2 = new NodeEntry("CHALL", 3, 7, "");
        NodeEntry v3 = new NodeEntry("CLABS", 3, 7, "");

        DoublyLinkedHashSet<NodeEntry> map = new DoublyLinkedHashSet<>();
        map.insert(0, v0);
        map.insert(0, v1);
        map.insert(0, v2);
        map.insert(0, v3);

        for(NodeEntry v : map) {
            assertTrue(map.containsKey(v));
        }
    }


    @Test
    public void testBFSWithDFS() {
        //tests every possibility b/c why not? we have a small dataset

        for (NodeEntry start : vertices.values()) {
            for (NodeEntry end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("dfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("bfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path bfs = graph.getPath(start, end);//.asList();

                    if(dfs != null && bfs != null) {


                        Iterator<NodeEntry> dfsListIterator = dfs.iterator(),
                                aStarListIterator = bfs.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            NodeEntry dfsElement = dfsListIterator.next(), bfsElement = aStarListIterator.next();
                            assertTrue(dfsElement.equals(bfsElement));
                        }
                        assertFalse(dfsListIterator.hasNext() || aStarListIterator.hasNext());
                    } else {
                        assertTrue(dfs == null && bfs == null);
                    }
                }
            }
        }
    }

    /**
     * Tests the functionality of the concatenate() function in DoublyLinkedHashSet
     * @author Tony Vuolo
     */
    @Test
    public void testDLHSConcatenate() {
        DoublyLinkedHashSet<Integer> set = new DoublyLinkedHashSet<>();
        List<Integer> list = new LinkedList<>();
        for(int i = 0; i < 10; i++) {
            DoublyLinkedHashSet<Integer> augend = new DoublyLinkedHashSet<>();
            augend.add(i);
            set.concatenate(augend);
            list.add(i);
            assertEquals(list.toString(), set.toString());
        }
    }

    /**
     * Tests the functionality of the concatenate() function in Path
     * @author Tony Vuolo (bdane)
     */
    @Test
    public void testPathConcatenate() {
        final int MAX_NUM_VERTICES = 10;
        NodeEntry[] vertices = new NodeEntry[MAX_NUM_VERTICES * 2 + 1];
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = new NodeEntry("" + i, 0, 0, "");
        }
        Path mainPath = new Path();
        Path[] path = new Path[MAX_NUM_VERTICES];
        for(int i = 0; i < MAX_NUM_VERTICES; i++) {
            path[i] = new Path();
            for(int j = 0; j < 3; j++) {
                path[i].addVertexToPath(vertices[2 * i + j], 0);
            }
            mainPath.concatenate(path[i]);
        }
        int value = 0;
        for(NodeEntry vertex : mainPath) {
            assertEquals("" + value++, vertex.getNodeID());
        }
    }

    /**
     * Tests whether getPath with a List of Vertices works
     * @author Tony Vuolo (bdane)
     */
    @Test
    public void testGetPathWithOrderedStops() {
        String[] vertexList = {
                "CLABS002L1",
                "WELEV00KL1",
                "CREST001L1",
                "CHALL007L1",
                "CRETL001L1",
                "CDEPT003L1"
        };
        List<NodeEntry> list = new LinkedList<>();
        for(String element : vertexList) {
            list.add(this.vertices.get(element));
        }
        Path totalPath = this.graph.getPath(list);
        List<NodeEntry> totalPathList = totalPath.asList(), fragmentedPathList = new LinkedList<>();
        fragmentedPathList.add(this.vertices.get(vertexList[0]));
        double length = 0;
        for(int i = 1; i < vertexList.length; i++) {
            Path newPathFragment = this.graph.getPath(this.vertices.get(vertexList[i - 1]), this.vertices.get(vertexList[i]));
            length += newPathFragment.getPathCost();
            List<NodeEntry> fragmentList = newPathFragment.asList();
            fragmentList.remove(0);
            fragmentedPathList.addAll(fragmentList);
        }
        assertEquals(totalPathList.size(), fragmentedPathList.size());
        assertEquals(totalPath.getPathCost(), length, 0.1);
        ListIterator<NodeEntry> iterator = totalPathList.listIterator();;
        for(NodeEntry v : fragmentedPathList) {
            assertEquals(iterator.next().getNodeID(), v.getNodeID());
        }
    }
}
