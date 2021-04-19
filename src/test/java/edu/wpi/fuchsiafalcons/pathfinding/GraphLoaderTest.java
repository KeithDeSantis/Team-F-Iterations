package edu.wpi.fuchsiafalcons.pathfinding;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class is used to test the GraphLoader.
 * @author Alex Friedman (ahf)
 */
public class GraphLoaderTest {

    /** Setup test suite. */
    @BeforeAll
    public static void setup() { }

    @AfterAll
    public static void cleanup() { }


    /**
     * This makes sure that we get an exception if we try to load a graph from a bad file which,
     * in this case, is null.
     * @author Alex Friedman (ahf)
     */
    @Test
    public void testNullFiles()
    {
        //ahf - tests that we throw exceptions when loading null files.
        assertThrows(Exception.class, () -> GraphLoader.load((String) null, null));
        //We rely on CSVReader, so as long as we know that we can catch one bad CSV, we're good.
    }


    /**
     * This tests that we only accept files in the correct format.
     * @author Alex Friedman (ahf)
     */
    @Test
    public void testFileFormat()
    {
        //ahf - tests the format of the files that we read.

        final String edgesCSV = "MapfAlledges.csv";
        final String nodesCSV = "MapfAllnodes.csv";

        //Here we provide incorrect file dimensions, but valid files
        assertThrows(Exception.class, () -> GraphLoader.load(edgesCSV, edgesCSV));
        assertThrows(Exception.class, () -> GraphLoader.load(edgesCSV, nodesCSV));
        assertThrows(Exception.class, () -> GraphLoader.load(nodesCSV, nodesCSV));

        assertDoesNotThrow(() -> GraphLoader.load(nodesCSV, edgesCSV));

        //FIXME: DO MORE, BETTER TESTS
    }
}
