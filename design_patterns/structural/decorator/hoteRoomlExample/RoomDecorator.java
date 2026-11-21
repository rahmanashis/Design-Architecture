package design_patterns.structural.decorator.hoteRoomlExample;

public abstract class RoomDecorator implements Room {
    protected Room room;

    public RoomDecorator(Room room) {
        this.room = room;
    }

    @Override
    public String getDescription() {
        return room.getDescription();
    }

    @Override
    public double getCost() {
        return room.getCost();
    }

}
