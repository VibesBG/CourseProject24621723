package commands;

import model.*;
import operations.AddOperation;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles saving and loading of the storage to and from a text file
 * The file format is line-based, fields separated by '|'
 * Each line starts with PRODUCT or LOG to mark its kind
 */
public class FileManager {

    /**
     * Utility class, not meant to be instantiated
     */
    private FileManager() {
    }

    /**
     * Saves the storage contents to the given file
     * Writes all products first, then the log entries
     * @param storage the storage to write
     * @param filePath the path of the file to write to
     * @throws IOException if a write error occurs
     */
    public static void save(Storage storage, String filePath) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Product> list : storage.getStock().values()) {
                for (Product p : list) {
                    w.write(productLine(p));
                    w.newLine();
                }
            }
            for (LogEntry e : storage.getLogEntries()) {
                w.write(logLine(e));
                w.newLine();
            }
        }
    }

    /**
     * Loads the storage contents from the given file
     * If the file does not exist, the storage is just cleared
     * @param storage the storage to fill
     * @param filePath path of the file to read
     * @throws IOException if a read error occurs
     * @throws IllegalStateException if a line cannot be parsed
     */
    public static void load(Storage storage, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            storage.clear();
            return;
        }
        storage.clear();
        try (BufferedReader r = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNo = 0;
            while ((line = r.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    if (line.startsWith("PRODUCT" + "|")) {
                        parseProduct(storage, line);
                    } else if (line.startsWith("LOG" + "|")) {
                        parseLog(storage, line);
                    } else {
                        throw new IllegalStateException(
                                "Invalid format at line " + lineNo + ": " + line);
                    }
                } catch (IllegalStateException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "Could not parse line " + lineNo + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Builds the file line for a Product
     * @param p product to serialize
     * @return the formatted line
     */
    private static String productLine(Product p) {
        return String.join("|",
                "PRODUCT",
                esc(p.getName()),
                esc(p.getManufacturerName()),
                p.getMeasureUnit().name(),
                String.valueOf(p.getPricePerUnit()),
                p.getExpireDate().toString(),
                p.getDateOfEnter().toString(),
                String.valueOf(p.getQuantity()),
                esc(p.getComment()),
                p.getLocation().toString()
        );
    }

    /**
     * Builds the file line for a LogEntry
     * @param e log entry to serialize
     * @return the formatted line
     */
    private static String logLine(LogEntry e) {
        return String.join("|",
                "LOG",
                e.getDate().toString(),
                e.getType().name(),
                esc(e.getProductName()),
                String.valueOf(e.getQuantity()),
                e.getUnit().name(),
                e.getLocation().toString(),
                e.getExpiryDate().toString(),
                esc(e.getNote())
        );
    }

    /**
     * Parses a PRODUCT line and adds the product to the storage
     * @param storage the storage to add to
     * @param line the file line to parse
     */
    private static void parseProduct(Storage storage, String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 10)
            throw new IllegalStateException("PRODUCT line has too few fields.");

        Location location = Location.fromString(p[9]);
        Product product = new Product.Builder(unesc(p[1]))
                .manufacturer(unesc(p[2]))
                .unit(p[3])
                .pricePerUnit(Double.parseDouble(p[4]))
                .expiryDate(LocalDate.parse(p[5]))
                .arrivalDate(LocalDate.parse(p[6]))
                .quantity(Double.parseDouble(p[7]))
                .comment(unesc(p[8]))
                .build();

        AddOperation addOp = new AddOperation(product, location);
        addOp.execute(storage);
    }

    /**
     * Parses a LOG line and appends it to the storage log
     * @param storage the storage whose log will be filled
     * @param line the file line to parse
     */
    private static void parseLog(Storage storage, String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 9)
            throw new IllegalStateException("LOG line has too few fields.");

        storage.getLogEntries().add(new LogEntry(
                LocalDate.parse(p[1]),
                LogEntry.Type.valueOf(p[2]),
                unesc(p[3]),
                Double.parseDouble(p[4]),
                MeasureUnit.fromString(p[5]),
                Location.fromString(p[6]),
                LocalDate.parse(p[7]),
                unesc(p[8])
        ));
    }

    /**
     * Escapes the '|' and '\' characters so they don't break the format
     * @param s string to escape
     * @return escaped string
     */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Reverses the escape operation
     * @param s string to unescape
     * @return unescaped string
     */
    private static String unesc(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|").replace("\\\\", "\\");
    }
}
