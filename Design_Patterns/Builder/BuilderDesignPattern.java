package Week_2;

class User {
    private final String name;
    private final int age;
    private final String phone;
    private final String address;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age +
                ", phone='" + phone + "', address='" + address + "'}";
    }

    // Step 2: Builder class
    static class UserBuilder {
        private String name;        // required
        private int age;            // optional
        private String phone;       // optional
        private String address;     // optional

        public UserBuilder(String name) {
            this.name = name;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

public class BuilderDesignPattern {
    public static void main(String[] args) {
        User user = User.UserBuilder("pradeep").
    }
}