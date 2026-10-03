package metrics;

public class Metrics {

    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;

    private long startTime;
    private long endTime;

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void incrementSteps(){
        steps++;
    }

    public void incrementMoves(){
        moves++;
    }

    public void incrementComparisons(){
        comparisons++;
    }

    public void startTimer(){
        startTime = System.nanoTime();
    }

    public void stopTimer(){
        endTime = System.nanoTime();
    }

    public long getTimeMs(){
        return endTime - startTime;
    }

    public void reset(){
        steps = 0;
        moves = 0;
        comparisons = 0;
        startTime = 0;
        endTime = 0;
    }
}

