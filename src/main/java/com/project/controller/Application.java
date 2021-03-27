package com.project.controller;

import com.project.utility.GraphBenchmarkWorker;
import com.project.utility.MyAlgorithm;
import com.project.view.ChartResultView;
import com.project.view.MSTResultView;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Application extends JFrame implements Runnable {
    private MSTResultView kruskalResultView;
    private MSTResultView primResultView;
    private ChartResultView chartResultView;

    private JTabbedPane resultTabbedPane;
    private JPanel kruskalTab;
    private JPanel primTab;
    private JPanel kruskalGraphPanel;
    private JPanel primGraphPanel;
    private JPanel topPanel;

    private JTextField numberOfNodesPerStepTextField;
    private JTextField numberOfStepsTextField;
    private JTextField iterationsPerStepTextField;

    private JButton generateButton;
    private JButton clearButton;
    private JButton runButton;

    private boolean built;
    private int numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep;

    @Override
    public void run() {
        System.setProperty("org.graphstream.ui", "swing");
        this.setTitle("Minimum Spanning Network");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1280, 720));

        initLeftPane(this);
        //initGraphPanel();

        resultTabbedPane = new JTabbedPane();

        /*kruskalTab = new JPanel();
        primTab = new JPanel();

        kruskalTab.setLayout(new GridLayout());
        primTab.setLayout(new GridLayout());

        resultTabbedPane.addTab("Kruskal", kruskalTab);
        resultTabbedPane.addTab("Prim", primTab);

        kruskalTab.add(kruskalGraphPanel);
        primTab.add(primGraphPanel);*/

        this.getContentPane().add(resultTabbedPane);

        //kruskalResultView = new MSTResultView(kruskalGraphPanel, MyAlgorithm.algorithms.KRUSKAL);
        //primResultView = new MSTResultView(primGraphPanel, MyAlgorithm.algorithms.PRIM);
        //chartResultView = new ChartResultView(resultTabbedPane);

        //kruskalGraphPanel.add(kruskalResultView.getTabbedPane());
        //primGraphPanel.add(primResultView.getTabbedPane());

        this.setSize(this.getPreferredSize());
        this.pack();
        this.setVisible(true);
    }

    private void initLeftPane(Container container) {
        topPanel = new JPanel();

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(5);

        topPanel.setLayout(flowLayout);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

        topPanel.add(new JLabel("Nodes per step ="));
        numberOfNodesPerStepTextField = new JTextField();
        numberOfNodesPerStepTextField.setPreferredSize(new Dimension(70, 20));
        topPanel.add(numberOfNodesPerStepTextField);

        topPanel.add(new JLabel("Number of steps ="));
        numberOfStepsTextField = new JTextField();
        numberOfStepsTextField.setPreferredSize(new Dimension(70, 20));
        topPanel.add(numberOfStepsTextField);

        topPanel.add(new JLabel("Iteration per step ="));
        iterationsPerStepTextField = new JTextField();
        iterationsPerStepTextField.setPreferredSize(new Dimension(70, 20));
        topPanel.add(iterationsPerStepTextField);

        initButtons();

        topPanel.add(generateButton);
        topPanel.add(clearButton);
        topPanel.add(runButton);

        container.add(topPanel, BorderLayout.NORTH);
    }

    private void initGraphPanel() {
        kruskalGraphPanel = new JPanel();
        kruskalGraphPanel.setLayout(new GridLayout());
        kruskalGraphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

        primGraphPanel = new JPanel();
        primGraphPanel.setLayout(new GridLayout());
        primGraphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
    }

    private void initButtons() {
        Dimension dimension = new Dimension(100, 20);

        generateButton = new JButton("Generate");
        generateButton.setPreferredSize(dimension);

        generateButton.addActionListener(event -> {
            if (!built) {
                generateButton.setEnabled(false);
                clearButton.setEnabled(true);
                runButton.setEnabled(true);

                initGraphPanel();

                kruskalTab = new JPanel();
                primTab = new JPanel();

                kruskalTab.setLayout(new GridLayout());
                primTab.setLayout(new GridLayout());

                resultTabbedPane.addTab("Kruskal", kruskalTab);
                resultTabbedPane.addTab("Prim", primTab);

                kruskalTab.add(kruskalGraphPanel);
                primTab.add(primGraphPanel);

                kruskalResultView = new MSTResultView(kruskalGraphPanel, MyAlgorithm.algorithms.KRUSKAL);
                primResultView = new MSTResultView(primGraphPanel, MyAlgorithm.algorithms.PRIM);
                chartResultView = new ChartResultView(resultTabbedPane);

                kruskalGraphPanel.add(kruskalResultView.getTabbedPane());
                primGraphPanel.add(primResultView.getTabbedPane());

                this.numberOfVertexPerStep = Integer.parseInt(numberOfNodesPerStepTextField.getText());
                this.maxNumberOfIteration = Integer.parseInt(numberOfStepsTextField.getText()) * numberOfVertexPerStep;
                this.iterationPerStep = Integer.parseInt(iterationsPerStepTextField.getText());

                kruskalResultView.init(numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep);
                primResultView.init(numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep);

                kruskalResultView.execute();

                try {
                    kruskalResultView.get();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                } catch (ExecutionException executionException) {
                    executionException.printStackTrace();
                }

                if (kruskalResultView.isDone()) {
                    primResultView.execute();
                    System.out.println("Kruskal done!");
                    try {
                        primResultView.get();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    } catch (ExecutionException executionException) {
                        executionException.printStackTrace();
                    }
                }

                built = true;
            }
        });

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(dimension);

        clearButton.addActionListener(event -> {
            generateButton.setEnabled(true);
            clearButton.setEnabled(false);
            runButton.setEnabled(false);

            kruskalResultView.getGraphLists().stream()
                    .flatMap(Collection::parallelStream)
                    .forEach(graph -> graph.clear());

            primResultView.getGraphLists().stream()
                    .flatMap(Collection::parallelStream)
                    .forEach(graph -> graph.clear());

            kruskalResultView.getSwingViewer().getGraphicGraph().clear();
            primResultView.getSwingViewer().getGraphicGraph().clear();

            resultTabbedPane.removeAll();

            built = false;
        });

        runButton = new JButton("Run");
        runButton.setPreferredSize(dimension);

        runButton.addActionListener(event -> {
            generateButton.setEnabled(false);
            clearButton.setEnabled(true);
            runButton.setEnabled(false);

            try {
                GraphBenchmarkWorker graphBenchmarkWorker = new GraphBenchmarkWorker();

                graphBenchmarkWorker.init(kruskalResultView.getGraphLists(), kruskalResultView.getAlgorithm());
                graphBenchmarkWorker.compute();

                List<List<Double>> kruskalResults = graphBenchmarkWorker.getBenchmarkLists();

                graphBenchmarkWorker.init(primResultView.getGraphLists(), primResultView.getAlgorithm());
                graphBenchmarkWorker.compute();

                List<List<Double>> primResults = graphBenchmarkWorker.getBenchmarkLists();

                chartResultView.draw(
                        kruskalResults,
                        primResults,
                        maxNumberOfIteration,
                        numberOfVertexPerStep);

                this.validate();
                this.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        clearButton.setEnabled(false);
        runButton.setEnabled(false);
    }
}
