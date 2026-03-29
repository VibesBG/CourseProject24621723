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

    public String getSection() { return section; }
    public int getRack() { return rack; }
    public int getSlot() { return slot; }

    @Override
    public String toString() {
        return section + "/" + rack + "/" + slot;
    }
}