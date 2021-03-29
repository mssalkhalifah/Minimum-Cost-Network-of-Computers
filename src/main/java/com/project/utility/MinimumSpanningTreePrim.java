package com.project.utility;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.FibonacciHeap;

import java.util.*;

public class MinimumSpanningTreePrim extends MyAlgorithm {

    @Override
    public void compute() throws IndexOutOfBoundsException {
        boolean[] mstIncluded = new boolean[graph.getNodeCount()];
        int[] parent = new int[graph.getNodeCount()];
        FibonacciHeap<Double, Integer> fibonacciHeap = new FibonacciHeap<>(); // Value, Key
        Map<Integer, AddressableHeap.Handle<Double, Integer>> entries = new HashMap<>(); // Key, (Value, key)

        for (int i = 0; i < graph.getNodeCount(); i++) {
            int nodeIndex = graph.getNode(i).getIndex();
            parent[nodeIndex] = -1;
            mstIncluded[nodeIndex] = false;
            entries.put(nodeIndex, fibonacciHeap.insert(Double.POSITIVE_INFINITY, nodeIndex));
        }

        int nodeIndex = graph.getNode(0).getIndex();
        mstIncluded[nodeIndex] = true;
        entries.get(nodeIndex).decreaseKey(0.0);

        while (!fibonacciHeap.isEmpty()) {
            AddressableHeap.Handle<Double, Integer> entry = fibonacciHeap.deleteMin();
            mstIncluded[entry.getValue()] = true;

            graph.getNode(entry.getValue()).neighborNodes().forEach(node -> {
                if (!mstIncluded[node.getIndex()]) {
                    double otherNodeWeight = (int) node.getEdgeFrom(entry.getValue()).getAttribute("weight");

                    if (entries.get(node.getIndex()).getKey() > otherNodeWeight) {
                        entries.get(node.getIndex()).decreaseKey(otherNodeWeight);
                        parent[node.getIndex()] = entry.getValue();
                    }
                }
            });
        }

        for (int i = 1; i < graph.getNodeCount(); i++) {
            mstResult.add(graph.getNode(parent[i]).getEdgeToward(i));
        }
    }
}
