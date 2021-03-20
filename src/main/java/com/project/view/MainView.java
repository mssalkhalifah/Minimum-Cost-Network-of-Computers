package com.project.view;

import com.project.model.Edge;
import com.project.model.MyGraph;
import com.project.utility.Algorithms;
import com.project.utility.GraphUtil;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;

public class MainView {
    private JFrame frame;

    private JPanel graphPanel;

    private JTextField nodesTextField;
    private JTextField edgesTextField;

    private JLabel nodesLabel;
    private JLabel edgesLabel;
    private JLabel maxEdgesLabel;
    private JLabel minEdgesLabel;

    private JButton generateButton;
    private JButton clearButton;
    private JButton runButton;

    public void display() {
        frame = new JFrame("Minimum Spanning Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        Container container = frame.getContentPane();

        // Left container
        JPanel leftListPane = new JPanel();
        leftListPane.setLayout(new BoxLayout(leftListPane, BoxLayout.Y_AXIS));
        leftListPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        leftListPane.setPreferredSize(new Dimension(150, 720));

        nodesLabel = new JLabel("Number of nodes");
        nodesTextField = new JTextField();
        nodesTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        nodesTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        edgesLabel = new JLabel("number of edges");
        edgesTextField = new JTextField();
        edgesTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        maxEdgesLabel = new JLabel("Max |E|=");
        minEdgesLabel = new JLabel("Min |E|=");

        nodesTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateMaxMinEdgeCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMaxMinEdgeCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateMaxMinEdgeCount();
            }
        });

        leftListPane.add(nodesLabel);
        leftListPane.add(nodesTextField);
        leftListPane.add(edgesLabel);
        leftListPane.add(edgesTextField);
        leftListPane.add(maxEdgesLabel);
        leftListPane.add(minEdgesLabel);

        leftListPane.add(Box.createRigidArea(new Dimension(0, 5)));

        initButtons();

        leftListPane.add(generateButton);
        leftListPane.add(Box.createRigidArea(new Dimension(0, 5)));
        leftListPane.add(clearButton);
        leftListPane.add(Box.createRigidArea(new Dimension(0, 5)));
        leftListPane.add(runButton);

        // Center container
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayout());
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        // Merge all component
        container.add(leftListPane, BorderLayout.LINE_START);
        container.add(graphPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public JTextField getNodesTextField() {
        return nodesTextField;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    private void initButtons() {
        generateButton = new JButton("Generate");
        generateButton.setMaximumSize(new Dimension(140, 30));

        clearButton = new JButton("Clear");
        clearButton.setMaximumSize(new Dimension(140, 30));
        runButton = new JButton("Run");
        runButton.setMaximumSize(new Dimension(140, 30));
    }

    private void updateMaxMinEdgeCount() {
        if (nodesTextField.getText().isBlank()) {
            maxEdgesLabel.setText("Max |E|=");
            minEdgesLabel.setText("Min |E|=");
        } else {
            int nodes = Integer.parseInt(nodesTextField.getText());
            int maxEdge = (nodes * (nodes - 1)) / 2;
            int minEdge = nodes - 1;
            maxEdgesLabel.setText("Max |E|=" + maxEdge);
            minEdgesLabel.setText("Min |E|=" + minEdge);
        }
    }
}
