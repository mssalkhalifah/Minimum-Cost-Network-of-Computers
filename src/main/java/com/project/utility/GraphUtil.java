package com.project.utility;

import com.project.model.Edge;
import com.project.model.MyGraph;
import org.graphstream.algorithm.Prim;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.util.Random;

public class GraphUtil {
    private static final int MAX_WEIGHT = 1000;
    private static final int MIN_WEIGHT = 1;

    public static MyGraph generateGraph(int numberOfVector) {
        MyGraph resultGraph = new MyGraph(numberOfVector);

        Random random = new Random();

        resultGraph.addEdge(0, 1, MIN_WEIGHT + random.nextInt(MAX_WEIGHT));
        for (int i = 2; i < numberOfVector; i++) {
            int randomWeight = MIN_WEIGHT + random.nextInt(MAX_WEIGHT);
            int randomSource = random.nextInt(i);
            resultGraph.addEdge(randomSource, i, randomWeight);
        }

        int maxEdge = (int) Math.ceil(Math.sqrt((((double) numberOfVector * (numberOfVector - 1) / 2))) * 0.6);
        for (int i = 0; i < maxEdge; i++) {
            int src = random.nextInt(numberOfVector);
            int dst = random.nextInt(numberOfVector);

            if (src == dst) {
                if (dst == numberOfVector - 1) {
                    --dst;
                } else {
                    ++dst;
                }
            }

            if (!resultGraph.getGraph().get(src).containsKey(dst)) {
                resultGraph.addEdge(src, dst, MIN_WEIGHT + random.nextInt(MAX_WEIGHT));
            } else {
                i--;
            }
        }

        return resultGraph;
    }

    public static SwingViewer createGraphViewPanel(MyGraph myGraph) {
        Graph graph = new SingleGraph("");

        for (int i = 0; i < myGraph.TOTAL_VERTICES; i++) {
            graph.addNode(String.valueOf(i));
        }

        for (Edge edge : myGraph.getEdgeList()) {
            String src = String.valueOf(edge.getSource());
            String dst = String.valueOf(edge.getDestination());
            graph.addEdge(src + "-" + dst, src, dst);
        }

        SwingViewer swingViewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        swingViewer.enableAutoLayout(new SpringBox());

        return swingViewer;
    }
}
