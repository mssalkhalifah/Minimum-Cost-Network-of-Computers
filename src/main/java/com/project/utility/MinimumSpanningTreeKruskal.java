package com.project.utility;

import com.project.model.Subset;
import org.graphstream.graph.Edge;

import java.util.*;

public class MinimumSpanningTreeKruskal extends MyAlgorithm {

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
