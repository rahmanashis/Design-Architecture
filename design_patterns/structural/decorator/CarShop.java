package design_patterns.structural.decorator;

import java.util.logging.Logger;

public abstract class CarShop {
    private static final Logger LOGGER = Logger.getLogger(CarShop.class.getName());

    public abstract int cost();

    public abstract String description();

    public static void main(String[] args) {
        CarShop service = new BasicService();
        service = new SyntheticOil(service);
        service = new TireRotation(service);
        service = new InteriorCleaning(service);

        LOGGER.info(service.description() + " = " + service.cost());
    }
}

class BasicService extends CarShop {
    @Override
    public int cost() {
        return 1500;
    }

    @Override
    public String description() {
        return "Basic car service";
    }
}

class FullService extends CarShop {
    @Override
    public int cost() {
        return 3500;
    }

    @Override
    public String description() {
        return "Full car service";
    }
}

class CarServiceDecorator extends CarShop {
    protected final CarShop service;

    protected CarServiceDecorator(CarShop service) {
        this.service = service;
    }

    @Override
    public int cost() {
        return service.cost();
    }

    @Override
    public String description() {
        return service.description();
    }
}

class SyntheticOil extends CarServiceDecorator {
    public SyntheticOil(CarShop service) {
        super(service);
    }

    @Override
    public int cost() {
        return service.cost() + 900;
    }

    @Override
    public String description() {
        return service.description() + ", synthetic oil";
    }
}

class TireRotation extends CarServiceDecorator {
    public TireRotation(CarShop service) {
        super(service);
    }

    @Override
    public int cost() {
        return service.cost() + 500;
    }

    @Override
    public String description() {
        return service.description() + ", tire rotation";
    }
}

class InteriorCleaning extends CarServiceDecorator {
    public InteriorCleaning(CarShop service) {
        super(service);
    }

    @Override
    public int cost() {
        return service.cost() + 700;
    }

    @Override
    public String description() {
        return service.description() + ", interior cleaning";
    }
}
