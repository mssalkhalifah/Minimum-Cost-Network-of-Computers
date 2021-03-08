package com.project.utility;

import com.project.model.Subset;

public class SetUtil {
    public static int find(Subset[] subsets, int i) {
        if (subsets[i].getParent() != i) {
            subsets[i].setParent(find(subsets, subsets[i].getParent()));
        }

        return subsets[i].getParent();
    }

    public static void union(Subset[] subsets, int x, int y) {
        int rootX = find(subsets, x);
        int rootY = find(subsets, y);

        if (subsets[rootX].getRank() > subsets[rootY].getRank()) {
            subsets[rootY].setParent(rootX);
        } else if (subsets[rootX].getRank() < subsets[rootY].getRank()) {
            subsets[rootX].setParent(rootY);
        } else {
            subsets[rootY].setParent(rootX);
            subsets[rootX].setRank(subsets[rootX].getRank() + 1);
        }
    }
}
