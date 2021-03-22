package com.project.utility;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class GraphGeneratorWorker extends SwingWorker<Void, Void> {
    private final MyAlgorithm myAlgorithm;
    private final Generator GRAPH_GENERATOR;
    private final int NUMBER_OF_VERTICES;

    private Graph graph;

    public GraphGeneratorWorker(Graph graph, int numberOfVertices, MyAlgorithm myAlgorithm) {
        this.myAlgorithm = myAlgorithm;
        this.graph = graph;
        this.NUMBER_OF_VERTICES = numberOfVertices;

        GRAPH_GENERATOR = new GridGenerator(true, false, true);
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    protected Void doInBackground() throws Exception {
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
                            0,
                            graph.getNodeCount(),
                            findArticulationPoints(graph));

                    if (graph.getNode(randomNode) != null) {
                        graph.removeNode(randomNode);
                    } else {
                        i--;
                    }
                }
            }
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
        }

        assert (graph.getNodeCount() == NUMBER_OF_VERTICES);
        assert (Toolkit.isConnected(graph));

        graph.setAttribute("ui.stylesheet",
                "node {fill-mode: dyn-plain;}" +
                        "edge {fill-mode: dyn-plain;" +
                        "size: 8px;}");

        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        myAlgorithm.init(graph);
        myAlgorithm.compute();

        List<Edge> mstArrayList = new ArrayList<>(myAlgorithm.getMstResult());
        int size = mstArrayList.size();
        for (int i = 0; i < size; i++) {
            if (mstArrayList.get(i).getId() != myAlgorithm.getSuperComputers().getId()) {
                mstArrayList.get(i).setAttribute("ui.style", "fill-color: blue;");
            }
        }
        myAlgorithm.getSuperComputers().setAttribute("ui.style", "fill-color: red;");
        myAlgorithm.getSuperComputers().setAttribute("weight", myAlgorithm.getOriginalWeight());

        return null;
    }

    private int getRandomWithExclusion(Random rnd, int start, int end, List<Integer> exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.size());
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
        int disc[] = new int[V];
        int low[] = new int[V];
        int parent[] = new int[V];
        boolean ap[] = new boolean[V];

        for (int i = 0; i < V; i++)
        {
            parent[i] = -1;
            visited[i] = false;
            ap[i] = false;
        }

        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                APUtil(i, visited, disc, low, parent, ap, graph);

        for (int i = 0; i < V; i++) {
            if (ap[i] == true) {
                articulationPoints.add(i);
            }
        }

        return articulationPoints;
    }

    private int time = 0;

    void APUtil(int u, boolean visited[], int disc[],
                int low[], int parent[], boolean ap[], Graph graph) {
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

        Iterator<Integer> i = adjNodes.iterator();
        while (i.hasNext())
        {
            int v = i.next();

            if (!visited[v])
            {
                children++;
                parent[v] = u;
                APUtil(v, visited, disc, low, parent, ap, graph);

                low[u]  = Math.min(low[u], low[v]);

                if (parent[u] == -1 && children > 1)
                    ap[u] = true;

                if (parent[u] != -1 && low[v] >= disc[u])
                    ap[u] = true;
            }

            else if (v != parent[u])
                low[u]  = Math.min(low[u], disc[v]);
        }
    }

    /*@Override
    protected void process(List<Integer> chunks) {
        int lastChunkValue = chunks.get(chunks.size() - 1);
        jProgressBar.setValue(lastChunkValue);
        double currentProgress = ((double) jProgressBar.getValue()/ NUMBER_OF_VERTICES) * 100;
        jProgressBar.setString(String.format("%.2f%%", currentProgress));
    }*/
}
