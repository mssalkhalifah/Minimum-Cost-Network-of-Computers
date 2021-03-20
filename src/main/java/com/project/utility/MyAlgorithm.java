package com.project.utility;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;

public abstract class MyAlgorithm implements Algorithm {
    protected Graph graph;

    protected abstract void computeBody();

    @Override
    public void init(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void compute() {
        computeBody();
    }
}
