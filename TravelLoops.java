import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 
class Graph
{
    private int V;   
    private LinkedList<Integer> adj[];
    private Map<String, Integer> nodeNamesToNodeNumbers;
 
    Graph(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }

    Graph(String[] nodeNames)
    {
        nodeNamesToNodeNumbers = new HashMap<String, Integer>();

        int nodeCounter=0;
        for(String nodeName : nodeNames)
        {
            if(!nodeNamesToNodeNumbers.containsKey(nodeName))
            {
                nodeNamesToNodeNumbers.put(nodeName, nodeCounter);
                nodeCounter = nodeCounter+1;
            }
        }
        V = nodeCounter;
        adj = new LinkedList[V];
        for (int i=0; i<V; ++i)
            adj[i] = new LinkedList();
    }

    void addEdge(String fromEdge, String toEdge)
    {
        addEdge(nodeNamesToNodeNumbers.get(fromEdge), nodeNamesToNodeNumbers.get(toEdge));
    }
 
    void addEdge(int v, int w)  
    { 
        adj[v].add(w); 
    }
 
    // A recursive function to print DFS starting from v
    void DFSUtil(int v,boolean visited[])
    {
        // Mark the current node as visited and print it
        visited[v] = true;
 
        int n;
 
        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i =adj[v].iterator();
        while (i.hasNext())
        {
            n = i.next();
            if (!visited[n])
                DFSUtil(n,visited);
        }
    }
 
    // Function that returns reverse (or transpose) of this graph
    Graph getTranspose()
    {
        Graph g = new Graph(V);
        for (int v = 0; v < V; v++)
        {
            // Recur for all the vertices adjacent to this vertex
            Iterator<Integer> i =adj[v].listIterator();
            while(i.hasNext())
                g.adj[i.next()].add(v);
        }
        return g;
    }
 
    void fillOrder(int v, boolean visited[], Stack stack)
    {
        // Mark the current node as visited and print it
        visited[v] = true;
 
        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext())
        {
            int n = i.next();
            if(!visited[n])
                fillOrder(n, visited, stack);
        }
 
        // All vertices reachable from v are processed by now,
        // push v to Stack
        stack.push(new Integer(v));
    }
 
    // The main function that finds and prints all strongly
    // connected components
    void printNumberOfTravelLoops()
    {
        Stack stack = new Stack();
 
        // Mark all the vertices as not visited (For first DFS)
        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;
 
        // Fill vertices in stack according to their finishing
        // times
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                fillOrder(i, visited, stack);
 
        // Create a reversed graph
        Graph gr = getTranspose();
 
        // Mark all the vertices as not visited (For second DFS)
        for (int i = 0; i < V; i++)
            visited[i] = false;
 
        // Now process all vertices in order defined by Stack
        int travelLoops=0;
        while (stack.empty() == false)
        {
            // Pop a vertex from stack
            int v = (int)stack.pop();
 
            // Print Strongly connected component of the popped vertex
            if (visited[v] == false)
            {
                gr.DFSUtil(v, visited);
                travelLoops=travelLoops+1;
            }
        }
        System.out.println("Number of travel loops is "+travelLoops);


    }
 
}

public class TravelLoops
{

    public static void main(String args[]) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get("input.txt")); 
        /*
         * Sample input.txt - first line has number of test cases
         * Subsequent lines show the travel
         */

        int numberOfTestCases = Integer.parseInt(lines.get(0));

        for(int i=1;i<=numberOfTestCases;i++)
        {

            String[] travels = lines.get(i).split("->");

            Graph g = new Graph(travels);
            int numberOfTravels = travels.length;

            for(int j=0;j<numberOfTravels-1;j++)
            {
                g.addEdge(travels[j], travels[j+1]);
            }

            g.printNumberOfTravelLoops();
        }
    }
}
