package com.project.utility;

import org.graphstream.graph.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphBenchmarkWorker {
    private List<List<Double>> benchmarkLists;
    private List<List<Graph>> graphLists;

    private MyAlgorithm.algorithms algorithms;

    public void init(List<List<Graph>> graphList, MyAlgorithm.algorithms algorithms) {
        this.graphLists = graphList;
        this.algorithms = algorithms;

        benchmarkLists = new LinkedList<>();
    }

    public List<List<Double>> getBenchmarkLists() {
        return benchmarkLists;
    }

    public void compute() {
        List<Double> runTimes = new LinkedList<>();
        MyAlgorithm myAlgorithm;

        for (List<Graph> graphList : graphLists) {

            // Warm-up
            for (int i = 0; i < 1000; i++) {
                for (Graph graph : graphList) {
                    myAlgorithm = setup(algorithms);
                    myAlgorithm.init(graph);
                    myAlgorithm.compute();
                }
            }

            myAlgorithm = setup(algorithms);

            // Real test
            for (Graph graph : graphList) {
                myAlgorithm.init(graph);

                double startTime = System.nanoTime();
                myAlgorithm.compute();
                double endTime = System.nanoTime();

                runTimes.add((endTime - startTime) /1000000);

                myAlgorithm.getMstResult()
                        .forEach(edge -> edge.setAttribute("ui.style", "fill-color: blue; size: 5px;"));

                myAlgorithm.getSuperComputers().setAttribute("ui.style", "fill-color: red; size: 4px;");
                myAlgorithm.getSuperComputers().getNode0().setAttribute("ui.class", "superComputer");
                myAlgorithm.getSuperComputers().getNode1().setAttribute("ui.class", "superComputer");
                myAlgorithm.getSuperComputers().setAttribute("weight", myAlgorithm.getOriginalWeight());
            }

            benchmarkLists.add(new ArrayList<>(runTimes));
            runTimes.clear();
        }
    }

    private MyAlgorithm setup(MyAlgorithm.algorithms algorithms) {
        return (algorithms == MyAlgorithm.algorithms.KRUSKAL)
                ? new MinimumSpanningTreeKruskal()
                : new MinimumSpanningTreePrim();
    }
}
