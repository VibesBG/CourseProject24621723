package commands;

import model.*;
import operations.AddOperation;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class FileManager {

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

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }

    private static String unesc(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|").replace("\\\\", "\\");
    }
}