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
        MSTVertex parent;
        Vertex self;
        int index;
        int distance;
        int rank;

        MSTVertex(Vertex u) {
            seen = false;
            parent = this;
            distance = INFINITY;
            self = u;
            rank = 0;
        }

        MSTVertex(MSTVertex u) {  // for prim2
            seen = false;
            parent = null;
            distance = INFINITY;
            rank = 0;
        }

        public MSTVertex make(Vertex u) { return new MSTVertex(u); }

        public MSTVertex find() {
            if(!this.equals(parent)) {
                parent = parent.find();
            }
            return parent;
        }

        public void union(MSTVertex rv) {
            if(this.getRank() > rv.getRank()) {
                rv.setParent(this);
            } else if(this.getRank() < rv.getRank()) {
                this.setParent(rv);
            } else {
                this.setRank(this.rank++);
                rv.setParent(this);
            }

        }

        public void putIndex(int index) { this.index = index;}

        public int getIndex() { return index; }

        public int compareTo(MSTVertex other) {
            // To-do
            return 0;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public boolean isSeen() {
            return seen;
        }

        public void setSeen(boolean seen) {
            this.seen = seen;
        }

        public MSTVertex getParent() {
            return parent;
        }

        public void setParent(MSTVertex parent) {
            this.parent = parent;
        }

        public Vertex getSelf() {
            return self;
        }

        public void setSelf(Vertex u) {
            this.self = u;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    // getter and setter methods to retrieve and update vertex properties
    public boolean getSeen(Vertex u) {
        return get(u).isSeen();
    }

    public void setSeen(Vertex u, boolean value) {
        get(u).setSeen(value);
    }

    public MSTVertex getParent(Vertex u) {
        return get(u).getParent();
    }

    public void setParent(Vertex u, MSTVertex p) {
        get(u).setParent(p);
    }

    public int getDistance(Vertex u) {
        return get(u).getDistance();
    }

    public void setDistance(Vertex u, int distance) {
        get(u).setDistance(distance);
    }

    public Vertex getSelf(Vertex u) {
        return get(u).getSelf();
    }

    public void setSelf(Vertex u) {
        get(u).setSelf(u);
    }

    public void initialize() {
        for(Vertex u: g) {
            setSeen(u, false);
            setParent(u, null);
            setDistance(u, INFINITY);
            setSelf(u);
        }
    }

    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        MSTVertex ru, rv;
        Arrays.sort(edgeArray);

        for(Edge e : edgeArray) {
            ru = get(e.fromVertex()).find();
            rv = get(e.toVertex()).find();
            if(!ru.equals(rv)) {
                mst.add(e);
                ru.union(rv);
                wmst += e.getWeight();
            }
        }

        return wmst;
    }

    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        MSTVertex u, otherEnd;
        initialize();
        for(Vertex v : g.getVertexArray()) {
            q.add(get(v));
        }
        get(s).setDistance(0);

        while(!q.isEmpty()) {
            u = q.remove();
            u.setSeen(true);
            wmst += u.getDistance();

            for(Edge e : g.incident(u.getSelf())) {
                otherEnd = get(e.otherEnd(u.getSelf()));
                if(!otherEnd.isSeen() && e.getWeight() < otherEnd.getDistance()) {
                    otherEnd.setDistance(e.getWeight());
                    otherEnd.setParent(u);
                    q.decreaseKey(otherEnd);
                }
            }
        }
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
        Set<Vertex> remainingVertex = new HashSet<>();
        MSTVertex u;
        Collections.addAll(remainingVertex, g.getVertexArray());
        initialize();
        get(s).setDistance(0);
        q.add(get(s));

        while(!q.isEmpty()) {
            u = q.remove();
            if(!u.isSeen()) {
                u.setSeen(true);
                if(remainingVertex.contains(u.getSelf())) {
                    wmst += u.getDistance();
                    remainingVertex.remove(u.getSelf());
                }
                for(Edge e: g.incident(u.getSelf())) {
                    MSTVertex otherEnd = get(e.otherEnd(u.getSelf()));
                    if(!otherEnd.isSeen() && e.getWeight() < otherEnd.getDistance()) {
                        otherEnd.setDistance(e.getWeight());
                        otherEnd.setParent(u);
                        q.add(otherEnd);
                    }
                }
            }
        }
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
        Collections.addAll(remainingVertex, g.getVertexArray());
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
                setParent(toVertex, get(fromVertex));
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
        int choice = 0;  // Kruskal
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) { choice = Integer.parseInt(args[1]); }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        while(choice < 4) {
            Timer timer = new Timer();
            MST m = mst(g, s, choice);
            System.out.println(m.algorithm + "\n" + m.wmst);
            System.out.println(timer.end());
            choice++;
            System.out.println();
        }
    }
}
