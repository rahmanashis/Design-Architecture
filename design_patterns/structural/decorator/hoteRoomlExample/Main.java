package design_patterns.structural.decorator.hoteRoomlExample;

import week_1.structural.decorator.hoteRoomlExample.Room;-pattern.decorator.hoteRoomlExample.Room;

public class Main {
    public static void main(String[] args) {
        Room basicRoom = new design_patterns.structural.decorator.hoteRoomlExample.BasicRoom();
        System.out.println("Description: " + basicRoom.getDescription());
        System.out.println("Cost: " + basicRoom.getCost());

        Room extraBedRoom = new design_patterns.structural.decorator.hoteRoomlExample.ExtraBedRoom(basicRoom);
        System.out.println("Description: " + extraBedRoom.getDescription());
        System.out.println("Cost: " + extraBedRoom.getCost());
    }
}
