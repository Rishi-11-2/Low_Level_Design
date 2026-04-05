import java.util.*;


interface Pizza{

    void getDescription();
    int getCost();
}


class PlainPizza implements Pizza{

    public void getDescription()
    {
        System.out.println("Plain pizza");
    }

    public int getCost()
    {
        return 50;
    }

}
class MargheritaPizza implements Pizza{


    public void getDescription()
    {
        System.out.println("Margherita Pizaa");
    }

    public int getCost()
    {
        return 100;
    }
}

abstract class PizzaDecorator implements Pizza{

    protected Pizza pizza;

    PizzaDecorator(Pizza pizza)
    {
        this.pizza = pizza;
    }
}

class CheeseDecorator extends PizzaDecorator{



    CheeseDecorator(Pizza pizza)
    {
        super(pizza);
    }

    public void getDescription()
    {
        pizza.getDescription();
        System.out.println("Extra Cheese");
    }
    
    public int getCost()
    {
        return pizza.getCost() + 50;
    }
}

class OliveDecorator extends PizzaDecorator{


    OliveDecorator(Pizza pizza)
    {
        super(pizza);
    }

    public void getDescription()
    {
        pizza.getDescription();
        System.out.println("Extra olives");
    }

    public int getCost()
    {
        return pizza.getCost() + 40;
    }
}


class Decorator{

    public static void main(String args[])
    {
        Pizza cm = new CheeseDecorator(new MargheritaPizza());

        System.out.println(cm.getCost());

        Pizza op = new OliveDecorator(new PlainPizza());

        System.out.println(op.getCost());


        Pizza opc = new OliveDecorator(new CheeseDecorator(new PlainPizza()));

        System.out.println(opc.getCost());

    }
}