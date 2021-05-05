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
        graph = GraphLoader.load(nodesCSV, edgesCSV);

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field edgesField = graph.getClass().getDeclaredField("edges");
        final Field verticesField = graph.getClass().getDeclaredField("vertices");
        //Set the fields to be accessible
        edgesField.setAccessible(true);
        verticesField.setAccessible(true);
        //Initialize our local lists
        edges = (List<Edge>) edgesField.get(graph);
        vertices = (HashMap<String, Vertex>) verticesField.get(graph);

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
            assertTrue(graph.contains(v));
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
        graph.setPathfindingAlgorithm("dfs");

        //Tests that we always get the shortest path along an edge.
        for (Edge e : edges) {
            final Vertex v0 = e.getVertices()[0];
            final Vertex v1 = e.getVertices()[1];

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
     * @see Edge
     * @see Vertex
     */
    @Test
    public void testDFSPathNullVertex() {
        graph.setPathfindingAlgorithm("dfs");
        final Edge e = edges.get(0);
        final Vertex v0 = e.getVertices()[0];
        final Vertex v1 = e.getVertices()[1];

        assertNull(graph.getPath(null, v1));
        assertNull(graph.getPath(v0, null));
        assertNull(graph.getPath(null, null));
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

        for (Vertex start : vertices.values()) {
            for (Vertex end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("dfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("a*");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path aStar = graph.getPath(start, end);//.asList();

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
        graph.setPathfindingAlgorithm("a*");
        final Edge e = edges.get(0);
        final Vertex v0 = e.getVertices()[0];
        final Vertex v1 = e.getVertices()[1];

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
    private Path runDFS(Vertex start, Vertex end) //FIXME: DO BETTER, methodize better & use in more places?
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


    @Test
    public void testBFSWithDFS() {
        //tests every possibility b/c why not? we have a small dataset

        for (Vertex start : vertices.values()) {
            for (Vertex end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("dfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("bfs");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path bfs = graph.getPath(start, end);//.asList();

                    if(dfs != null && bfs != null) {


                        Iterator<Vertex> dfsListIterator = dfs.iterator(),
                                aStarListIterator = bfs.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            Vertex dfsElement = dfsListIterator.next(), bfsElement = aStarListIterator.next();
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
        Vertex[] vertices = new Vertex[MAX_NUM_VERTICES * 2 + 1];
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex("" + i, 0, 0, "");
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
        for(Vertex vertex : mainPath) {
            assertEquals("" + value++, vertex.getID());
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
        List<Vertex> list = new LinkedList<>();
        for(String element : vertexList) {
            list.add(this.vertices.get(element));
        }
        Path totalPath = this.graph.getPath(list);
        List<Vertex> totalPathList = totalPath.asList(), fragmentedPathList = new LinkedList<>();
        fragmentedPathList.add(this.vertices.get(vertexList[0]));
        double length = 0;
        for(int i = 1; i < vertexList.length; i++) {
            Path newPathFragment = this.graph.getPath(this.vertices.get(vertexList[i - 1]), this.vertices.get(vertexList[i]));
            length += newPathFragment.getPathCost();
            List<Vertex> fragmentList = newPathFragment.asList();
            fragmentList.remove(0);
            fragmentedPathList.addAll(fragmentList);
        }
        assertEquals(totalPathList.size(), fragmentedPathList.size());
        assertEquals(totalPath.getPathCost(), length, 0.1);
        ListIterator<Vertex> iterator = totalPathList.listIterator();
        for(Vertex v : fragmentedPathList) {
            assertEquals(iterator.next().getID(), v.getID());
        }
    }
 /**
     * Tests HashCluster join() and iterator() methods
     * @author Tony Vuolo (bdane)
     * @see HashCluster
     */
    @Test
    public void testHashCluster() {
        HashCluster<Integer> cluster = new HashCluster<>();
        for(int i = 0; i < 15; i++) {
            cluster.add(i);
        }
        for(int i = 0; i < 6; i++) {
            cluster.join(i, i + 1);
        }
        for(int i = 10; i < 14; i++) {
            cluster.join(i, i + 1);
        }
        cluster.join(7, 10);

        assertEquals(4, cluster.getNumberOfChains());
        assertNull(cluster.focus);
        int index = 0;
        cluster.focus = 0;
        for(int i : cluster) {
            assertEquals(index++, i);
            assertTrue(i < 7);
        }
        cluster.focus = 7;
        index = 9;
        for(int i : cluster) {
            assertEquals((index == 9 ? -2 : 0) + index++, i);
        }
    }

    /**
     * Tests switchAfter() in DoublyLinkedHashSet.java
     * @author Tony Vuolo (bdane)
     * @see DoublyLinkedHashSet
     */
    @Test
    public void testDLHSSwitchAfter() {
        DoublyLinkedHashSet<Integer> set = new DoublyLinkedHashSet<>();
        for(int i = 0; i < 10; i++) {
            set.add(i);
        }
        set.switchAfter(5);
        set.switchAfter(5);
        set.switchAfter(5);
        assertEquals("[0, 1, 2, 3, 4, 6, 7, 8, 5, 9]", set.toString());
    }

    /**
     * Tests getUnorderedPath() in Graph.java
     * @author Tony Vuolo (bdane)
     * @see Graph
     * @see HashCluster
     */
    @Test
    public void testFindPathWithUnorderedStops() {
        String[] vertexIDs = {
                "CREST002L1",
                "CHALL002L1",
                "CRETL001L1",
                "CHALL013L1",
                "CDEPT002L1",
                "CSERV001L1"
        };
        Vertex[] vertices = new Vertex[vertexIDs.length];
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = this.vertices.get(vertexIDs[i]);
        }
        assertEquals(this.graph.getEfficientOrder(vertices).toString(), "[CHALL002L1, CRETL001L1, CHALL013L1, CDEPT002L1, CREST002L1, CSERV001L1]");
    }
  @Test
    public void testBestFirstWithAStar() {
        //tests every possibility b/c why not? we have a small dataset

        for (Vertex start : vertices.values()) {
            for (Vertex end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("AStar");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("bestfirst");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path bfs = graph.getPath(start, end);//.asList();

                    if(dfs != null && bfs != null) {


                        Iterator<Vertex> dfsListIterator = dfs.iterator(),
                                aStarListIterator = bfs.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            Vertex dfsElement = dfsListIterator.next(), bfsElement = aStarListIterator.next();
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

    @Test
    public void testDijkstraWithAStar() {
        //tests every possibility b/c why not? we have a small dataset

        for (Vertex start : vertices.values()) {
            for (Vertex end : vertices.values()) {
                if (! start.equals(end)) {

                    graph.setPathfindingAlgorithm("AStar");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path dfs = graph.getPath(start, end);

                    graph.setPathfindingAlgorithm("Dijkstra");
                    assertDoesNotThrow(() -> graph.getPath(start, end));
                    final Path bfs = graph.getPath(start, end);//.asList();

                    if(dfs != null && bfs != null) {


                        Iterator<Vertex> dfsListIterator = dfs.iterator(),
                                aStarListIterator = bfs.iterator();
                        while(dfsListIterator.hasNext() && aStarListIterator.hasNext()) {
                            Vertex dfsElement = dfsListIterator.next(), bfsElement = aStarListIterator.next();
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

    @Test
    public void testPermutation() {
        Permutation permutation = new Permutation(4);
        int[] p = permutation.getPermutation();
        assertEquals(4, p.length);
        for(int i = 0; i < 24; i++) {
            permutation.makeNextPermutation();
        }
        int[] p2 = permutation.getPermutation();
        for(int i = 0; i < 4; i++) {
            assertEquals(i, p2[i]);
            assertEquals(i, p[i]);
        }
    }

    /**
     *
     */
    @Test
    public void testUnorderedPair() {
        HashMap<UnorderedPair, Integer> map = new HashMap<>();
        map.put(new UnorderedPair(2, 4), 5);
        assertEquals(5, map.get(new UnorderedPair(2, 4)));
        assertEquals(5, map.get(new UnorderedPair(4, 2)));
    }
}
