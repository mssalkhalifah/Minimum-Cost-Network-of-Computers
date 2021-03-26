package com.project.view;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.swing.*;
import java.util.*;

public class ChartResultView {
    private final JTabbedPane resultTabbedPane;
    private final JTabbedPane chartResultsTabbedPane;

    public ChartResultView(JTabbedPane resultTabbedPane) {
        this.resultTabbedPane = resultTabbedPane;

        chartResultsTabbedPane = new JTabbedPane();
    }

    public void draw(List<List<Double>> kruskalRuntimeResults,
                     List<List<Double>> primRuntimeResults,
                     int maxNumberOfVectors,
                     int numberOfVectorsPerStep) throws NoSuchElementException {
        XChartPanel<XYChart> bestResult = new XChartPanel<>(new XYChartBuilder()
                .xAxisTitle("Number of vertices")
                .yAxisTitle("Time unit: ms")
                .title("Best Runtime")
                .build());

        XChartPanel<XYChart> worstResult = new XChartPanel<>(new XYChartBuilder()
                .xAxisTitle("Number of vertices")
                .yAxisTitle("Time unit: ms")
                .title("Worst Runtime")
                .build());

        XChartPanel<XYChart> averageResult = new XChartPanel<>(new XYChartBuilder()
                .xAxisTitle("Number of vertices")
                .yAxisTitle("Time unit: ms")
                .title("Average Runtime")
                .build());

        double[] chartNodesX = new double[maxNumberOfVectors / numberOfVectorsPerStep];
        for (int i = 0; i < chartNodesX.length; i++) {
            chartNodesX[i] = numberOfVectorsPerStep * (i + 1);
        }

        double[] kruskalBestResult = kruskalRuntimeResults.stream()
                .mapToDouble(x -> x.stream().min(Double::compare).orElseThrow())
                .toArray();

        double[] kruskalWorstResult = kruskalRuntimeResults.stream()
                .mapToDouble(x -> x.stream().max(Double::compare).orElseThrow())
                .toArray();

        double[] kruskalAverageResult = kruskalRuntimeResults.stream()
                .mapToDouble(x -> x.stream().mapToDouble(y -> y).average().orElseThrow())
                .toArray();

        bestResult.getChart().addSeries("Kruskal", chartNodesX, kruskalBestResult);
        worstResult.getChart().addSeries("Kruskal", chartNodesX, kruskalWorstResult);
        averageResult.getChart().addSeries("Kruskal", chartNodesX, kruskalAverageResult);

        double[] primeBestResult = primRuntimeResults.stream()
                .mapToDouble(x -> x.stream().min(Double::compare).orElseThrow())
                .toArray();

        double[] primeWorstResult = primRuntimeResults.stream()
                .mapToDouble(x -> x.stream().max(Double::compare).orElseThrow())
                .toArray();

        double[] primeAverageResult = primRuntimeResults.stream()
                .mapToDouble(x -> x.stream().mapToDouble(y -> y).average().orElseThrow())
                .toArray();

        bestResult.getChart().addSeries("Prime", chartNodesX, primeBestResult);
        worstResult.getChart().addSeries("Prime", chartNodesX, primeWorstResult);
        averageResult.getChart().addSeries("Prime", chartNodesX, primeAverageResult);

        chartResultsTabbedPane.addTab("Best runtime results", bestResult);
        chartResultsTabbedPane.addTab("Worst runtime results", worstResult);
        chartResultsTabbedPane.addTab("Average runtime results", averageResult);

        resultTabbedPane.add("Results", chartResultsTabbedPane);
    }
}
