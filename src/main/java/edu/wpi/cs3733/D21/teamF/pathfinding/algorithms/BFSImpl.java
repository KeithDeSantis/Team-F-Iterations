package edu.wpi.cs3733.D21.teamF.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamF.pathfinding.*;

import java.util.*;

public class BFSImpl implements IPathfindingAlgorithm {


    /**
     * Performs Breath-First Search on adjacency matrix
     * @param a vertex to start from
     * @return ArrayList of resultant path
     */
    @Override
    public Path getPath(Graph graph, Vertex a, Vertex b) {
        if (graph.contains(a) && graph.contains(b)) { //FIXME: NULL CHECK
            return BFS(a, b);
        }
        return null;
    }


    //FIXME: COMMENT
    private Path BFS(Vertex start, Vertex end)
    {
        if(start == null) //FIXME: DO MORE NULL CHECKS, END SHOULDN'T BE ABLE TO BE NULL ANYWAYS
            return null;

        final Queue<BFSNode> verticesToCheck = new LinkedList<>();
        verticesToCheck.add(new BFSNode(0.0, null, start));

        final Set<Vertex> visited = new HashSet<>();
        //visited.add(start);


        BFSNode bestPath = null;

        while(verticesToCheck.size() > 0)
        {
            final BFSNode currentVertex = verticesToCheck.remove();

            if(visited.contains(currentVertex.value))
                continue;

            visited.add(currentVertex.value);

            for(Edge e : currentVertex.value.getEdges())
            {
                final Vertex n = currentVertex.value.getNeighbor(e);

                final BFSNode currBFSNode = new BFSNode(currentVertex.heuristic + e.getWeight(), currentVertex, n);


                if(!visited.contains(n) && (bestPath == null || (bestPath.heuristic > currBFSNode.heuristic))) {
                    //   visited.add(n);
                    verticesToCheck.add(currBFSNode);
                }

                if(n.equals(end) && (bestPath == null || bestPath.heuristic > currBFSNode.heuristic))
                {
                    bestPath = currBFSNode;
                }
            }


        }

        if(bestPath == null)
            return  null;

        //FIXME: MOVE THIS BACK UP INTO DFS MAIN LOOP
        BFSNode cursor = bestPath;

        final Path path = new Path();

        path.insertVertexInPath(0, cursor.value, cursor.heuristic);
        cursor = cursor.parent;

        while(cursor != null)
        {
            //FIXME: BETTER INSERT TO TRACK WEIGHT
            path.insertVertexInPath(0, cursor.value, 0);

            cursor = cursor.parent;
        }
        return path;
    }

    private static class BFSNode implements Comparable<BFSNode>{
        private final Double heuristic;
        private final BFSNode parent;
        private final Vertex value;

        public BFSNode(Double heuristic, BFSNode parent, Vertex value) {
            this.heuristic = heuristic;
            this.parent = parent;
            this.value = value;
        }

        @Override
        public int compareTo(BFSNode o) {
            return heuristic.compareTo(o.heuristic);
        }

        @Override
        public String toString() {
            return "BFSNode{" +
                    "heuristic=" + heuristic +
                    ", parent=" + ((parent != null) ? parent.value  : "null") +
                    ", value=" + value +
                    '}';
        }
    }
}
