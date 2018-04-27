import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * The main class.</br>
 * <ul>
 *     <li>Builds the <code>Graph</code>.</li>
 *     <li>Finds the taxi routes.</li>
 *     <li>Makes the kml file.</li>
 * </ul>
 */
public class TaxiFinder {

    public static void main(String[] args) {

        Graph graph = new Graph(args[0], args[1], args[2]);
        aStarSolver solver = new aStarSolver(args[3]);
        TreeMap<State, Taxi> results = solver.solve(graph);
        //System.out.println("Found routes");

        kml(args[4], results);

    }

    private static void kml(String filename, TreeMap<State, Taxi> results) {

        Random rand = new Random();
        Color green = Color.GREEN.darker(), color;
        PrintWriter writer = null;

        try {

            writer = new PrintWriter(filename);
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
            writer.println("<Document>");
            writer.println("<name>Taxi Routes</name>");
            writer.println("<Style id=\"green\">");
            writer.println("<LineStyle>");
            writer.println("<color>" + Integer.toHexString(green.getRGB()) + "</color>");
            writer.println("<width>4</width>");
            writer.println("</LineStyle>");
            writer.println("</Style>");
            int i = 1;
            /**
             *  Make random colors for the
             *  rest of the taxi routes.
             *  Don't make it green.
             */
            while (i < results.size()) {

                int r = rand.nextInt(255);
                int g = rand.nextInt(127);
                int b = rand.nextInt(255);
                color = new Color(r, g, b);

                if (color.getRGB() == green.getRGB())
                    continue;

                writer.println("<Style id=\"taxi" + i + "\">");
                writer.println("<LineStyle>");
                writer.println("<color>" + Integer.toHexString(color.getRGB()) + "</color>");
                writer.println("<width>4</width>");
                writer.println("</LineStyle>");
                writer.println("</Style>");

                i++;

            }
            State current = results.firstEntry().getKey();
            Taxi taxi = results.firstEntry().getValue();
            System.out.println(current.getCost());
            System.out.println(taxi.getID());
            results.pollFirstEntry();
            /**
             *  This is to make the client point
             */
            writer.println("<Placemark>");
            writer.println("<name>Client</name>");
            writer.println("<Point>");
            writer.println("<coordinates>");
            writer.println(current.getCoordinates());
            writer.println("</coordinates>");
            writer.println("</Point>");
            writer.println("</Placemark>");
            /**
             *  This is to make the first route
             *  have green color.
             */
            writer.println("<Placemark>");
            writer.println("<name>TaxiID " + taxi.getID() + "</name>");
            writer.println("<styleUrl>#green</styleUrl>");
            writer.println("<LineString>");
            writer.println("<altitudeMode>relative</altitudeMode>");
            writer.println("<coordinates>");
            writer.println(current.getCoordinates());
            /**
             *  Stop before null to put a
             *  taxi point on map too.
             */
            while (current.getPrevious() != null) {
                current = current.getPrevious();
                writer.println(current.getCoordinates());
            }
            /**
             *  Draw last coordinate and
             *  make point.
             */
            writer.println(current.getCoordinates());
            writer.println("</coordinates>");
            writer.println("</LineString>");
            writer.println("</Placemark>");
            writer.println("<Placemark>");
            writer.println("<name>TaxiID " + taxi.getID() + "</name>");
            writer.println("<Point>");
            writer.println("<coordinates>");
            writer.println(current.getCoordinates());
            writer.println("</coordinates>");
            writer.println("</Point>");
            writer.println("</Placemark>");

            i = 1;
            /**
             *  Do the same thing for all the rest
             *  of the taxi routes.
             */
            while (!results.isEmpty()) {

                current = results.firstEntry().getKey();
                taxi = results.firstEntry().getValue();
                System.out.println(current.getCost());
                System.out.println(taxi.getID());
                results.pollFirstEntry();
                writer.println("<Placemark>");
                writer.println("<name>TaxiID " + taxi.getID() + "</name>");
                writer.println("<styleUrl>#taxi" + i + "</styleUrl>");
                writer.println("<LineString>");
                writer.println("<altitudeMode>relative</altitudeMode>");
                writer.println("<coordinates>");
                writer.println(current.getCoordinates());
                while (current.getPrevious() != null) {
                    current = current.getPrevious();
                    writer.println(current.getCoordinates());
                }
                writer.println(current.getCoordinates());
                writer.println("</coordinates>");
                writer.println("</LineString>");
                writer.println("</Placemark>");
                writer.println("<Placemark>");
                writer.println("<name>TaxiID " + taxi.getID() + "</name>");
                writer.println("<Point>");
                writer.println("<coordinates>");
                writer.println(current.getCoordinates());
                writer.println("</coordinates>");
                writer.println("</Point>");
                writer.println("</Placemark>");

                i++;

            }

            writer.println("</Document>");
            writer.println("</kml>");

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } finally {
            if (writer != null)
                writer.close();
        }


    }

}
