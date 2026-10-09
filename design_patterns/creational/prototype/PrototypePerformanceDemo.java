public class PrototypePerformanceDemo {

    // Prototype Interface
    interface CharacterPrototype {
        CharacterPrototype clone();
    }


    // Concrete Prototype
    static class GameCharacter implements CharacterPrototype {

        private String name;
        private byte[] heavyTexture;   // simulate large memory object
        private byte[] aiModel;        // simulate heavy AI model

        public GameCharacter(String name) {
            this.name = name;

            // Simulate expensive creation
            simulateHeavyInitialization();

            // Simulate heavy memory allocation
            this.heavyTexture = new byte[5 * 1024 * 1024]; // 5 MB
            this.aiModel = new byte[3 * 1024 * 1024];      // 3 MB
        }

        // Copy constructor (used by Prototype)
        private GameCharacter(GameCharacter character) {
            this.name = character.name;
            this.heavyTexture = character.heavyTexture; // shared reference (shallow)
            this.aiModel = character.aiModel;           // shared reference (shallow)
        }

        @Override
        public CharacterPrototype clone() {
            return new GameCharacter(this);
        }

        private void simulateHeavyInitialization() {
            try {
                Thread.sleep(50); // simulate heavy setup cost
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // Utility Methods
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static void main(String[] args) {

        int objectCount = 20;

        System.out.println("===== NORMAL OBJECT CREATION =====");

        long memBeforeNormal = getUsedMemory();
        long startNormal = System.currentTimeMillis();

        GameCharacter[] normalCharacters = new GameCharacter[objectCount];
        for (int i = 0; i < objectCount; i++) {
            normalCharacters[i] = new GameCharacter("Normal-" + i);
        }

        long endNormal = System.currentTimeMillis();
        long memAfterNormal = getUsedMemory();

        System.out.println("Time taken (normal): " + (endNormal - startNormal) + " ms");
        System.out.println("Memory used (normal): " +
                (memAfterNormal - memBeforeNormal) / (1024 * 1024) + " MB");

        System.out.println("\n===== PROTOTYPE CLONING =====");

        long memBeforePrototype = getUsedMemory();
        long startPrototype = System.currentTimeMillis();

        // Create base prototype ONCE
        GameCharacter basePrototype = new GameCharacter("Base");

        GameCharacter[] clonedCharacters = new GameCharacter[objectCount];
        for (int i = 0; i < objectCount; i++) {
            clonedCharacters[i] = (GameCharacter) basePrototype.clone();
            clonedCharacters[i].setName("Clone-" + i);
        }

        long endPrototype = System.currentTimeMillis();
        long memAfterPrototype = getUsedMemory();

        System.out.println("Time taken (prototype): " + (endPrototype - startPrototype) + " ms");
        System.out.println("Memory used (prototype): " +
                (memAfterPrototype - memBeforePrototype) / (1024 * 1024) + " MB");

        System.out.println("\n===== SUMMARY =====");
        System.out.println("Time saved: " +
                ((endNormal - startNormal) - (endPrototype - startPrototype)) + " ms");

        System.out.println("Memory saved: " +
                ((memAfterNormal - memBeforeNormal) -
                        (memAfterPrototype - memBeforePrototype)) / (1024 * 1024) + " MB");
    }
}

/*
Sprinboot Developer
prototype scope

SnakeAndLadder
TicTacToe



*/