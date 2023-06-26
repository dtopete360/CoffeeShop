public enum Ingredient {

    CARAMEL_SYRUP("CARAMEL Syrup", 0),
    MOCHA_SYRUP("MOCHA Syrup", 1),
    VANILLA_SYRUP("VANILLA Syrup", 2),
    HOT_WATER("Hot Water", 3),
    MILK("Milk", 4),
    SUGAR("Sugar", 5),
    WHIPPED_CREAM("Whipped Cream", 6),
    BLACK_COFFEE("Black Coffee", 7),
    ESPRESSO("Espresso", 8);

    public final String label;
    public final int id;

    private Ingredient(String label, int id) {
        this.label = label;
        this.id = id;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Ingredient getById(int id) {
        return switch (id) {
            case 0 -> Ingredient.CARAMEL_SYRUP;
            case 1 -> Ingredient.MOCHA_SYRUP;
            case 2 -> Ingredient.VANILLA_SYRUP;
            case 3 -> Ingredient.HOT_WATER;
            case 4 -> Ingredient.MILK;
            case 5 -> Ingredient.SUGAR;
            case 6 -> Ingredient.WHIPPED_CREAM;
            case 7 -> Ingredient.BLACK_COFFEE;
            case 8 -> Ingredient.ESPRESSO;
            default -> null;
        };
    }
}
