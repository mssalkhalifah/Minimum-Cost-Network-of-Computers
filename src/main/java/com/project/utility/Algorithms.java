package com.project.utility;

import com.project.model.Edge;
import com.project.model.MyGraph;
import com.project.model.Subset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Algorithms {
    public static MyGraph kruskal(MyGraph myGraph) {
        LinkedList<Edge> mstResult = new LinkedList<>();
        ArrayList<Edge> edgeList = myGraph.getEdgeList();

        Collections.sort(edgeList);

        Subset[] subsets = new Subset[myGraph.TOTAL_VERTICES];

        for (int i = 0; i < subsets.length; i++) {
            subsets[i] = new Subset();
            subsets[i].setParent(i);
            subsets[i].setRank(0);
        }

        for (Edge edge : edgeList) {
            int x = SetUtil.find(subsets, edge.getSource());
            int y = SetUtil.find(subsets, edge.getDestination());

            if (x != y) {
                mstResult.add(edge);
                SetUtil.union(subsets, x, y);
            }
        }

        return new MyGraph(myGraph.TOTAL_VERTICES, mstResult);
    }
}
