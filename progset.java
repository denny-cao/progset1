import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class progset {

    public static class UFTree { //union find data structure

        private int[] parent;
        private int[] rank;

        public UFTree (int vertices) {
            parent = new int[vertices];
            rank = new int[vertices];

            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {return i;} //check if object is parent of itself, in that case return obj
            else {
                int returnFind = find(parent[i]); //recursively find the parent
                parent[i] = returnFind; //caching the value/tree compression
                return returnFind;
            }
        }

        public void union(int s1, int s2) {
            //finding the roots of both sets
            int s1root = find(s1);
            int s2root = find(s2);

            if (rank[s1root] > rank[s2root]) {parent[s2root] = s1root; rank[s1root]++;} //higher rank root becomes parent of the other and ranks update
            else {parent[s1root] = s2root; rank[s2root]++;}
        }
    }

    static class edge {
        int v1, v2;
        double value;

        public edge(int v1, int v2, double value) {
            this.v1 = v1;
            this.v2 = v2;
            this.value = value;
        }
    }


    private static double kruskal(int n, List<edge> edges) {

        List<edge> sortedEdges = edges;
        sortedEdges.sort(new Comparator<edge>() {
            @Override public int compare(edge e1, edge e2) {
                return Double.compare(e1.value, e2.value);
            }
        });

        int i = 0; //iterator for getting edges
        int usedEdges = 0;
        double cost = 0;

        edge MST[] = new edge[n];
        UFTree graph = new UFTree(n);

        while (usedEdges < n-2) {

            edge smallestEdge = edges.get(i); //get the next edge
            int newv1 = graph.find(smallestEdge.v1);
            int newv2 = graph.find(smallestEdge.v2); //get ready to check for cycles

            if (newv1 != newv2) { //cycle check!
                MST[usedEdges] = smallestEdge;
                usedEdges++;
                graph.union(newv1, newv2);
                cost += smallestEdge.value;
            }
            i++;
        }

        double minCost = 0;
        for (int j = 0; j < usedEdges; j++) {
            minCost += MST[j].value;
        }

        return minCost;
    }

    // Threshold function
    public static double euclideanDistance2D(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public static double zeroThreshold(int n) {
        return 2 * Math.log(n) / n;
    }
    public static double twoThreshold(int n) {
        return Math.sqrt(2 * Math.log(n) / n);
    }

    public static void zeroDimension(int n, int trials) {

        double[] trialResults = new double[trials];

        for (int trial = 0; trial < trials; trial++) {
            double threshold = zeroThreshold(n);
            List<edge> graphEdges = new ArrayList<edge>();

            // Generate a complete undirected graph with n vertices... Avoid repeated edges (aka if i,j is in the list, j,i should not be)
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    // Check where we are
                    double weight = Math.random(); // Generate a random weight
                    // Check if weight is less than the threshold
                    if (weight < threshold) {
                        graphEdges.add(new edge(i, j, weight));
                    }
                }
            }
            trialResults[trial] = kruskal(n, graphEdges);
        }
       // Average numpoints numtrials dimension
        double average = 0;
        for (int i = 0; i < trials; i++) {
            average += trialResults[i];
        }
        average = average / trials;
        System.out.println(average + " " + n + " " + trials + " " + 0);
    }

   public static void twoDimension(int n, int trials) {

        double[] trialResults = new double[trials];

       for (int trial = 0; trial < trials; trial++) {
           List<edge> edges = new ArrayList<>();
           double[][] points = new double[n][2];

           for (int i = 0; i < n; i++) {
               points[i][0] = Math.random();
               points[i][1] = Math.random();
           }

           double threshold = twoThreshold(n);
           for (int i = 0; i < n; i++) {
               for (int j = i + 1; j < n; j++) {
                   double distance = euclideanDistance2D(points[i][0], points[i][1], points[j][0], points[j][1]);

                   if (distance < threshold) {
                       edges.add(new edge(i, j, distance));
                   }
               }

           }
           trialResults[trial] = kruskal(n, edges);
       }
       double average = 0;
       for (int i = 0; i < trials; i++) {
           average += trialResults[i];
       }
       average = average / trials;
       System.out.println(average + " " + n + " " + trials + " " + 2);
   }
    public static void main(String[] args) {

        int n = Integer.parseInt(args[1]);
        int trials = Integer.parseInt(args[2]);
        int dim = Integer.parseInt(args[3]);

        if (dim == 0) {
            zeroDimension(n, trials);
        } else if (dim == 2) {
            twoDimension(n, trials);
        }
    }

}
