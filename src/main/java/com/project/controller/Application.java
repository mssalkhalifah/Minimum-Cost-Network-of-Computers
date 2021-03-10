package com.project.controller;

import com.project.model.Edge;
import com.project.model.MyGraph;
import com.project.utility.Algorithms;

import java.util.Collections;
import java.util.List;

public class Application implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World!\n");

        // Example graph
        MyGraph myGraph = new MyGraph(7);
        myGraph.addEdge(6, 1, 2);
        myGraph.addEdge(6, 2, 4);
        myGraph.addEdge(6, 3, 5);
        myGraph.addEdge(6, 4, 2);
        myGraph.addEdge(6, 5, 6);
        myGraph.addEdge(0, 1, 5);
        myGraph.addEdge(0, 5, 3);
        myGraph.addEdge(5, 4, 4);
        myGraph.addEdge(3, 2, 2);
        myGraph.addEdge(1, 2, 1);

        System.out.println(myGraph + "\n");

        // Example edge list sorted
        List<Edge> edgeArrayList = myGraph.getEdgeList();
        System.out.println(edgeArrayList);
        Collections.sort(edgeArrayList);
        System.out.println(edgeArrayList + "\n");

        // Example of Kruskal
        System.out.println(Algorithms.kruskal(myGraph) + "\n");

        // Example of Prim
        System.out.println(Algorithms.prim(myGraph));
    }
}
