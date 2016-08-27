package dr.games.mspacman.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacStats {

    private List<Double> scores = new ArrayList<Double>();
    private List<Double> timeSteps = new ArrayList<Double>();
    private List<Double> mazesCleared = new ArrayList<Double>();
    private List<Double> elapsedTime = new ArrayList<Double>();

    public void addResult(double score, double timeSteps, double mazesCleared, double elapsedTime) {
        scores.add(score);
        this.timeSteps.add(timeSteps);
        this.mazesCleared.add(mazesCleared);
        this.elapsedTime.add(elapsedTime);
    }

    public double average(List<Double> data) {
        return sum(data) / (double) scores.size();
    }

    public double sum(List<Double> data)
    {
        int total = 0;

        for (Double score : data)
            total += score;

        return total;
    }

    public double max(List<Double> data) {
        return Collections.max(data);
    }

    public double min(List<Double> data) {
        return Collections.min(data);
    }

    private void printDataSummary(List<Double> data) {
        System.out.printf("Average:      %8.1f %n", average(data));
        System.out.printf("Max:          %8.1f %n", max(data));
        System.out.printf("Min:          %8.1f %n", min(data));
    }

    public void printSummary()
    {
        System.out.println("\n----------------------------------------");
        System.out.printf("STATISTICAL SUMMARY OF %d SIMULATIONS", scores.size());
        System.out.println("\n----------------------------------------");

        System.out.println("\n\nSCORES\n");
        printDataSummary(scores);

        System.out.println("\n\nELAPSED TIMES\n");
        printDataSummary(elapsedTime);

        System.out.println("\n\nTIME STEPS\n");
        printDataSummary(timeSteps);

        System.out.println("\n\nMAZES COMPLETED\n");
        printDataSummary(mazesCleared);
    }

}
