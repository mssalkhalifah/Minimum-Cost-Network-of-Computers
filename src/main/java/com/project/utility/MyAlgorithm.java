package com.project.utility;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class MyAlgorithm implements Algorithm {
    public enum algorithms {
        KRUSKAL, PRIM
    }

    protected Graph graph;
    protected Edge superComputers;
    protected List<Edge> mstResult;
    protected List<Edge> edgeList;
    protected int originalWeight;

    public Edge getSuperComputers() {
        return superComputers;
    }

    public List<Edge> getMstResult() {
        return mstResult;
    }

    public int getOriginalWeight() {
        return originalWeight;
    }

    @Override
    public void init(Graph graph) {
        this.graph = graph;

        Random random = new Random();
        mstResult = new LinkedList<>();

        edgeList = graph.edges().collect(Collectors.toList());

        int minWeight = 1;
        int maxWeight = 500;
        for (Edge edge : edgeList) {
            int weight = minWeight + random.nextInt(maxWeight);
            edge.setAttribute("weight", weight);
            edge.setAttribute("ui.label", weight);
            edge.setAttribute("ui.style", "fill-color: gray;");
        }

        int randomEdge = random.nextInt(edgeList.size());
        superComputers = edgeList.get(randomEdge);

        originalWeight = (int) superComputers.getAttribute("weight");
        superComputers.setAttribute("weight", 0);
    }
}
