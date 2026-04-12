public class Main {

    public static void main(String[] args) {

        ParkingSlot slot1 = new ParkingSlot(1, false);
        ParkingSlot slot2 = new ParkingSlot(2, true);

        ParkingSlotService service = new ParkingSlotService();

        service.addSlot(slot1);
        service.addSlot(slot2);
    }
}