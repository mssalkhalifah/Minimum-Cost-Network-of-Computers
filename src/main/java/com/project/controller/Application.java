package com.project.controller;

import com.project.model.Edge;
import com.project.model.MyGraph;
import com.project.utility.Algorithms;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.LinkedList;

public class Application implements Runnable {

    @Override
    public void run() {
        MyGraph myGraph = Algorithms.generateGraph(20);
        //MyGraph myGraph = new MyGraph(9);

        /*myGraph.addEdge(0, 1, 10);
        myGraph.addEdge(0, 6, 5);
        myGraph.addEdge(0, 5, 50);
        myGraph.addEdge(1, 6, 7);
        myGraph.addEdge(6, 5, 8);
        myGraph.addEdge(1, 2, 15);
        myGraph.addEdge(1, 7, 20);
        myGraph.addEdge(6, 7, 15);
        myGraph.addEdge(5, 7, 10);
        myGraph.addEdge(5, 4, 30);
        myGraph.addEdge(7, 2, 15);
        myGraph.addEdge(7, 8, 100);
        myGraph.addEdge(7, 4, 40);
        myGraph.addEdge(2, 8, 1);
        myGraph.addEdge(8, 4, 35);
        myGraph.addEdge(2, 3, 50);
        myGraph.addEdge(8, 3, 7);
        myGraph.addEdge(4, 3, 10);*/

        Graph graph = new SingleGraph("Test");

        MyGraph mst = Algorithms.prim(myGraph);

        LinkedList<Node> linkedList = new LinkedList<>();
        LinkedList<org.graphstream.graph.Edge> edgeLinkedList = new LinkedList<>();

        for (int i = 0; i < myGraph.TOTAL_VERTICES; i++) {
            Node n = graph.addNode(String.valueOf(i));
            linkedList.add(n);
            n.setAttribute("ui.label", String.valueOf(i));
        }

        for (Edge edge : myGraph.getEdgeList()) {
            int n1 = edge.getSource();
            int n2 = edge.getDestination();
            int weight = edge.getWeight();
            org.graphstream.graph.Edge e = graph.addEdge(String.format("%d-%d", n1, n2), n1, n2);
            e.setAttribute("ui.label", String.valueOf(weight));
            edgeLinkedList.add(e);
            //e.setAttribute("layout.weight", weight * 0.01);
        }

        System.setProperty("org.graphstream.ui", "swing");

        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.stylesheet",
                "node { fill-mode: dyn-plain; text-size: 10;text-alignment: above; text-background-mode: plain; text-background-color: #EB2; text-color: #222; }" +
                        "edge { fill-mode: dyn-plain;}");

        for (org.graphstream.graph.Edge edge : edgeLinkedList) {
            if (mst.getGraph().get(Integer.parseInt(edge.getNode0().getId())).containsKey(Integer.parseInt(edge.getNode1().getId()))) {
                edge.setAttribute("ui.color", Color.RED);
            }
        }

        Layout layout = new SpringBox(false);
        layout.setQuality(1);
        graph.addSink(layout);
        layout.addAttributeSink(graph);

        Viewer view = graph.display();
    }
}
