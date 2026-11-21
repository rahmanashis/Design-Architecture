package design_patterns.structural.decorator.hoteRoomlExample;

import week_1.structural.decorator.hoteRoomlExample.Room;

public class ExtraBedRoom extends design_patterns.structural.decorator.hoteRoomlExample.RoomDecorator {
    public ExtraBedRoom(Room room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", with Extra Bed";
    }

    @Override
    public double getCost() {
        return room.getCost() + 800.0;
    }
}
