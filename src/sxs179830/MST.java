/**
 * Starter code for MST Algorithms
 * @author Shiva Prasad Reddy Bitla (sxb180066)
 * @author Sudeep Maity (sdm170530)
 * @author Saurav Sharma (sxs179830)
 */

package sxs179830;

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;
import sxs179830.BinaryHeap.Index;
import sxs179830.BinaryHeap.IndexedHeap;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;

    private static final int INFINITY = Integer.MAX_VALUE;

    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {

        boolean seen;
        Vertex parent;
        int index;
        int distance;

        MSTVertex(Vertex u) {
            seen = false;
            parent = null;
        }

        MSTVertex(MSTVertex u) {  // for prim2
            seen = false;
            parent = null;
            distance = INFINITY;
        }

        public MSTVertex make(Vertex u) { return new MSTVertex(u); }

        public void putIndex(int index) { index = index;}

        public int getIndex() { return index; }

        public int compareTo(MSTVertex other) {
            return 0;
        }
    }

    // getter and setter methods to retrieve and update vertex properties
    public boolean getSeen(Vertex u) {
        return get(u).seen;
    }

    public void setSeen(Vertex u, boolean value) {
        get(u).seen = value;
    }

    public Vertex getParent(Vertex u) {
        return get(u).parent;
    }

    public void setParent(Vertex u, Vertex p) {
        get(u).parent = p;
    }

    public int getDistance(Vertex u) {
        return get(u).distance;
    }

    public void setDistance(Vertex u, int distance) {
        get(u).distance = distance;
    }

    public void initialize() {
        for(Vertex u: g) {
            setSeen(u, false);
            setParent(u, null);
            setDistance(u, INFINITY);
        }
    }

    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }

    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        return wmst;
    }

    /**
     *
     * @param s Source Vertex to run Prims II Algorithm
     * @return weight of minimum spanning tree
     */
    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<MSTVertex> q = new PriorityQueue<>();

        return wmst;
    }

    /**
     *
     * @param s Source Vertex to run Prims I Algorithm
     * @return weight of minimum spanning tree
     */
    public long prim1(Vertex s) {

        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();
        Set<Vertex> remainingVertex = new HashSet<>();
        Edge edge;
        Vertex fromVertex, toVertex;
        for(Vertex v : g.getVertexArray()) {
            remainingVertex.add(v);
        }
        initialize();
        for(Edge e : g.incident(s)) {
            q.add(e);
        }

        while(!q.isEmpty()) {
            edge = q.remove();
            fromVertex = edge.fromVertex();
            toVertex = edge.toVertex();
            setSeen(fromVertex, true);
            if(!getSeen(toVertex)) {
                setParent(toVertex, fromVertex);
                if(remainingVertex.contains(toVertex)) {
                    mst.add(edge);
                    wmst += edge.getWeight();
                    remainingVertex.remove(toVertex);
                }
                for(Edge e2 : g.incident(toVertex)) {
                    if(!getSeen(e2.otherEnd(toVertex))) {
                        q.add(e2);
                    }
                }
            }
        }

        return wmst;
    }

    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch(choice) {
            case 0:
                m.kruskal();
                break;
            case 1:
                m.prim1(s);
                break;
            case 2:
                m.prim2(s);
                break;
            default:
                m.prim3(s);
                break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice = 2;  // Kruskal
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) { choice = Integer.parseInt(args[1]); }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, choice);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
}
