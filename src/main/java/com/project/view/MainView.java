package com.project.view;

import com.project.utility.MyAlgorithm;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private MSTResultView kruskalResultView;
    private MSTResultView primResultView;
    private ChartResultView chartResultView;

    private JTabbedPane resultTabbedPane;

    private JTextField numberOfNodesPerStepTextField;
    private JTextField numberOfStepsTextField;
    private JTextField iterationsPerStepTextField;

    private JButton generateButton;
    private JButton clearButton;
    private JButton runButton;

    public void display() {
        System.setProperty("org.graphstream.ui", "swing");

        this.setTitle("Minimum Spanning Network");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1280, 720));

        initLeftPane(this);

        resultTabbedPane = new JTabbedPane();

        this.getContentPane().add(resultTabbedPane);

        clearButton.setEnabled(false);
        runButton.setEnabled(false);

        this.setSize(this.getPreferredSize());
        this.pack();
        this.setVisible(true);
    }

    public ChartResultView getChartResultView() {
        return chartResultView;
    }

    public JTabbedPane getResultTabbedPane() {
        return resultTabbedPane;
    }

    public MSTResultView getKruskalResultView() {
        return kruskalResultView;
    }

    public MSTResultView getPrimResultView() {
        return primResultView;
    }

    public JTextField getNumberOfNodesPerStepTextField() {
        return numberOfNodesPerStepTextField;
    }

    public JTextField getNumberOfStepsTextField() {
        return numberOfStepsTextField;
    }

    public JTextField getIterationsPerStepTextField() {
        return iterationsPerStepTextField;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public void initGraphPanel() {
        JPanel kruskalGraphPanel = new JPanel();
        kruskalGraphPanel.setLayout(new GridLayout());
        kruskalGraphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

        JPanel primGraphPanel = new JPanel();
        primGraphPanel.setLayout(new GridLayout());
        primGraphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

        JPanel kruskalTab = new JPanel();
        JPanel primTab = new JPanel();

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
    }

    private void initLeftPane(Container container) {
        JPanel topPanel = new JPanel();

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

    private void initButtons() {
        Dimension dimension = new Dimension(100, 20);

        generateButton = new JButton("Generate");
        generateButton.setPreferredSize(dimension);

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(dimension);

        runButton = new JButton("Run");
        runButton.setPreferredSize(dimension);
    }
}
