import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WithFlavor extends CoffeeDecorator {

    enum Syrup {
        CARAMEL,
        MOCHA,
        VANILLA
    }

    private Syrup flavor;

    public WithFlavor(Coffee c, Syrup s) {
        super(c);
        flavor = s;
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.35;
    }

    @Override
    public List<String> getIngredients() {
        List<String> ingredients = new ArrayList<>(super.getIngredients());
        ingredients.add(getSyrupIngredient(flavor).toString());
        return ingredients;
    }

    @Override
    public String printCoffee() {
        return super.printCoffee() + " with " + flavor;
    }

    public static Ingredient getSyrupIngredient(WithFlavor.Syrup syrup) {
        return switch (syrup) {
            case CARAMEL -> Ingredient.CARAMEL_SYRUP;
            case VANILLA -> Ingredient.VANILLA_SYRUP;
            case MOCHA -> Ingredient.MOCHA_SYRUP;
        };
    }
}
