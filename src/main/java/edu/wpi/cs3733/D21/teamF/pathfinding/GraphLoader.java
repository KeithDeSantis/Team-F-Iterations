package edu.wpi.cs3733.D21.teamF.pathfinding;

import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;

import java.util.HashMap;
import java.util.List;

/**
 * FIXME: Rename this class.
 *
 * This class is used to load graphs from the CSV file.
 * @author Alex Friedman (ahf)
 */
class GraphLoader {

    /**
     * Given a CSV file for nodes and edges, this function will
     * read the files and construct the corresponding graph for them.
     * @param nodesCSV The CSV file of nodes
     * @param edgesCSV The CSV file of edges
     * @return A Graph representing the data in the files.
     * @author Alex Friedman (ahf)
     * @see Graph
     */
    public static Graph load(String nodesCSV, String edgesCSV) throws Exception {
        //Null checks provided by exceptions in CSVReader.load
        throw new Error("Removed");
        /*

        Graph graph = new Graph();

        final List<String[]> nodesReader = CSVManager.load(nodesCSV);

        //I would use a HashSet, but HashSets use HashMaps anyways, so this just makes sense.
        final HashMap<String, Vertex> vertices = new HashMap<>();

        if(!nodesReader.isEmpty() && nodesReader.get(0).length != 8)
            throw new Exception("Invalid nodes csv file format");

        for(String[] currLine : nodesReader)
        {
            //Each line going to be same length b/c of how CSVReader works.

            try {
                final String nodeID = currLine[0];

                if(vertices.get(nodeID) != null)
                    throw new Exception("nodesCVS lists the same vertex twice: " + nodeID);

                final int xCoordinate = Integer.parseInt(currLine[1]);
                final int yCoordinate = Integer.parseInt(currLine[2]);

                final String floor = currLine[3];

                final Vertex currVertex = new Vertex(nodeID, xCoordinate, yCoordinate, floor);
                vertices.put(nodeID, currVertex);

                graph.addVertex(currVertex);
            }
            catch(Exception e)
            {
                throw new Exception("Incorrect node CSV format: " + e.getMessage());
            }
        }

        final List<String[]> edgesReader = CSVManager.load(edgesCSV);

        if(!edgesReader.isEmpty() && edgesReader.get(0).length != 3)
            throw new Exception("Invalid edges csv file format");

        //Read in all the edges.
        for(String[] currLine : edgesReader)
        {
            final String edgeID = currLine[0]; //Currently unused, but will be taken out by compiler anyways.

            final String startNode = currLine[1];
            final String endNode = currLine[2];

            addVertex(graph, vertices, edgeID, startNode, endNode);
        }
        return graph;

         */
    }

    /**
     * Loads from node/edge entries respectively //FIXME: DO BETTER & METHODZIE
     * @return
     * @author Alex Friedman (ahf)
     * @throws Exception
     */
    public static void load(List<NodeEntry> nodeEntries, List<EdgeEntry> edgeEntries) throws Exception {
      Graph.getGraph().load(nodeEntries, edgeEntries);
    }
}
