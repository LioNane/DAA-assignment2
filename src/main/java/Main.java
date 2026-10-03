void main() {
    String csvFile = "results.csv";

    try {
        List<String> lines = Files.readAllLines(Path.of(csvFile));

        if (lines.isEmpty()) {
            System.out.println("File is empty.");
            return;
        }

        System.out.println("-".repeat(113));

        for (int i = 0; i < lines.size(); i++) {
            String[] columns = lines.get(i).split(",", -1);

            if (columns.length == 8) {
                System.out.printf(
                        "| %-10s | %-8s | %-14s | %-8s | %-12s | %-12s | %-12s | %-12s |%n",
                        columns[0], columns[1], columns[2], columns[3],
                        columns[4], columns[5], columns[6], columns[7]);
            } else {
                System.err.println("Invalid CSV row " + (i + 1) + ": expected 8 columns.");
            }

            if (i == 0) {
                System.out.println("-".repeat(113));
            }
        }

        System.out.println("-".repeat(113));

    } catch (IOException e) {
        System.err.println("File reader error: " + e.getMessage());
    }
}