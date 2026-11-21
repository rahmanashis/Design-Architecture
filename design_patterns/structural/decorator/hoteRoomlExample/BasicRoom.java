package design_patterns.structural.decorator.hoteRoomlExample;

import week_1.structural.decorator.hoteRoomlExample.Room;

public class BasicRoom implements Room {
    @Override
    public String getDescription() {
        return "Basic Hotel Room";
}

    @Override
    public double getCost() {
        return 100.0;
    }
}
