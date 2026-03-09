/*
 * PROGRAMMING 2 – MACHINE PROBLEM
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Information Technology - Game Development
 * Dataset: https://www.kaggle.com/datasets/asaniczka/video-game-sales-2024
 *
 * DataRecord.java — Represents a single row from the CSV dataset
 */

public class DataRecord {
    private String title;
    private String genre;       // used as "category"
    private double totalSales;

    public DataRecord(String title, String genre, double totalSales) {
        this.title = title;
        this.genre = genre;
        this.totalSales = totalSales;
    }

    public String getTitle()      { return title; }
    public String getGenre()      { return genre; }
    public double getTotalSales() { return totalSales; }

    @Override
    public String toString() {
        return String.format("%-50s | %-20s | %,.2f", title, genre, totalSales);
    }
}
