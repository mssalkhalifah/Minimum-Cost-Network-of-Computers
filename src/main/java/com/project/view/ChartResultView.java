package com.project.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ChartResultView {
    private final JTabbedPane resultTabbedPane;
    private final JTabbedPane chartResultsTabbedPane;

    private enum dataCompareTypes {
        MIN, MAX, AVERAGE, ALL
    }

    public ChartResultView(JTabbedPane resultTabbedPane) {
        this.resultTabbedPane = resultTabbedPane;

        chartResultsTabbedPane = new JTabbedPane();
    }

    public void draw(List<List<Double>> kruskalRuntimeResults,
                     List<List<Double>> primRuntimeResults,
                     int numberOfVectorsPerStep) throws NoSuchElementException {

        XYDataset bestDataset = createDataset(kruskalRuntimeResults, primRuntimeResults, numberOfVectorsPerStep, dataCompareTypes.MIN);
        XYDataset worstDataset = createDataset(kruskalRuntimeResults, primRuntimeResults, numberOfVectorsPerStep, dataCompareTypes.MAX);
        XYDataset averageDataset = createDataset(kruskalRuntimeResults, primRuntimeResults, numberOfVectorsPerStep, dataCompareTypes.AVERAGE);

        JFreeChart bestRuntimeChart = createChart(bestDataset, "Best Runtimes");
        JFreeChart worstRuntimeChart = createChart(worstDataset, "Worst Runtimes");
        JFreeChart averageRuntimeChart = createChart(averageDataset, "Average Runtimes");

        chartResultsTabbedPane.addTab("Best Results", new ChartPanel(bestRuntimeChart));
        chartResultsTabbedPane.addTab("Worst Results", new ChartPanel(worstRuntimeChart));
        chartResultsTabbedPane.addTab("Average Results", new ChartPanel(averageRuntimeChart));

        resultTabbedPane.add("Results", chartResultsTabbedPane);
    }

    private JFreeChart createChart(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Number of vertices",
                "Runtime in milliseconds",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(4);
        XYItemLabelGenerator generator =
                new StandardXYItemLabelGenerator("|V|={1},T={2}ms", format, format);
        renderer.setDefaultItemLabelGenerator(generator);
        renderer.setDefaultItemLabelsVisible(true);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }

    private XYDataset createDataset(List<List<Double>> kruskalDataList,
                                    List<List<Double>> primDataList,
                                    int numberOfXPointsPerStep,
                                    dataCompareTypes dataCompareType) {
        XYSeries kruskalSeries = new XYSeries("Kruskal");
        XYSeries primeSeries = new XYSeries("Prim");

        double[] kruskalData = null;
        double[] primData = null;

        switch (dataCompareType) {
            case MIN -> {
                kruskalData = kruskalDataList.stream()
                        .mapToDouble(x -> x.stream().min(Double::compare).orElseThrow())
                        .toArray();
                primData = primDataList.stream()
                        .mapToDouble(x -> x.stream().min(Double::compare).orElseThrow())
                        .toArray();
            }
            case MAX -> {
                kruskalData = kruskalDataList.stream()
                        .mapToDouble(x -> x.stream().max(Double::compare).orElseThrow())
                        .toArray();
                primData = primDataList.stream()
                        .mapToDouble(x -> x.stream().max(Double::compare).orElseThrow())
                        .toArray();
            }
            case AVERAGE -> {
                kruskalData = kruskalDataList.stream()
                        .mapToDouble(x -> x.stream().mapToDouble(y -> y).average().orElseThrow())
                        .toArray();
                primData = primDataList.stream()
                        .mapToDouble(x -> x.stream().mapToDouble(y -> y).average().orElseThrow())
                        .toArray();
            }
        }

        for (int i = 0; i < Objects.requireNonNull(kruskalData).length; i++) {
            kruskalSeries.add((i + 1) * numberOfXPointsPerStep, kruskalData[i]);
            assert primData != null;
            primeSeries.add((i + 1) * numberOfXPointsPerStep, primData[i]);
        }

        XYSeriesCollection xyDataset = new XYSeriesCollection();
        xyDataset.addSeries(kruskalSeries);
        xyDataset.addSeries(primeSeries);

        return xyDataset;
    }
}
