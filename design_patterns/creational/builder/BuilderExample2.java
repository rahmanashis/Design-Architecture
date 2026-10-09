package Week_2;
class FoodOrder {

    // Required
    private final String itemName;
    private final int quantity;

    // Optional
    private final boolean extraRaita;
    private final boolean cutlery;
    private final boolean extraSpicy;
    private final String cookingInstructions;
    private final String couponCode;

    private FoodOrder(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.extraRaita = builder.extraRaita;
        this.cutlery = builder.cutlery;
        this.extraSpicy = builder.extraSpicy;
        this.cookingInstructions = builder.cookingInstructions;
        this.couponCode = builder.couponCode;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", extraRaita=" + extraRaita +
                ", cutlery=" + cutlery +
                ", extraSpicy=" + extraSpicy +
                ", cookingInstructions='" + cookingInstructions + '\'' +
                ", couponCode='" + couponCode + '\'' +
                '}';
    }

    // Builder
    static class Builder {

        private String itemName;
        private int quantity;

        private boolean extraRaita;
        private boolean cutlery;
        private boolean extraSpicy;
        private String cookingInstructions;
        private String couponCode;

        public Builder(String itemName, int quantity) {
            this.itemName = itemName;
            this.quantity = quantity;
        }

        public Builder extraRaita(boolean value) {
            this.extraRaita = value;
            return this;
        }

        public Builder cutlery(boolean value) {
            this.cutlery = value;
            return this;
        }

        public Builder extraSpicy(boolean value) {
            this.extraSpicy = value;
            return this;
        }

        public Builder cookingInstructions(String text) {
            this.cookingInstructions = text;
            return this;
        }

        public Builder couponCode(String code) {
            this.couponCode = code;
            return this;
        }

        public FoodOrder build() {
            return new FoodOrder(this);
        }
    }
}

public class BuilderExample2 {
    public static void main(String[] args) {
        FoodOrder order = new FoodOrder.Builder("Chicken Biryani", 1)
                .extraRaita(true)
                .extraSpicy(true)
                .cutlery(false)
                .cookingInstructions("Less oil")
                .couponCode("SAVE50")
                .build();

        System.out.println(order);
    }
}