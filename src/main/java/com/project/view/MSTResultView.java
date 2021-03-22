package com.project.view;

import com.project.utility.GraphGeneratorWorker;
import com.project.utility.MinimumSpanningTreeKruskal;
import com.project.utility.MyAlgorithm;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.project.controller.Application.swingViewer;
import static com.project.controller.Application.view;

public class MSTResultView {
    private MyAlgorithm.algorithms algorithm;

    private JTabbedPane tabbedPane;
    private JPanel graphPanel;

    private int currentGraphIndex;
    private int currentTabIndex;

    private List<JPanel> tabList;
    private List<SwingWorker> swingWorkers;
    private List<List<Graph>> graphLists;

    public MSTResultView(JPanel graphPanel, MyAlgorithm.algorithms algorithm) {
        this.algorithm = algorithm;
        this.graphPanel = graphPanel;

        tabbedPane = new JTabbedPane();
        swingWorkers = new LinkedList<>();
    }

    public void init(int numberOfVertexPerStep, int maxNumberOfIteration, int iterationPerStep) {
        currentGraphIndex = 0;
        currentTabIndex = 0;

        tabList = new ArrayList<>(maxNumberOfIteration / numberOfVertexPerStep);
        graphLists = new ArrayList<>(maxNumberOfIteration / numberOfVertexPerStep);

        for (int i = 0; i < maxNumberOfIteration / numberOfVertexPerStep; i++) {
            graphLists.add(new ArrayList<>(iterationPerStep));
        }

        for (int i = 0; i < maxNumberOfIteration / numberOfVertexPerStep; i++) {
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());

            JLabel pageNumber = new JLabel(String.format("Page %d of %d", currentGraphIndex + 1, iterationPerStep));

            JButton nextButton = new JButton("Next");
            JButton prevButton = new JButton("Prev");

            nextButton.addActionListener(e -> {
                if (currentGraphIndex < iterationPerStep - 1) {
                    changeGraph(graphLists.get(currentTabIndex).get(++currentGraphIndex), swingViewer);
                    pageNumber.setText(String.format("Page %d of %d", currentGraphIndex + 1, iterationPerStep));
                }
            });

            prevButton.addActionListener(e -> {
                if (currentGraphIndex > 0) {
                    changeGraph(graphLists.get(currentTabIndex).get(--currentGraphIndex), swingViewer);
                    pageNumber.setText(String.format("Page %d of %d", currentGraphIndex + 1, iterationPerStep));
                }
            });

            topPanel.add(prevButton);
            topPanel.add(nextButton);
            topPanel.add(pageNumber);

            JPanel tab = new JPanel();
            tab.setLayout(new BorderLayout());
            tab.add(topPanel, BorderLayout.NORTH);

            tabList.add(tab);
            tabbedPane.addTab(String.valueOf((i + 1) * numberOfVertexPerStep), tab);

            tabbedPane.addChangeListener(e -> {
                currentTabIndex = Integer.parseInt(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())) / numberOfVertexPerStep - 1;
                currentGraphIndex = 0;
                changeGraph(graphLists.get(currentTabIndex).get(currentGraphIndex), swingViewer);
                tabList.get(currentTabIndex).add((Component) view);
                graphPanel.validate();
                graphPanel.repaint();
            });
        }

        int i = 0;
        for (List<Graph> graphList : graphLists) {
            for (int j = 0; j < iterationPerStep; j++) {
                Graph graph = new SingleGraph(String.valueOf((i + 1) * numberOfVertexPerStep));
                graphList.add(graph);
                swingWorkers.add(new GraphGeneratorWorker(
                        graph,
                        (i + 1) * numberOfVertexPerStep, new MinimumSpanningTreeKruskal()));
            }
            i++;
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public List<JPanel> getTabList() {
        return tabList;
    }

    public Graph getFirstGraph() {
        return graphLists.get(currentTabIndex).get(currentGraphIndex);
    }

    public void compute() {
        for (SwingWorker worker : swingWorkers) {
            worker.execute();
        }
    }

    public void blockUntilDone() throws InterruptedException {
        for (SwingWorker worker : swingWorkers) {
            waitForWorker(worker);
        }
    }

    private void waitForWorker(SwingWorker worker) throws InterruptedException {
        try {
            worker.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeGraph(Graph graph, SwingViewer swingViewer) {
        assert (graph.getNodeCount() == graphLists.get(currentTabIndex).get(currentGraphIndex).getNodeCount());

        swingViewer.getGraphicGraph().clear();

        int size = graph.getNodeCount();
        for (int i = 0; i < size; i++) {
            Node currentNode = graph.getNode(i);
            double[] xyz = Toolkit.nodePosition(currentNode);
            Node changedNode = swingViewer.getGraphicGraph().addNode(currentNode.getId());
            changedNode.setAttribute("ui.style", currentNode.getAttribute("ui.style"));
            changedNode.setAttribute("xyz", xyz[0], xyz[1], xyz[2]);
        }

        size = graph.getEdgeCount();
        for (int i = 0; i < size; i++) {
            Edge currentEdge = graph.getEdge(i);
            Edge changedEdge = swingViewer.getGraphicGraph().addEdge(currentEdge.getId(), currentEdge.getNode0().getId(), currentEdge.getNode1().getId());
            changedEdge.setAttribute("ui.style", currentEdge.getAttribute("ui.style"));
            changedEdge.setAttribute("ui.label", currentEdge.getAttribute("ui.label"));
        }

        swingViewer.getGraphicGraph().setAttribute("ui.quality");
        swingViewer.getGraphicGraph().setAttribute("ui.antialias");

        assert (swingViewer.getGraphicGraph().getNodeCount() == graphLists.get(currentTabIndex).get(currentGraphIndex).getNodeCount());
    }
}
