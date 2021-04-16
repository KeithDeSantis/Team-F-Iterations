package edu.wpi.fuchsiafalcons.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to manage reading CSV files.
 * @author Alex Friedman (ahf)
 */
public class CSVManager {

    /**
     * When given a CSV file, this function will read it, make sure it is in the
     * correct format, and then return a List of String[] where:
     * <ol>
     *     <li>Each String[] is of the same length</li>
     *     <li>The elements of each String[] correspond to each field of the corresponding line in the CSV</li>
     *     <li>The first line of the csv (where field names are listed) is NOT returned</li>
     *     <li>List.size() == number of data lines in the CSV (lines excluding the first line)</li>
     * </ol>
     *
     * @param file
     * @return A List of String[] as described above
     * @throws Exception If there was an error in reading or parsing the file.
     * @author Alex Friedman (ahf)
     */
    public static List<String[]> load(File file) throws Exception {
        //Verify that inputs were given for both files.
        if (file == null)
            throw new Exception("File cannot be null!");

        return load(new BufferedReader(new FileReader(file)));
    }

    /**
     * Given a file location (within the resource folder), this function will read the
     * resource as a CSV and return it.
     * @param resource A string representing the CSV's location in the JAR
     * @return A List of String[] as described in load(File);
     * @author Alex Friedman (ahf)
     * @throws Exception
     */
    public static List<String[]> load(String resource) throws Exception {
        if (resource == null)
            throw new Exception("Resource cannot be null!");

        return load(CSVManager.class.getClassLoader().getResourceAsStream(resource));
    }

    /**
     * Given an InputStream, this function will read the
     * resource as a CSV and return it.
     * @param inputStream The inputstream containing the file's data.
     * @return A List of String[] as described in load(File);
     * @author Alex Friedman (ahf)
     * @throws Exception
     */
    public static List<String[]> load(InputStream inputStream) throws Exception {
        if (inputStream == null)
            throw new Exception("InputStream cannot be null!");

        final BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        return load(streamReader);
    }

    /**
     * Given an BufferedReader, this function will read the resource as a CSV and return it.
     * @param fileReader The bufferedreader containing the file's data.
     * @return A List of String[] as described in load(File);
     * @author Alex Friedman (ahf)
     * @throws Exception
     */
    public static List<String[]> load(BufferedReader fileReader) throws Exception {

        //FIXME: Check how errors are handled from this.

        //Stores initial line.
        final String definitions = fileReader.readLine();

        if(definitions == null)
            throw new Exception("File empty");

        final String[] d = definitions.split(",");

        final List<String[]> csvData = new ArrayList<>();

        //Reads in all the vertices.
        String lineStr = null;
        while ((lineStr = fileReader.readLine()) != null) {
            final String[] currLine = lineStr.split(",");

            //FIXME: VERIFY returns out of method.
            if (currLine.length != d.length)
                throw new Exception("nodesCSV file is in incorrect format. Expected 8 values per line and got: " + currLine.length);

            csvData.add(currLine);
        }

        return csvData;
    }

    /**
     * Used to write a CSV file.
     * @param fileName The directory/name of the file.
     * @param csvData The data that the file contains. This is in the same format as load(File), with
     *                the exception that the list provided here should contain the names of each
     *                column.
     * @author Alex Friedman (ahf)
     * @throws Exception
     */
    public static void writeFile(String fileName, List<String[]> csvData) throws Exception {
        if(fileName == null || csvData == null)
            throw new Exception("Could not write null data/file.");

        final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for(String[] line : csvData)
        {
            for(int i = 0; i < line.length; i++)
            {
                writer.write(line[i]);

                if(i + 1 < line.length)
                    writer.write(", ");
                else
                    writer.write("\n");
            }
        }
        writer.close();
    }
}
