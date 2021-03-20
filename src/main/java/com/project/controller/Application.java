package com.project.controller;

import com.project.utility.MinimumSpanningTreeKruskal;
import com.project.utility.MyGraphGenerator;
import com.project.view.MainView;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.awt.*;

public class Application implements Runnable {
    private Graph mainGraph;
    private SwingViewer graphSwing;
    private View graphViewer;
    private MainView mainView;
    private MyGraphGenerator myGraphGenerator;
    private ProxyPipe pipe;
    private boolean built;

    @Override
    public void run() {
        mainView = new MainView();

        mainView.display();

        mainView.getGenerateButton().addActionListener(e -> {
            int chosenVertices = Integer.parseInt(mainView.getNodesTextField().getText());

            myGraphGenerator = new MyGraphGenerator(chosenVertices);
            myGraphGenerator.execute();

            while (!myGraphGenerator.isDone())
            {
                System.out.println("Generating..."); //Template
            }

            if (mainGraph == null) {
                mainGraph = myGraphGenerator.getGraph();
            } else {
                mainGraph.clear();

                Graph temp = myGraphGenerator.getGraph();

                for (int i = 0; i < temp.getNodeCount(); i++) {
                    mainGraph.addNode(String.valueOf(i));
                }

                temp.edges().forEach(edge ->
                        mainGraph.addEdge(edge.getId(), edge.getNode0().getId(), edge.getNode1().getId()));
            }

            mainGraph.setAttribute("ui.quality");
            mainGraph.setAttribute("ui.antialias");

            if (graphSwing == null) {
                graphSwing = new SwingViewer(mainGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
                graphSwing.enableAutoLayout();

                graphViewer = graphSwing.addDefaultView(false);
            } else {
                graphSwing.getGraphicGraph().clear();

                for (int i = 0; i < mainGraph.getNodeCount(); i++) {
                    graphSwing.getGraphicGraph().addNode(mainGraph.getNode(i).getId());
                }

                for (int i = 0; i < mainGraph.getEdgeCount(); i++) {
                    Edge edge = mainGraph.getEdge(i);
                    graphSwing.getGraphicGraph().addEdge(edge.getId(), edge.getNode0().getId(), edge.getNode1().getId(), false);
                }
            }

            pipe = graphSwing.newViewerPipe();
            pipe.addAttributeSink(mainGraph);

            graphViewer.getCamera().setAutoFitView(true);

            if (!built) {
                mainView.getGraphPanel().add((Component) graphViewer);
                built = true;
            }

            mainView.getGraphPanel().validate();
            mainView.getGraphPanel().repaint();
        });

        mainView.getRunButton().addActionListener(e -> {
            Thread thread = new Thread(() -> {
                MinimumSpanningTreeKruskal mstKruskal = new MinimumSpanningTreeKruskal();
                mstKruskal.init(mainGraph);
                mstKruskal.compute();

                pipe.pump();
                for (int i = 0; i < mainGraph.getNodeCount(); i++) {
                    Node node = mainGraph.getNode(String.valueOf(i));
                    double[] xyz = Toolkit.nodePosition(mainGraph, node.getId());
                    mstKruskal.getMstGraph().getNode(node.getId()).setAttribute("xyz", xyz[0], xyz[1], xyz[2]);
                }

                SwingViewer swingViewer = new SwingViewer(mstKruskal.getMstGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

                View view = swingViewer.addDefaultView(false);

                for (Edge edge : mstKruskal.getMstResult()) {
                    if (!edge.getId().equals(mstKruskal.getSuperComputers().getId())) {
                        edge.setAttribute("ui.style", "fill-color: green;");
                    }
                }

                mainView.getGraphPanel().add((Component) view);
                mainView.getGraphPanel().validate();
                mainView.getGraphPanel().repaint();
            });
            thread.start();
        });
    }
}
