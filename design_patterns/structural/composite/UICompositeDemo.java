package design_patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class UICompositeDemo {

    // ==========================
    // 1. Component
    // ==========================
    interface UIComponent {
        void render();
        void enable();
    }

    // ==========================
    // 2. Leaf - Button
    // ==========================
    static class Button implements UIComponent {
        private String name;

        Button(String name) {
            this.name = name;
        }

        @Override
        public void render() {
            System.out.println("Rendering Button: " + name);
        }

        @Override
        public void enable() {
            System.out.println("Enabling Button: " + name);
        }
    }

    // ==========================
    // 3. Leaf - TextBox
    // ==========================
    static class TextBox implements UIComponent {
        private String name;

        TextBox(String name) {
            this.name = name;
        }

        @Override
        public void render() {
            System.out.println("Rendering TextBox: " + name);
        }

        @Override
        public void enable() {
            System.out.println("Enabling TextBox: " + name);
        }
    }

    // ==========================
    // 4. Composite - Panel
    // ==========================
    static class Panel implements UIComponent {
        private String name;
        private List<UIComponent> children = new ArrayList<>();

        Panel(String name) {
            this.name = name;
        }

        void add(UIComponent component) {
            children.add(component);
        }

        @Override
        public void render() {
            System.out.println("Rendering Panel: " + name);
            for (UIComponent component : children) {
                component.render();
            }
        }

        @Override
        public void enable() {
            System.out.println("Enabling Panel: " + name);
            for (UIComponent component : children) {
                component.enable();
            }
        }
    }

    // ==========================
    // 5. Composite - Window
    // ==========================
    static class Window implements UIComponent {
        private String title;
        private List<UIComponent> children = new ArrayList<>();

        Window(String title) {
            this.title = title;
        }

        void add(UIComponent component) {
            children.add(component);
        }

        @Override
        public void render() {
            System.out.println("Rendering Window: " + title);
            for (UIComponent component : children) {
                component.render();
            }
        }

        @Override
        public void enable() {
            System.out.println("Enabling Window: " + title);
            for (UIComponent component : children) {
                component.enable();
            }
        }
    }

    // ==========================
    // 6. Client
    // ==========================
    public static void main(String[] args) {

        Button loginButton = new Button("Login");
        Button cancelButton = new Button("Cancel");
        TextBox usernameField = new TextBox("Username");

        Panel formPanel = new Panel("Login Form");
        formPanel.add(usernameField);
        formPanel.add(loginButton);

        Window window = new Window("Login Window");
        window.add(formPanel);
        window.add(cancelButton);

        // Treating everything uniformly
        window.render();
        System.out.println("----");
        window.enable();
    }
}