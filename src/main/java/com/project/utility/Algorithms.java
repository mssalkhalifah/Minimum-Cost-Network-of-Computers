package com.project.utility;

import com.project.model.Edge;
import com.project.model.MyGraph;
import com.project.model.Subset;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.FibonacciHeap;

import java.util.*;

public class Algorithms {
    private static final int MAX_WEIGHT = 1000;
    private static final int MIN_WEIGHT = 1;

    public static MyGraph generateGraph(int numberOfVector) {
        MyGraph resultGraph = new MyGraph(numberOfVector);

        Random random = new Random();

        resultGraph.addEdge(0, 1, MIN_WEIGHT + random.nextInt(MAX_WEIGHT));
        for (int i = 2; i < numberOfVector; i++) {
            int randomWeight = MIN_WEIGHT + random.nextInt(MAX_WEIGHT);
            int randomSource = random.nextInt(i);
            resultGraph.addEdge(randomSource, i, randomWeight);
        }

        int maxEdge = (int) Math.ceil((((double) numberOfVector * (numberOfVector - 1) / 2) ) / numberOfVector);
        for (int i = 0; i < maxEdge; i++) {
            int src = random.nextInt(numberOfVector);
            int dst = random.nextInt(numberOfVector);

            if (src == dst) {
                if (dst == numberOfVector - 1) {
                    --dst;
                } else {
                    ++dst;
                }
            }

            if (!resultGraph.getGraph().get(src).containsKey(dst)) {
                resultGraph.addEdge(src, dst, MIN_WEIGHT + random.nextInt(MAX_WEIGHT));
            } else {
                i--;
            }
        }

        return resultGraph;
    }

    public static MyGraph kruskal(MyGraph myGraph) {
        LinkedList<Edge> mstResult = new LinkedList<>();
        List<Edge> edgeList = myGraph.getEdgeList();

        Collections.sort(edgeList);

        Subset[] subsets = new Subset[myGraph.TOTAL_VERTICES];

        for (int i = 0; i < subsets.length; i++) {
            subsets[i] = new Subset();
            subsets[i].setParent(i);
            subsets[i].setRank(0);
        }

        for (Edge edge : edgeList) {
            int x = SetUtil.find(subsets, edge.getSource());
            int y = SetUtil.find(subsets, edge.getDestination());

            if (x != y) {
                mstResult.add(edge);
                SetUtil.union(subsets, x, y);
            }
        }

        return new MyGraph(myGraph.TOTAL_VERTICES, mstResult);
    }

    public static MyGraph prim(MyGraph myGraph) {
        boolean[] mstIncluded = new boolean[myGraph.TOTAL_VERTICES];
        int[] parent = new int[myGraph.TOTAL_VERTICES];
        MyGraph mst = new MyGraph(myGraph.TOTAL_VERTICES);
        FibonacciHeap<Double, Integer> fibonacciHeap = new FibonacciHeap<>(); // Value, Key
        Map<Integer, AddressableHeap.Handle<Double, Integer>> entries = new HashMap<>(); // Key, (Value, key)

        for (int i = 0; i < myGraph.TOTAL_VERTICES; i++) {
            parent[i] = -1;
            mstIncluded[i] = false;
            entries.put(i, fibonacciHeap.insert(Double.POSITIVE_INFINITY, i));
        }

        mstIncluded[0] = true;
        entries.get(0).decreaseKey(0.0);

        while (!fibonacciHeap.isEmpty()) {
            AddressableHeap.Handle<Double, Integer> entry = fibonacciHeap.deleteMin();
            mstIncluded[entry.getValue()] = true;

            for (Map.Entry<Integer, Integer> arc : myGraph.getGraph().get(entry.getValue()).entrySet()) {
                if (!mstIncluded[arc.getKey()]) {
                    if (entries.get(arc.getKey()).getKey() > arc.getValue()) {
                        entries.get(arc.getKey()).decreaseKey((double) arc.getValue());
                        parent[arc.getKey()] = entry.getValue();
                    }
                }
            }
        }

        for (int i = 1; i < myGraph.TOTAL_VERTICES; i++) {
            mst.addEdge(parent[i], i, myGraph.getGraph().get(parent[i]).get(i));
        }

        return mst;
    }
}
