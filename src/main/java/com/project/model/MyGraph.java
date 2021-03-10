package com.project.model;

import java.util.*;

public class MyGraph {
    public final int TOTAL_VERTICES;

    private final HashMap<Integer, HashMap<Integer, Integer>> graph;
    private final List<Edge> edgeList;

    public MyGraph(int totalVertices) {
        TOTAL_VERTICES = totalVertices;
        edgeList = new LinkedList<>();
        graph = new HashMap<>();

        for (int i = 0; i < totalVertices; i++) {
            graph.put(i, new HashMap<>());
        }
    }

    public MyGraph(int totalVertices, List<Edge> edges) {
        TOTAL_VERTICES = totalVertices;
        edgeList = edges;
        graph = new HashMap<>();

        for (int i = 0; i < totalVertices; i++) {
            graph.put(i, new HashMap<>());
        }

        for (Edge edge : edges) {
            graph.get(edge.getSource()).put(edge.getDestination(), edge.getWeight());
            graph.get(edge.getDestination()).put(edge.getSource(), edge.getWeight());
        }
    }

    public void addEdge(int src, int dst, int weight) {
        if (!graph.get(src).containsKey(dst) && !graph.get(dst).containsKey(src) && src != dst) {
            edgeList.add(new Edge(src, dst, weight));
            graph.get(src).put(dst, weight);
            graph.get(dst).put(src, weight);
        } else {
            if (src == dst)
                throw new IllegalArgumentException("Loop Edge");
            throw new IllegalArgumentException("Duplicated edge");
        }
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getGraph() {
        return graph;
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
