import java.util.*;

class SimpleBurgerMeal {
    // required
    private final String bunType;
    private final String patty;

    // optional
    private final String drink;
    private final String side;
    private final List<String> toppings;

    private SimpleBurgerMeal(BurgerBuilder builder) {
        this.bunType = builder.bunType;
        this.patty = builder.patty;
        this.drink = builder.drink;
        this.side = builder.side;
        this.toppings = builder.toppings;
    }

    @Override
    public String toString() {
        return "SimpleBurgerMeal{" +
                "bunType='" + bunType + '\'' +
                ", patty='" + patty + '\'' +
                ", drink='" + drink + '\'' +
                ", side='" + side + '\'' +
                ", toppings=" + toppings +
                '}';
    }

    public static class BurgerBuilder {
        // required
        private final String bunType;
        private final String patty;

        // optional
        private String drink;
        private String side;
        private List<String> toppings;

        public BurgerBuilder(String bunType, String patty) {
            this.bunType = bunType;
            this.patty = patty;
        }

        public BurgerBuilder withDrink(String drink) {
            this.drink = drink;
            return this;
        }

        public BurgerBuilder withSide(String side) {
            this.side = side;
            return this;
        }

        public BurgerBuilder withToppings(List<String> toppings) {
            this.toppings = toppings;
            return this;
        }

        public SimpleBurgerMeal build() {
            return new SimpleBurgerMeal(this);
        }
    }
}

public class Builder {
    public static void main(String args[]) {
        SimpleBurgerMeal burger1 = new SimpleBurgerMeal.BurgerBuilder("wheat", "veg").build();
        SimpleBurgerMeal burger2 = new SimpleBurgerMeal.BurgerBuilder("wheat", "nonveg")
                .withSide("Fries")
                .withDrink("Cola")
                .build();

        System.out.println("Burger 1: " + burger1);
        System.out.println("Burger 2: " + burger2);
    }
}