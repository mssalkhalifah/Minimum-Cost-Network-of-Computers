package com.project.controller;

import com.project.utility.GraphBenchmarkWorker;
import com.project.view.MainView;
import org.graphstream.graph.Graph;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Application {
    private int numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep;

    public void start() {
        MainView mainView = new MainView();

        mainView.display();

        mainView.getGenerateButton().addActionListener(event -> {
            mainView.getGenerateButton().setEnabled(false);
            mainView.getClearButton().setEnabled(true);
            mainView.getRunButton().setEnabled(true);

            mainView.initGraphPanel();

            this.numberOfVertexPerStep = Integer.parseInt(mainView.getNumberOfNodesPerStepTextField().getText());
            this.maxNumberOfIteration = Integer.parseInt(mainView.getNumberOfStepsTextField().getText()) * numberOfVertexPerStep;
            this.iterationPerStep = Integer.parseInt(mainView.getIterationsPerStepTextField().getText());

            mainView.getKruskalResultView().init(numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep);
            mainView.getPrimResultView().init(numberOfVertexPerStep, maxNumberOfIteration, iterationPerStep);

            mainView.getKruskalResultView().execute();

            try {
                mainView.getKruskalResultView().get();
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }

            if (mainView.getKruskalResultView().isDone()) {
                mainView.getPrimResultView().execute();
                System.out.println("Kruskal done!");
                try {
                    mainView.getPrimResultView().get();
                } catch (InterruptedException | ExecutionException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        mainView.getClearButton().addActionListener(event -> {
            mainView.getGenerateButton().setEnabled(true);
            mainView.getClearButton().setEnabled(false);
            mainView.getRunButton().setEnabled(false);

            mainView.getKruskalResultView().getGraphLists().stream()
                    .flatMap(Collection::parallelStream)
                    .forEach(Graph::clear);

            mainView.getPrimResultView().getGraphLists().stream()
                    .flatMap(Collection::parallelStream)
                    .forEach(Graph::clear);

            mainView.getKruskalResultView().getSwingViewer().getGraphicGraph().clear();
            mainView.getPrimResultView().getSwingViewer().getGraphicGraph().clear();

            mainView.getResultTabbedPane().removeAll();
        });

        mainView.getRunButton().addActionListener(event -> {
            mainView.getGenerateButton().setEnabled(false);
            mainView.getClearButton().setEnabled(true);
            mainView.getRunButton().setEnabled(false);

            try {
                GraphBenchmarkWorker graphBenchmarkWorker = new GraphBenchmarkWorker();

                graphBenchmarkWorker.init(mainView.getKruskalResultView().getGraphLists(), mainView.getKruskalResultView().getAlgorithm());
                graphBenchmarkWorker.compute();

                List<List<Double>> kruskalResults = graphBenchmarkWorker.getBenchmarkLists();

                graphBenchmarkWorker.init(mainView.getPrimResultView().getGraphLists(), mainView.getPrimResultView().getAlgorithm());
                graphBenchmarkWorker.compute();

                List<List<Double>> primResults = graphBenchmarkWorker.getBenchmarkLists();

                mainView.getChartResultView().draw(
                        kruskalResults,
                        primResults,
                        numberOfVertexPerStep);

                mainView.validate();
                mainView.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
