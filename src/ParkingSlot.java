public class ParkingSlot {

    private int slotNumber;
    private boolean isOccupied;

    public ParkingSlot(int slotNumber, boolean isOccupied) {
        this.slotNumber = slotNumber;
        this.isOccupied = isOccupied;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}