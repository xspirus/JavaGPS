import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

    /**
     * Main.
     * Displays times of each stage as well.
     * @param args The arguments.
     */
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        Graph<Coordinates> graph = new Graph<>(args[0], args[1], args[2], new Coordinates());
        long graphTime = System.currentTimeMillis();
        AStar<Coordinates> astar = new AStar<>();
        Map<Graph.Vertex<Coordinates>, List<Graph.Vertex<Coordinates>>> results = astar.solve(graph, args[3]);
        long astarTime = System.currentTimeMillis();
        kml(results, args[3]);
        long kmlTime = System.currentTimeMillis();

        System.out.println("Preprocessing\t: " + (graphTime - startTime) / 1000.0);
        System.out.println("A* Algorithm\t: " + (astarTime - graphTime) / 1000.0);
        System.out.println("KML file\t: " + (kmlTime - astarTime) / 1000.0);
        System.out.println("Total Time\t: " + (kmlTime - startTime) / 1000.0);

    }

    /**
     * Constructs the kml file.
     * We suppose we have the results sorted in a <code>TreeMap</code>
     * based on the total cost in kilometres. The map consists
     * of an <code>ArrayList</code> of <code>Coordinates</code>.
     * @param result The results of the A* for each taxi.
     * @param arg The capacity of the openSet given by the user.
     */
    private static void kml(Map<Graph.Vertex<Coordinates>, List<Graph.Vertex<Coordinates>>> result, String arg) {

        Random rand = new Random();
        Color green = Color.GREEN.darker(), color;
        PrintWriter writer = null;
        TreeMap<Graph.Vertex<Coordinates>, List<Graph.Vertex<Coordinates>>> results = (TreeMap<Graph.Vertex<Coordinates>, List<Graph.Vertex<Coordinates>>>) result;
        int capacity = Integer.parseInt(arg);
        String file = "kml" + capacity + ".kml";

        try {

            writer = new PrintWriter(file);
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
            writer.println("<Document>");
            writer.println("<name>Taxi Routes " + capacity + "</name>");
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
            Graph.Vertex<Coordinates> current = results.firstEntry().getKey();
            ArrayList<Graph.Vertex<Coordinates>> coordinates = (ArrayList<Graph.Vertex<Coordinates>>)results.firstEntry().getValue();
            results.pollFirstEntry();
            /**
             *  This is to make the client point
             */
            writer.println("<Placemark>");
            writer.println("<name>Client</name>");
            writer.println("<Point>");
            writer.println("<coordinates>");
            writer.println(coordinates.get(0).getLocation());
            writer.println("</coordinates>");
            writer.println("</Point>");
            writer.println("</Placemark>");
            /**
             *  This is to make the first route
             *  have green color.
             */
            writer.println("<Placemark>");
            writer.println("<name>TaxiID " + current.getId() + "</name>");
            writer.println("<styleUrl>#green</styleUrl>");
            writer.println("<LineString>");
            writer.println("<altitudeMode>relative</altitudeMode>");
            writer.println("<coordinates>");
            for (Graph.Vertex<Coordinates> co : coordinates)
                writer.println(co.getLocation());
            writer.println("</coordinates>");
            writer.println("</LineString>");
            writer.println("</Placemark>");
            writer.println("<Placemark>");
            writer.println("<name>TaxiID " + current.getId() + "</name>");
            writer.println("<Point>");
            writer.println("<coordinates>");
            writer.println(current.getLocation());
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
                coordinates = (ArrayList<Graph.Vertex<Coordinates>>)results.firstEntry().getValue();
                results.pollFirstEntry();
                writer.println("<Placemark>");
                writer.println("<name>TaxiID " + current.getId() + "</name>");
                writer.println("<styleUrl>#taxi" + i + "</styleUrl>");
                writer.println("<LineString>");
                writer.println("<altitudeMode>relative</altitudeMode>");
                writer.println("<coordinates>");
                for (Graph.Vertex<Coordinates> co : coordinates)
                    writer.println(co.getLocation());
                writer.println("</coordinates>");
                writer.println("</LineString>");
                writer.println("</Placemark>");
                writer.println("<Placemark>");
                writer.println("<name>TaxiID " + current.getId() + "</name>");
                writer.println("<Point>");
                writer.println("<coordinates>");
                writer.println(current.getLocation());
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
