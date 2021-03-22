package com.project.controller;

import com.project.utility.MinimumSpanningTreeKruskal;
import com.project.utility.MyAlgorithm;
import com.project.view.MSTResultView;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame implements Runnable {
    public static SwingViewer swingViewer;
    public static View view;

    private MSTResultView kruskalResultView;
    private MSTResultView primResultView;

    private JTabbedPane resultTabbedPane;
    private JPanel kruskalTab;
    private JPanel primTab;
    private JPanel graphPanel;
    private JPanel topPanel;

    private JTextField nodesTextField;

    private JLabel numberOfEdgesLabel;

    private JButton generateButton;
    private JButton clearButton;
    private JButton runButton;

    private boolean built;

    @Override
    public void run() {
        System.setProperty("org.graphstream.ui", "swing");
        this.setTitle("Minimum Spanning Network");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1280, 720));

        initLeftPane(this);
        initGraphPanel(this);

        resultTabbedPane = new JTabbedPane();
        kruskalTab = new JPanel();
        primTab = new JPanel();

        kruskalTab.setLayout(new GridLayout());

        resultTabbedPane.addTab("Kruskal", kruskalTab);
        resultTabbedPane.addTab("Prim", primTab);

        kruskalTab.add(graphPanel);

        this.getContentPane().add(resultTabbedPane);

        kruskalResultView = new MSTResultView(graphPanel, MyAlgorithm.algorithms.KRUSKAL);

        graphPanel.add(kruskalResultView.getTabbedPane());

        this.setSize(this.getPreferredSize());
        this.setVisible(true);
    }

    private void initLeftPane(Container container) {
        topPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(5);
        topPanel.setLayout(flowLayout);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        topPanel.add(new JLabel("Number of nodes = "));

        nodesTextField = new JTextField();
        nodesTextField.setPreferredSize(new Dimension(120, 20));
        topPanel.add(nodesTextField);

        initButtons();

        topPanel.add(generateButton);
        topPanel.add(clearButton);
        topPanel.add(runButton);

        numberOfEdgesLabel = new JLabel("|E| = 0");
        topPanel.add(numberOfEdgesLabel);

        container.add(topPanel, BorderLayout.NORTH);
    }

    private void initGraphPanel(Container container) {
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayout());
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        container.add(graphPanel, BorderLayout.CENTER);
    }

    private void initButtons() {
        Dimension dimension = new Dimension(100, 20);

        generateButton = new JButton("Generate");
        generateButton.setPreferredSize(dimension);

        generateButton.addActionListener(e -> {
            if (!built) {
                kruskalResultView.init(50, 1000, 20);
                kruskalResultView.compute();

                swingViewer = new SwingViewer(kruskalResultView.getFirstGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

                view = swingViewer.addDefaultView(false);

                kruskalResultView.getTabList().stream().findFirst().get().add((Component) view);

                graphPanel.validate();
                graphPanel.repaint();

                built = true;
            }
        });

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(dimension);

        runButton = new JButton("Run");
        runButton.setPreferredSize(dimension);
    }

    /*
    @Override
    public void run() {
        mainView = new MainView();

        mainView.display();

        mainView.getGenerateButton().addActionListener(e -> {
            int chosenVertices = Integer.parseInt(mainView.getNodesTextField().getText());

            myGraphGenerator = new MyGraphGenerator(chosenVertices);

            Thread thread = new Thread(myGraphGenerator::execute);
            thread.start();

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

            mainView.getMaxEdgesLabel().setText(String.valueOf(mainGraph.getEdgeCount()));

            mainView.getGraphPanel().validate();
            mainView.getGraphPanel().repaint();
        });

        mainView.getRunButton().addActionListener(e -> {
            /*Thread thread = new Thread(() -> {
                MSTResultView mstResultView = new MSTResultView();

                mstResultView.display();

                int availableProcesses = Runtime.getRuntime().availableProcessors() + 1;
                ExecutorService executorService = Executors.newFixedThreadPool(availableProcesses);
                List<Callable<Graph>> callables = new LinkedList<>();

                pipe.pump();

                for (int i = 0; i < 40; i++) {
                    int finalI = i;
                    Callable<Graph> callableTask = () -> {
                        MinimumSpanningTreeKruskal mstKruskal = new MinimumSpanningTreeKruskal();
                        mstKruskal.init(mainGraph);

                        double startTimes = System.nanoTime();
                        mstKruskal.compute();
                        double endTimes = System.nanoTime();

                        for (int v = 0; v < mainGraph.getNodeCount(); v++) {
                            Node node = mainGraph.getNode(String.valueOf(v));
                            double[] xyz = Toolkit.nodePosition(mainGraph, node.getId());
                            mstKruskal.getMstGraph().getNode(node.getId()).setAttribute("xyz", xyz[0], xyz[1], xyz[2]);
                        }

                        for (Edge edge : mstKruskal.getMstResult()) {
                            if (!edge.getId().equals(mstKruskal.getSuperComputers().getId())) {
                                edge.setAttribute("ui.style", "fill-color: green;");
                            }
                        }

                        mstKruskal.getMstGraph().setAttribute("runtime", (endTimes - startTimes));

                        //mstKruskal.getMstGraph().setAttribute("ui.screenshot", "F:\\Temp\\New folder\\img" + finalI + ".png");

                        return mstKruskal.getMstGraph();
                    };

                    callables.add(callableTask);
                }

                List<Future<Graph>> futures;

                try {
                    futures = executorService.invokeAll(callables);

                    executorService.shutdown();

                    for (Future<Graph> graphFuture : futures) {
                        try {
                            System.out.println((((Double) graphFuture.get().getAttribute("runtime")) / 1000000) + "ms");
                            if (!graphFuture.isDone()) {
                                //Thread.sleep(1000);
                            }
                        } catch (InterruptedException | ExecutionException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            });

            double startTimes = System.nanoTime();
            thread.start();
            try {
                thread.join();
                double endTimes = System.nanoTime();
                System.out.println((endTimes - startTimes)/1000000000 + "s");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }*/

            /*assert futures != null;
            for (Future<Graph> graphFuture : futures) {
                SwingViewer swingViewer = null;
                try {
                    //swingViewer = new SwingViewer(graphFuture.get(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
                    System.out.println((((Double) graphFuture.get().getAttribute("runtime")) / 1000000) + "ms");
                } catch (InterruptedException | ExecutionException interruptedException) {
                    interruptedException.printStackTrace();
                }

                assert swingViewer != null;
                //View view = swingViewer.addDefaultView(false);
                //mstResultView.getKruskalResultPanel().add((Component) view);
            }
        });
    }*/
}
