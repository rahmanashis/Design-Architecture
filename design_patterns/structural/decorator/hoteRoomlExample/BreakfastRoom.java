package design_patterns.structural.decorator.hoteRoomlExample;

public class BreakfastRoom extends RoomDecorator {
    public BreakfastRoom(design_patterns.structural.decorator.hoteRoomlExample.Room room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", with Breakfast";
    }

    @Override
    public double getCost() {
        return room.getCost() + 200.0;
    }
}
