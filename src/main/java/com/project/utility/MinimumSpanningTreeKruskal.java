package com.project.utility;

import com.project.model.Subset;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class MinimumSpanningTreeKruskal extends MyAlgorithm {
    private List<Edge> edgeList;

    @Override
    public void init(Graph graph) {
        super.graph = graph;

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
        superComputers.getNode0().setAttribute("ui.style", "fill-image: url('src/main/resources/Redpc.png'); ");
        superComputers.getNode1().setAttribute("ui.style", "fill-image: url('src/main/resources/Redpc.png'); ");
    }

    @Override
    public void compute() {
        edgeList.sort(Comparator.comparingInt(edge -> ((int) edge.getAttribute("weight"))));

        Subset[] subsets = new Subset[graph.getNodeCount()];

        for (int i = 0; i < subsets.length; i++) {
            subsets[i] = new Subset();
            subsets[i].setParent(i);
            subsets[i].setRank(0);
        }

        for (Edge edge : edgeList) {
            int x = SetUtil.find(subsets, edge.getNode0().getIndex());
            int y = SetUtil.find(subsets, edge.getNode1().getIndex());

            if (x != y) {
                mstResult.add(edge);
                SetUtil.union(subsets, x, y);
            }
        }
    }
}
