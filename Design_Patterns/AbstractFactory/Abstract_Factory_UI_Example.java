package Week_2;
// Abstract Factory Pattern - UI Theme Example (Single File)

// ================= PRODUCT INTERFACES =================
interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}

// ================= LIGHT THEME PRODUCTS =================
class LightButton implements Button {
    public void paint() {
        System.out.println("Rendering Light Button");
    }
}

class LightCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering Light Checkbox");
    }
}

// ================= DARK THEME PRODUCTS =================
class DarkButton implements Button {
    public void paint() {
        System.out.println("Rendering Dark Button");
    }
}

class DarkCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering Dark Checkbox");
    }
}

// ================= ABSTRACT FACTORY =================
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// ================= CONCRETE FACTORIES =================
class LightThemeFactory implements UIFactory {

    public Button createButton() {
        return new LightButton();
    }

    public Checkbox createCheckbox() {
        return new LightCheckbox();
    }
}

class DarkThemeFactory implements UIFactory {

    public Button createButton() {
        return new DarkButton();
    }

    public Checkbox createCheckbox() {
        return new DarkCheckbox();
    }
}

// ================= CLIENT =================
public class Abstract_Factory_UI_Example {

    public static void main(String[] args) {

        // 🔁 Switch entire UI theme here
        UIFactory factory = new LightThemeFactory();
        // UIFactory factory = new DarkThemeFactory();

        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();

        button.paint();
        checkbox.paint();
    }
}