package com.project.utility;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

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
        MyAlgorithm myAlgorithm = null;

        for (int i = 0; i < 1000; i++) {
            myAlgorithm = (algorithms == MyAlgorithm.algorithms.KRUSKAL)
                    ? new MinimumSpanningTreeKruskal()
                    : new MinimumSpanningTreePrim();
        }

        for (List<Graph> graphList : graphLists) {
            List<Double> runTimes = new LinkedList<>();

            for (Graph graph : graphList) {
                List<Double> temp = new LinkedList<>();

                for (int i = 0; i < 130; i++) {
                    myAlgorithm = setup(algorithms);
                    myAlgorithm.init(graph);

                    double startTime = System.nanoTime();
                    myAlgorithm.compute();
                    double endTime = System.nanoTime();

                    temp.add((endTime - startTime));
                }

                double value = temp.stream().min(Double::compare).orElseThrow();

                runTimes.add(value/1000000);

                List<Edge> mstArrayList = myAlgorithm.getMstResult();
                mstArrayList.forEach(edge -> {
                    edge.setAttribute("ui.style", "fill-color: blue; size: 5px;");
                    edge.getNode0().setAttribute("ui.style", "fill-color: black;");
                    edge.getNode1().setAttribute("ui.style", "fill-color: black;");
                });
                myAlgorithm.getSuperComputers().setAttribute("ui.style", "fill-color: red; size: 4px;");
                myAlgorithm.getSuperComputers().setAttribute("weight", myAlgorithm.getOriginalWeight());
            }

            benchmarkLists.add(runTimes);
        }
    }

    private MyAlgorithm setup(MyAlgorithm.algorithms algorithms) {
        return (algorithms == MyAlgorithm.algorithms.KRUSKAL)
                ? new MinimumSpanningTreeKruskal()
                : new MinimumSpanningTreePrim();
    }
}
