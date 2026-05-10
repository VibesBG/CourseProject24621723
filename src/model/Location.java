package model;

public class Location {

    private String section;
    private int rack;
    private int slot;

    public Location(String section, int rack, int slot) {
        if (section == null || section.isBlank())
            throw new IllegalArgumentException("Section cannot be empty.");
        if (rack < 0) throw new IllegalArgumentException("Rack cannot be negative.");
        if (slot < 0) throw new IllegalArgumentException("Slot cannot be negative.");

        this.section = section;
        this.rack = rack;
        this.slot = slot;
    }

    public String getSection() {
        return section;
    }

    public int getRack() {
        return rack;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return section + "/" + rack + "/" + slot;
    }

    public static Location fromString(String s) {
        String[] parts = s.trim().split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid location: " + s);
        }
        return new Location(parts[0].trim(), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location other)) return false;
        return rack == other.rack && slot == other.slot && section.equals(other.section);
    }

    @Override
    public int hashCode() {
        int h = section.hashCode();
        h = 31 * h + rack;
        h = 31 * h + slot;
        return h;
    }
}