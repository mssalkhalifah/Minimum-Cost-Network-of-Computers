package com.project.utility;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class MyGraphGenerator extends SwingWorker<Graph, Void> {
    private Graph graph;
    private final Generator generator;

    private final int NUMBER_OF_VERTICES;

    public MyGraphGenerator(int numberOfVertices) {
        this.NUMBER_OF_VERTICES = numberOfVertices;
        this.graph = new SingleGraph("Main Graph");
        this.generator = new DorogovtsevMendesGenerator();
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    protected Graph doInBackground() {
        generator.addSink(graph);

        generator.begin();
        for (int i = 0; i < NUMBER_OF_VERTICES - 3; i++) {
            generator.nextEvents();
        }
        generator.end();

        graph.setAttribute("ui.stylesheet",
                "node {fill-mode: dyn-plain; fill-color: black, red;}" +
                        "edge {fill-mode: dyn-plain; fill-color: black, blue;}");

        return graph;
    }

    @Override
    protected void done() {
        try {
            graph = get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
