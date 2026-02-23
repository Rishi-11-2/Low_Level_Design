import java.util.*;
class BurgerMeal{

    // required

    private final String bunType;
    private final String patty ;

    // optional

    private final String drink;
    private final String side;
    private final List<String>toppings;

   private BurgerMeal(BurgerBuilder builder)
    {
        this.bunType = builder.bunType;
        this.patty = builder.patty;
        this.drink = builder.drink;
        this.side = builder.side;
        this.toppings = builder.toppings;
    }
    public static class BurgerBuilder{

        // required
        private final String bunType;
        private final String patty ;

        // optional

        private  String drink;
        private  String side;
        private  List<String>toppings;
        public BurgerBuilder(String bunType,String patty)
        {
            this.bunType = bunType;
            this.patty = patty;
        }

        public BurgerBuilder  getDrink(String drink)
        {
            this.drink= drink;
            return this;
        }

        public BurgerBuilder getSide(String side)
        {
            this.side = side;
            return this;
        }

        public BurgerBuilder getToppings(List<String>toppings)
        {
            this.toppings = toppings;
            return this;
        }
        public BurgerMeal build()
        {
            return new BurgerMeal(this);
        }
    }
}

public class Builder{

    public static void main(String agrs[])
    {
        BurgerMeal burger1 = new BurgerMeal.BurgerBuilder("wheat", "veg").build();
        BurgerMeal burger2 = new BurgerMeal.BurgerBuilder("wheat", "nonveg").getSide("Fries").getDrink("Cola").build();

        System.out.println();
    }
}