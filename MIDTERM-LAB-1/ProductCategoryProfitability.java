/*
 * PROGRAMMING 2 – MACHINE PROBLEM
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Information Technology - Game Development
 * Dataset: https://www.kaggle.com/datasets/asaniczka/video-game-sales-2024
 *
 * ProductCategoryProfitability.java — Main class
 *
 * ANALYSIS: Product Category Profitability
 *   - Groups records by genre (category)
 *   - Computes total sales and average sale per category
 *   - Identifies most & least profitable categories
 */

import java.io.*;
import java.util.*;

public class ProductCategoryProfitability {

    // ------------------------------------------------------------------ //
    //  File validation & loading                                          //
    // ------------------------------------------------------------------ //

    /**
     * Prompts the user for a CSV file path, validates it, and returns the
     * File object.  Loops until a valid, readable CSV file is provided.
     */
    private static File promptForFile(Scanner input) {
        while (true) {
            System.out.print("\nEnter dataset file path: ");
            String path = input.nextLine().trim();
            File file = new File(path);

            if (!file.exists()) {
                System.out.println("[ERROR] File does not exist. Please try again.");
            } else if (!file.isFile()) {
                System.out.println("[ERROR] Path does not point to a file. Please try again.");
            } else if (!file.canRead()) {
                System.out.println("[ERROR] File is not readable. Please try again.");
            } else if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("[ERROR] File does not appear to be a CSV. Please try again.");
            } else {
                System.out.println("[OK]    File found: " + file.getAbsolutePath());
                return file;
            }
        }
    }

    /**
     * Reads the CSV file and returns a list of DataRecord objects.
     * Expected columns (case-insensitive header detection):
     *   title, genre, total_sales
     */
    private static List<DataRecord> loadRecords(File file) throws IOException {
        List<DataRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) throw new IOException("CSV file is empty.");

            // Detect column indices from header
            String[] headers = headerLine.split(",");
            int titleIdx = -1, genreIdx = -1, salesIdx = -1;

            for (int i = 0; i < headers.length; i++) {
                String h = headers[i].trim().toLowerCase().replace("\"", "");
                if (h.equals("title") || h.equals("name") || h.equals("game"))          titleIdx = i;
                if (h.equals("genre") || h.equals("category"))                           genreIdx = i;
                if (h.equals("total_sales") || h.equals("sales") || h.equals("totalsales")) salesIdx = i;
            }

            if (genreIdx == -1 || salesIdx == -1) {
                throw new IOException(
                    "Required columns not found in CSV.\n" +
                    "Expected: 'genre' (or 'category') and 'total_sales' (or 'sales').\n" +
                    "Found headers: " + headerLine
                );
            }

            String line;
            int skipped = 0;

            while ((line = br.readLine()) != null) {
                // Handle quoted fields naively (split on comma outside quotes)
                String[] cols = splitCSVLine(line);

                try {
                    String title = (titleIdx >= 0 && titleIdx < cols.length)
                            ? cols[titleIdx].trim().replace("\"", "") : "Unknown";
                    String genre = cols[genreIdx].trim().replace("\"", "");
                    double sales = Double.parseDouble(
                            cols[salesIdx].trim().replace("\"", "").replace(",", ""));

                    if (!genre.isEmpty()) {
                        records.add(new DataRecord(title, genre, sales));
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    skipped++;   // skip malformed rows silently
                }
            }

            if (skipped > 0) {
                System.out.printf("[INFO]  %d malformed row(s) skipped.%n", skipped);
            }
        }

        return records;
    }

    /** Returns a String of n repeated copies of ch (replaces String.repeat for Java 8+). */
    private static String repeat(char ch, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(ch);
        return sb.toString();
    }

    /** Simple CSV line splitter that respects double-quoted fields. */
    private static String[] splitCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    // ------------------------------------------------------------------ //
    //  Analytics                                                          //
    // ------------------------------------------------------------------ //

    private static void analyzeAndDisplay(List<DataRecord> records) {
        // Accumulators: category -> {totalSales, count}
        Map<String, Double>  totalSalesMap = new LinkedHashMap<>();
        Map<String, Integer> countMap      = new LinkedHashMap<>();

        for (DataRecord r : records) {
            String cat = r.getGenre();
            totalSalesMap.merge(cat, r.getTotalSales(), Double::sum);
            countMap.merge(cat, 1, Integer::sum);
        }

        // Sort by total sales descending
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(totalSalesMap.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Compute grand total for share %
        double grandTotal = 0;
        for (double v : totalSalesMap.values()) grandTotal += v;

        // ================================================================
        //  BANNER
        // ================================================================
        System.out.println();
        System.out.println("  " + repeat('*', 72));
        System.out.println("  *" + repeat(' ', 70) + "*");
        System.out.println("  *     VIDEO GAME SALES 2024  --  CATEGORY PROFITABILITY REPORT" + repeat(' ', 7) + "*");
        System.out.println("  *     University of Perpetual Help System DALTA - Molino Campus" + repeat(' ', 6) + "*");
        System.out.println("  *     BS Information Technology - Game Development" + repeat(' ', 20) + "*");
        System.out.println("  *" + repeat(' ', 70) + "*");
        System.out.println("  " + repeat('*', 72));

        // ================================================================
        //  DATASET SUMMARY BOX
        // ================================================================
        System.out.println();
        System.out.println("  +--------------------------+--------------------------+");
        System.out.printf ("  |  Total Games Loaded      |  %,24d  |%n", records.size());
        System.out.printf ("  |  Unique Categories       |  %24d  |%n", sorted.size());
        System.out.printf ("  |  Grand Total Sales (M)   |  %,24.2f  |%n", grandTotal);
        System.out.println("  +--------------------------+--------------------------+");

        // ================================================================
        //  MAIN TABLE
        // ================================================================
        System.out.println();
        System.out.println("  " + repeat('=', 78));
        System.out.printf ("  %-4s  |  %-18s  |  %12s  |  %10s  |  %7s  |  %5s%n",
                "RANK", "CATEGORY", "TOTAL SALES", "AVG SALE", "# GAMES", "SHARE");
        System.out.println("  " + repeat('=', 78));

        for (int i = 0; i < sorted.size(); i++) {
            String cat    = sorted.get(i).getKey();
            double total  = sorted.get(i).getValue();
            int    count  = countMap.get(cat);
            double avg    = total / count;
            double share  = (total / grandTotal) * 100.0;

            // Alternate row markers for readability
            String marker = (i % 2 == 0) ? ">" : " ";

            System.out.printf("  %s%-3d  |  %-18s  |  %,12.2f  |  %,10.2f  |  %7d  |  %4.1f%%%n",
                    marker, (i + 1), cat, total, avg, count, share);

            // Separator every 5 rows
            if ((i + 1) % 5 == 0 && (i + 1) < sorted.size()) {
                System.out.println("  " + repeat('-', 78));
            }
        }

        System.out.println("  " + repeat('=', 78));

        // ================================================================
        //  FINDINGS PANEL
        // ================================================================
        Map.Entry<String, Double> best  = sorted.get(0);
        Map.Entry<String, Double> worst = sorted.get(sorted.size() - 1);
        double bestShare  = (best.getValue()  / grandTotal) * 100.0;
        double worstShare = (worst.getValue() / grandTotal) * 100.0;

        System.out.println();
        System.out.println("  +--[ FINDINGS ]" + repeat('-', 62) + "+");
        System.out.println("  |" + repeat(' ', 76) + "|");

        String bestLine  = String.format("[#1 MOST PROFITABLE]   %-18s   Sales: %,12.2f   Share: %4.1f%%",
                best.getKey(), best.getValue(), bestShare);
        String worstLine = String.format("[#%d LEAST PROFITABLE]  %-18s   Sales: %,12.2f   Share: %4.1f%%",
                sorted.size(), worst.getKey(), worst.getValue(), worstShare);

        System.out.printf("  |   %-73s|%n", bestLine);
        System.out.println("  |" + repeat(' ', 76) + "|");
        System.out.printf("  |   %-73s|%n", worstLine);
        System.out.println("  |" + repeat(' ', 76) + "|");
        System.out.println("  +" + repeat('-', 76) + "+");
        System.out.println();
    }

    // ------------------------------------------------------------------ //
    //  Entry point                                                        //
    // ------------------------------------------------------------------ //

    public static void main(String[] args) {
        System.out.println();
        System.out.println("  +-----------------------------------------------------------------+");
        System.out.println("  |                                                                 |");
        System.out.println("  |   PROGRAMMING 2  --  Machine Problem                           |");
        System.out.println("  |   Product Category Profitability Analyzer                      |");
        System.out.println("  |   Dataset : Video Game Sales 2024  (Kaggle)                    |");
        System.out.println("  |                                                                 |");
        System.out.println("  +-----------------------------------------------------------------+");
        System.out.println();

        Scanner input = new Scanner(System.in);

        try {
            // Step 1 – Prompt & validate file path
            File file = promptForFile(input);

            // Step 2 – Load dataset
            System.out.println("\n[INFO]  Loading dataset...");
            List<DataRecord> records = loadRecords(file);

            if (records.isEmpty()) {
                System.out.println("[ERROR] No valid records found in the file.");
                return;
            }

            // Step 3 – Analyse & display
            analyzeAndDisplay(records);

        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } finally {
            input.close();
        }
    }
}
