package com.charts;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author imssbora
 *
 */
public class JFreeCharts extends JFrame {

    private static final long serialVersionUID = 1L;

    private String measured = "measured";

    private String prediction = "prediction";

    private double[] measuredData;

    private double[] predictionData;

    private double[] yName;

    public JFreeCharts (String title, double[] measuredData, double[] predictionData, double[] yName) {
        super(title);
        this.measuredData = measuredData;
        this.predictionData = predictionData;
        this.yName = yName;
        // Create dataset
        DefaultCategoryDataset dataset = createDataset();
        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Site Traffic (WWW.BORAJI.COM)", // Chart title
                "Date", // X-Axis Label
                "Number of Visitor", // Y-Axis Label
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private DefaultCategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0, length = measuredData.length; i < length; i++) {
            dataset.addValue(measuredData[i], measured, Double.toString(yName[i]));
            dataset.addValue(predictionData[i], prediction, Double.toString(yName[i]));
        }

//        for (int i = 0, length = predictionData.length; i < length; i++) {
//            dataset.addValue(predictionData[i], prediction, Double.toString(yName[i]));
//        }

        return dataset;
    }

//    public static void main(String[] args) {
//        double[] measuredData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        double[] predictionData = {0, 1, 2, 3, 4, 5, 6, 7 ,8 ,9};
//        double[] y = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        SwingUtilities.invokeLater(() -> {
//            JFreeCharts example = new JFreeCharts("Line Chart Example", measuredData, predictionData, y);
//            example.setAlwaysOnTop(true);
//            example.pack();
//            example.setSize(600, 400);
//            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            example.setVisible(true);
//        });
//    }
}
