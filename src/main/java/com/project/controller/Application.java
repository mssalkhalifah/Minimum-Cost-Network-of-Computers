package com.project.controller;

import com.project.utility.MyAlgorithm;
import com.project.view.ChartResultView;
import com.project.view.MSTResultView;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public class Application extends JFrame implements Runnable {
    private MSTResultView kruskalResultView;
    private MSTResultView primResultView;
    private ChartResultView chartResultView;

    private JTabbedPane resultTabbedPane;
    private JPanel kruskalTab;
    private JPanel primTab;
    private JPanel graphPanel;
    private JPanel primGraphPanel;
    private JPanel topPanel;

    private JTextField numberOfNodesPerStepTextField;
    private JTextField numberOfStepsTextField;
    private JTextField iterationsPerStepTextField;

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
        initGraphPanel();

        resultTabbedPane = new JTabbedPane();

        kruskalTab = new JPanel();
        primTab = new JPanel();

        kruskalTab.setLayout(new GridLayout());
        primTab.setLayout(new GridLayout());

        resultTabbedPane.addTab("Kruskal", kruskalTab);
        resultTabbedPane.addTab("Prim", primTab);

        kruskalTab.add(graphPanel);
        primTab.add(primGraphPanel);

        this.getContentPane().add(resultTabbedPane);

        for (int i = 0; i < 10000; i++) {
            kruskalResultView = new MSTResultView(graphPanel, MyAlgorithm.algorithms.KRUSKAL);
            primResultView = new MSTResultView(primGraphPanel, MyAlgorithm.algorithms.PRIM);
            chartResultView = new ChartResultView(resultTabbedPane);
        }

        graphPanel.add(kruskalResultView.getTabbedPane());
        primGraphPanel.add(primResultView.getTabbedPane());

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
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayout());
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

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
                int numberOfVertexPerStep = Integer.parseInt(numberOfNodesPerStepTextField.getText());
                int maxNumberOfIteration = Integer.parseInt(numberOfStepsTextField.getText()) * numberOfVertexPerStep;
                int iterationPerStep = Integer.parseInt(iterationsPerStepTextField.getText());

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

                try {
                    chartResultView.draw(kruskalResultView.getResults(),
                            primResultView.getResults(),
                            maxNumberOfIteration,
                            numberOfVertexPerStep);
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }

                this.validate();
                this.repaint();

                built = true;
            }
        });

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(dimension);

        runButton = new JButton("Run");
        runButton.setPreferredSize(dimension);
    }
}
