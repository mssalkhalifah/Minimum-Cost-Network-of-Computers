package com.project.model;

import java.util.*;

public class MyGraph {
    public final int TOTAL_VERTICES;

    private Map<Integer, HashSet<Integer>> hashMap;
    private List<Edge> edgeList;

    public MyGraph(int totalVertices) {
        TOTAL_VERTICES = totalVertices;
        edgeList = new LinkedList<>();
        hashMap = new HashMap<>();

        for (int i = 0; i < TOTAL_VERTICES; i++) {
            hashMap.put(i, new HashSet<>());
        }
    }

    public MyGraph(int totalVertices, List<Edge> edges) {
        TOTAL_VERTICES = totalVertices;
        edgeList = edges;
        hashMap = new HashMap<>();

        for (Edge edge : edges) {
            hashMap.put(edge.source, new HashSet<>(edge.destination));
            hashMap.put(edge.destination, new HashSet<>(edge.source));
        }
    }

    public void addEdge(int src, int dst, int weight) {
        if (!hashMap.get(src).contains(dst) && !hashMap.get(dst).contains(src) && src != dst) {
            edgeList.add(new Edge(src, dst, weight));
            hashMap.get(src).add(dst);
            hashMap.get(dst).add(src);
        } else {
            if (src == dst)
                throw new IllegalArgumentException("Loop Edge");
            throw new IllegalArgumentException("Duplicated edge");
        }
    }

    public ArrayList<Edge> getEdgeList() {
        return new ArrayList<>(edgeList);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("%d Vertices: [0", TOTAL_VERTICES));
        for (int i = 1; i < TOTAL_VERTICES; i++) {
            stringBuilder.append(String.format(", %d", i));
        }
        stringBuilder.append("]");

        stringBuilder.append(String.format("\n%d Edges: {", edgeList.size()));
        for (Edge edge : edgeList) {
            stringBuilder.append(String.format("[%s]", edge));
        }
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
