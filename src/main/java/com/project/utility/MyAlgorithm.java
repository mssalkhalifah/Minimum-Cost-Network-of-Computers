package com.project.utility;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.util.List;

public abstract class MyAlgorithm implements Algorithm {
    public enum algorithms {
        KRUSKAL, PRIM
    }

    protected Graph graph;
    protected Edge superComputers;
    protected List<Edge> mstResult;
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
    }
}
