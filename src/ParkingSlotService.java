import java.io.FileWriter;
import java.io.IOException;

public class ParkingSlotService {

    public void addSlot(ParkingSlot slot) {

        try {
            FileWriter writer = new FileWriter("slots.txt", true);

            writer.write(
                    slot.getSlotNumber() + "," +
                            slot.isOccupied() + "\n"
            );

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}