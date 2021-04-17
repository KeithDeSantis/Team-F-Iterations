package edu.wpi.fuchsiafalcons.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test the CSVReader.
 * @author Alex Friedman (ahfriedman)
 * @see CSVManager
 */
public class CSVManagerTest {

  /** Setup test suite. */
  @BeforeAll
  public static void setup() { }

  @AfterAll
  public static void cleanup() {}

  @Test
  /**
   * This is used to verify that we throw errors when given null files.
   * @author Alex Friedman (ahf)
   */
  public void testNullFile() {
    //ahf - tests that we get an error when providing a null file.
    //FIXME: DO BETTER (EXPLICIT ERROR CLASSES)
    assertThrows(Exception.class, () -> CSVManager.load((BufferedReader) null));
  }

  @Test
  /**
   * This is used to make sure that we throw errors when given a file that doesn't exist.
   * @author Alex Friedman.
   */
  public void testNonexistentFile() {
    //ahf - tests what would happen if we request a file that DNE.
    final File f = new File("Z:/this/file/doesnt/exist.csv");
    assertThrows(Exception.class, () -> CSVManager.load(f));
  }

  @Test
  /**
   * This is used to test that we throw an exception when reading a CSV of a bad
   * format.
   * @author Alex Friedman (ahf)
   */
  public void testBadFormatCSV() throws IOException {
    //ahf - tests reading a CSV of a bad file format.

    //Create the temp file
    final File temp = File.createTempFile("badFormat", "csv");
    temp.deleteOnExit();

    final String testCSV = "a,b\n" +
                           "0,1\n" +
                           "0\n" +
                           "1,0\n";

    Files.write(temp.toPath(), testCSV.getBytes(StandardCharsets.UTF_8));

    assertThrows(Exception.class, () -> CSVManager.load(temp));
  }

  @Test
  /**
   * This is used to make sure that when given a proper CSV file, we parse it correctly.
   * @author Alex Friedman.
   */
  public void testValidFile() throws Exception {
    //ahf - tests reading a CSV correctly

    //Create the temp file
    final File temp = File.createTempFile("goodFormat", "csv");
    temp.deleteOnExit();

    final String testCSV = "a,b\n" +
            "0,1\n" +
            "2,3\n" +
            "4,5\n";

    Files.write(temp.toPath(), testCSV.getBytes(StandardCharsets.UTF_8));

    //Verify no exceptions
    assertDoesNotThrow(() -> CSVManager.load(temp));

    final List<String[]> data = CSVManager.load(temp);

    assertEquals(data.size(), 3);

    assertEquals(data.get(0).length, 2);
    assertEquals(data.get(1).length, 2);
    assertEquals(data.get(2).length, 2);

    assertEquals(data.get(0)[0], "0");
    assertEquals(data.get(0)[1], "1");

    assertEquals(data.get(1)[0], "2");
    assertEquals(data.get(1)[1], "3");

    assertEquals(data.get(2)[0], "4");
    assertEquals(data.get(2)[1], "5");
  }

  @Test
  public void testGetJarResource() throws Exception {
    //ahf - this tests if we are able to read files from our resources folder correctly (we've already tested regular files)

    System.out.println((new File("")).getAbsolutePath());

    final List<String[]> regFile = CSVManager.load(new File("./src/main/resources/L1Edges.csv"));
    final List<String[]> jarFile = CSVManager.load("L1Edges.csv");

    assertNotNull(regFile);
    assertNotNull(jarFile);

    assertEquals(regFile.size(), jarFile.size());

    for(int i = 0; i < regFile.size(); i++)
    {
      assertArrayEquals(regFile.get(i), jarFile.get(i));
    }
  }
}
