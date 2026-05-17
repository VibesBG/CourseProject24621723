package model;

/**
 * Location inside the storage
 * Stores section name, rack index, and slot position
 */
public class Location {

    /** Section marker or area label */
    private final String section;

    /** Shelf level or rack index number */
    private final int rack;

    /** Specific space or slot number on the rack */
    private final int slot;

    /**
     * Main constructor validating the variables
     * @param section section String
     * @param rack rack int
     * @param slot slot int
     * @throws IllegalArgumentException if parameters are negative or blank
     */
    public Location(String section, int rack, int slot) {
        if (section == null || section.isBlank())
            throw new IllegalArgumentException("Section cannot be empty.");
        if (rack < 0) throw new IllegalArgumentException("Rack cannot be negative.");
        if (slot < 0) throw new IllegalArgumentException("Slot cannot be negative.");

        this.section = section;
        this.rack = rack;
        this.slot = slot;
    }

    /**
     * Gets section
     * @return section
     */
    public String getSection() {
        return section;
    }

    /**
     * Gets rack
     * @return rack
     */
    public int getRack() {
        return rack;
    }

    /**
     * Gets slot number
     * @return slot number
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Returns a string of a Location
     * @return formatted string
     */
    @Override
    public String toString() {
        return section + "/" + rack + "/" + slot;
    }

    /**
     * Takes a string and creates a location object
     * @param s string
     * @return new Location
     * @throws IllegalArgumentException if string structure doesn't match
     */
    public static Location fromString(String s) {
        String[] parts = s.trim().split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid location: " + s);
        }
        return new Location(parts[0].trim(), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()));
    }

    /**
     * Comparing method
     * @param o other target object
     * @return match flag outcome
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location other)) return false;
        return rack == other.rack && slot == other.slot && section.equals(other.section);
    }

    /**
     * Generates a numeric value for lookup collections
     * @return calculated integer hash
     */
    @Override
    public int hashCode() {
        int h = section.hashCode();
        h = 31 * h + rack;
        h = 31 * h + slot;
        return h;
    }
}