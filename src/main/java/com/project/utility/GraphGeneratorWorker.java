package com.project.utility;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;

import java.util.*;

public class GraphGeneratorWorker implements Algorithm {
    private final Generator GRAPH_GENERATOR;
    private final int NUMBER_OF_VERTICES;

    private Graph graph;

    public GraphGeneratorWorker(int numberOfVertices) {
        this.NUMBER_OF_VERTICES = numberOfVertices;

        GRAPH_GENERATOR = new GridGenerator(true, false, true);
    }

    @Override
    public void init(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void compute() {
        GRAPH_GENERATOR.addSink(graph);

        GRAPH_GENERATOR.begin();
        for (int i = 1; i < Math.ceil(Math.sqrt(NUMBER_OF_VERTICES * 1.2)); i++) {
            GRAPH_GENERATOR.nextEvents();
        }
        GRAPH_GENERATOR.end();

        try {
            if (graph.getNodeCount() > NUMBER_OF_VERTICES) {
                Random random = new Random();
                int numberOfRemoveIteration = graph.getNodeCount() - NUMBER_OF_VERTICES;
                for (int i = 0; i < numberOfRemoveIteration; i++) {
                    int randomNode = getRandomWithExclusion(
                            random,
                            graph.getNodeCount() - 1,
                            findArticulationPoints(graph));

                    if (graph.getNode(randomNode) != null) {
                        graph.removeNode(randomNode);
                    } else {
                        i--;
                    }
                }
            }
        } catch (ElementNotFoundException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        assert graph.getNodeCount() == NUMBER_OF_VERTICES : String.format("|V|=%d expected=%d", graph.getNodeCount(), NUMBER_OF_VERTICES);
        assert Toolkit.isConnected(graph) : "Graph not connected";

        graph.setAttribute("ui.stylesheet",
                "node {fill-mode: image-scaled;" +
                        "size: 32px;" +
                        "fill-image: url('src/main/resources/PC.png'); shape: box; }"+
                        "edge {fill-mode: dyn-plain;" +
                        "text-alignment: under; " +
                        "text-color: white; " +
                        "text-style: bold; " +
                        "text-background-mode: rounded-box; " +
                        "text-background-color: #222C; " +
                        "text-padding: 1px; " +
                        "text-offset: 0px, 2px;}");

        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
    }

    private int getRandomWithExclusion(Random rnd, int end, List<Integer> exclude) {
        int random = rnd.nextInt(end + 1 - exclude.size());
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    private List<Integer> findArticulationPoints(Graph graph) {
        final int V = graph.getNodeCount();
        List<Integer> articulationPoints = new LinkedList<>();
        boolean[] visited = new boolean[V];
        int[] disc = new int[V];
        int[] low = new int[V];
        int[] parent = new int[V];
        boolean[] ap = new boolean[V];

        for (int i = 0; i < V; i++)
        {
            parent[i] = -1;
            visited[i] = false;
            ap[i] = false;
        }

        for (int i = 0; i < V; i++)
            if (!visited[i])
                APUtil(i, visited, disc, low, parent, ap, graph);

        for (int i = 0; i < V; i++) {
            if (ap[i]) {
                articulationPoints.add(i);
            }
        }

        return articulationPoints;
    }

    private int time = 0;

    private void APUtil(int u, boolean[] visited, int[] disc,
                        int[] low, int[] parent, boolean[] ap, Graph graph) {
        int children = 0;

        visited[u] = true;

        disc[u] = low[u] = ++time;

        LinkedList<Integer> adjNodes = new LinkedList<>();

        graph.getNode(u).edges().forEach(edge -> {
            if (edge.getNode1().getIndex() != u)
                adjNodes.add(edge.getNode1().getIndex());
            else {
                adjNodes.add(edge.getNode0().getIndex());
            }
        });

        for (int v : adjNodes) {
            if (!visited[v]) {
                children++;
                parent[v] = u;

                APUtil(v, visited, disc, low, parent, ap, graph);

                low[u] = Math.min(low[u], low[v]);

                if (parent[u] == -1 && children > 1)
                    ap[u] = true;

                if (parent[u] != -1 && low[v] >= disc[u])
                    ap[u] = true;
            } else if (v != parent[u])
                low[u] = Math.min(low[u], disc[v]);
        }
    }
}
