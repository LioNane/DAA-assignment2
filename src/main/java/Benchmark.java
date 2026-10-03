import dataStructures.DynamicArray;
import dataStructures.MinHeap;
import dataStructures.MyLinkedList;
import metrics.Metrics;

private static final int[] SIZES = {100, 1000, 10000, 100000};
private static final int WARMUP_RUNS = 2;
private static final int MEASURED_RUNS = 5;
private static final String CSV_FILE = "results.csv";
private static final Random random = new Random(42);

private static double calculateMedian(double[] times){
    Arrays.sort(times);
    return times[times.length / 2];
}

private static void runW1(PrintWriter writer, int n){
    String[] structures = {"DynamicArray", "MyLinkedList"};
    for (String struct : structures) {
        double[] times = new double[MEASURED_RUNS];
        long lastSteps = 0, lastMoves = 0, lastComparisons = 0;

        for (int i = 0; i < WARMUP_RUNS + MEASURED_RUNS; i++) {
            Metrics metrics = new Metrics();

            int[] randomIndexes = new int[10000];
            for (int j = 0; j < 10000; j++) {
                randomIndexes[j] = random.nextInt(n);
            }

            metrics = new Metrics();
            DynamicArray testArray = new DynamicArray(metrics);
            MyLinkedList testList = new MyLinkedList(metrics);

            for (int j = 0; j < n; j++) {
                int val = random.nextInt();
                if (struct.equals("DynamicArray")){
                    testArray.add(val);
                } else {
                    testList.add(val);
                }
            }

            metrics.reset();

            metrics.startTimer();
            if (struct.equals("DynamicArray")){
                for (int idx : randomIndexes) {
                    testArray.get(idx);
                }
            } else {
                for (int idx : randomIndexes){
                    testList.get(idx);
                }
            }
            metrics.stopTimer();

            if (i >= WARMUP_RUNS){
                times[i - WARMUP_RUNS] = metrics.getTimeMs();
                lastSteps = metrics.getSteps();
                lastMoves = metrics.getMoves();
                lastComparisons = metrics.getComparisons();
            }
        }

        double medianTime = calculateMedian(times);
        writer.printf(Locale.ROOT, "W1,-,%s,%d,%.6f,%d,%d,%d%n", struct, n, medianTime, lastSteps, lastMoves, lastComparisons);
    }
}

private static void runW2(PrintWriter writer, int n) {
    String[] structures = {"DynamicArray", "MyLinkedList"};
    for (String struct : structures) {
        double[] times = new double[MEASURED_RUNS];
        long lastSteps = 0, lastMoves = 0, lastComparisons = 0;

        for (int i = 0; i < WARMUP_RUNS + MEASURED_RUNS; i++) {
            Metrics metrics = new Metrics();
            DynamicArray testArray = new DynamicArray(metrics);
            MyLinkedList testList = new MyLinkedList(metrics);
            int[] data = new int[n];

            for (int j = 0; j < n; j++) {
                data[j] = random.nextInt();
                if (struct.equals("DynamicArray")) testArray.add(data[j]);
                else testList.add(data[j]);
            }

            int[] queries = new int[1000];
            for (int j = 0; j < 500; j++) queries[j] = data[random.nextInt(n)];
            for (int j = 500; j < 1000; j++) queries[j] = random.nextInt();

            for (int j = 999; j > 0; j--) {
                int k = random.nextInt(j + 1);
                int temp = queries[j]; queries[j] = queries[k]; queries[k] = temp;
            }

            metrics.reset();

            metrics.startTimer();
            if (struct.equals("DynamicArray")) {
                for (int query : queries) testArray.contains(query);
            } else {
                for (int query : queries) testList.contains(query);
            }
            metrics.stopTimer();

            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = metrics.getTimeMs();
                lastSteps = metrics.getSteps();
                lastMoves = metrics.getMoves();
                lastComparisons = metrics.getComparisons();
            }
        }
        double medianTime = calculateMedian(times);
        writer.printf(Locale.ROOT,"W2,-,%s,%d,%.6f,%d,%d,%d%n", struct, n, medianTime, lastSteps, lastMoves, lastComparisons);
    }
}

private static void runW3(PrintWriter writer, int n, String variant) {
    String[] structures = {"DynamicArray", "MyLinkedList"};
    for (String struct : structures) {
        double[] times = new double[MEASURED_RUNS];
        long lastSteps = 0, lastMoves = 0, lastComparisons = 0;

        for (int i = 0; i < WARMUP_RUNS + MEASURED_RUNS; i++) {
            Metrics metrics = new Metrics();
            DynamicArray testArray = new DynamicArray(metrics);
            MyLinkedList testList = new MyLinkedList(metrics);

            for (int j = 0; j < n; j++) {
                int val = random.nextInt();
                if (struct.equals("DynamicArray")) testArray.add(val);
                else testList.add(val);
            }

            int targetIndex = variant.equals("head") ? 0 : n / 2;

            metrics.reset();
            metrics.startTimer();

            if (struct.equals("DynamicArray")) {
                for (int j = 0; j < 1000; j++) testArray.add(targetIndex, random.nextInt());
                for (int j = 0; j < 1000; j++) testArray.remove(targetIndex);
            } else {
                for (int j = 0; j < 1000; j++) testList.add(targetIndex, random.nextInt());
                for (int j = 0; j < 1000; j++) testList.remove(targetIndex);
            }

            metrics.stopTimer();

            if (i >= WARMUP_RUNS) {
                times[i - WARMUP_RUNS] = metrics.getTimeMs();
                lastSteps = metrics.getSteps();
                lastMoves = metrics.getMoves();
                lastComparisons = metrics.getComparisons();
            }
        }
        double medianTime = calculateMedian(times);
        writer.printf(Locale.ROOT,"W3,%s,%s,%d,%.6f,%d,%d,%d%n", variant, struct, n, medianTime, lastSteps, lastMoves, lastComparisons);
    }
}

private static void runW4(PrintWriter writer, int n) {
    double[] times = new double[MEASURED_RUNS];
    long lastSteps = 0, lastMoves = 0, lastComparisons = 0;

    for (int i = 0; i < WARMUP_RUNS + MEASURED_RUNS; i++) {
        Metrics metrics = new Metrics();
        MinHeap heap = new MinHeap(metrics);

        metrics.startTimer();
        for (int j = 0; j < n; j++) {
            heap.insert(random.nextInt());
        }

        int prev = Integer.MIN_VALUE;
        for (int j = 0; j < n; j++) {
            int current = heap.extractMin();
            if (current < prev) {
                throw new RuntimeException("Heap property violated!");
            }
            prev = current;
        }
        metrics.stopTimer();

        if (i >= WARMUP_RUNS) {
            times[i - WARMUP_RUNS] = metrics.getTimeMs();
            lastSteps = metrics.getSteps();
            lastMoves = metrics.getMoves();
            lastComparisons = metrics.getComparisons();
        }
    }
    double medianTime = calculateMedian(times);
    writer.printf(Locale.ROOT,"W4,-,MinHeap,%d,%.6f,%d,%d,%d%n", n, medianTime, lastSteps, lastMoves, lastComparisons);
}

void main() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
        writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

        for (int n : SIZES) {
            runW1(writer, n);
            runW2(writer, n);
            runW3(writer, n, "head");
            runW3(writer, n, "middle");
            runW4(writer, n);
        }
        System.out.println("Benchmark finished successfully. Results saved in " + CSV_FILE);
    } catch (IOException e) {
        System.err.println("File writer error: " + e.getMessage());
    }
}