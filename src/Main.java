import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    private static final int EXIT = 5;
    private static Map<String, Integer> inventory = new TreeMap<String, Integer>();
    private static List<CoffeeOrder> orders = new ArrayList<CoffeeOrder>();
    private static String logFile = "OrderLog.txt";
    private static String inventoryFile = "Inventory.txt";

    public static void main(String[] args) {
        inventory = readInventory(inventoryFile);

        System.out.println("Welcome to Java Coffee Co.!");
        try (Scanner input = new Scanner(System.in)) {

            int option = -1;
            do {
                System.out.println("Select an option:");
                System.out.println("\t1. New Order");
                System.out.println("\t2. Reload Inventory");
                System.out.println("\t3. Update Inventory");
                System.out.println("\t4. Update Order Log");
                System.out.println("\t5. Exit Application");

                do {
                    if (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    } else {
                        option = input.nextInt();
                        if (option < 1 || option > 5) System.out.println("Please enter a valid option.");
                    }

                    switch (option) {
                        case 1:
                            addOrder(input, orders);
                            break;
                        case 2:
                            inventory = new TreeMap(readInventory(inventoryFile));
                            printInventory(inventory);
                            break;
                        case 3:
                            writeInventory(inventoryFile);
                            break;
                        case 4:
                            writeOrderLog(logFile);
                            break;
                    }
                } while (option < 1 || option > 5);
            } while (option != EXIT);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (orders.size() > 0) writeOrderLog(logFile);
    }

    private static void addOrder(Scanner input, List<CoffeeOrder> orders) {
        boolean addOrder;
        do {
            CoffeeOrder order = buildOrder();
            orders.add(order);
            System.out.println(order.printOrder());

            System.out.println("\nWould you like to enter another order (Y or N)?");
            String yn = input.nextLine();
            while (!(yn.equalsIgnoreCase("N") || yn.equalsIgnoreCase("Y"))) {
                System.out.println("Please enter Y or N.");
                yn = input.nextLine();
            }
            addOrder = !yn.equalsIgnoreCase("N");
        } while (addOrder);
    }

    private static CoffeeOrder buildOrder() {
        CoffeeOrder order = new CoffeeOrder();
        try {
            Scanner input = new Scanner(System.in);
            boolean addCoffee = true;
            while (addCoffee) {
                // prompt user to select base coffee type
                System.out.println("Select coffee type:");
                System.out.println("\t1. Black Coffee");
                System.out.println("\t2. Espresso");
                System.out.println("\t3. Barista's Choice");
                Coffee coffee;

                int option = 0;
                while (option < 1 || option > 3) {
                    if (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    } else {
                        option = input.nextInt();
                        if (option < 1 || option > 3) System.out.println("Please enter a valid option.");
                    }
                }
                input.nextLine(); // nextInt() doesn't consume newline
                if (option == 3) {      //this is the barista's choice
                    coffee = buildBaristaChoice();
                    if (coffee == null) {
                        System.out.println("Sorry, ingredients below threshold for barista's choice");
                        continue;
                    }
                } else if (option == 2) {
                    // Espresso is a specific case
                    if (!isInInventory("Espresso")) {
                        System.out.println("Sorry, no Espresso available.");
                        continue;
                    }
                    decreaseIngredientCount(Ingredient.ESPRESSO);
                    coffee = new Espresso();
                } else {
                    if (!isInInventory("Black Coffee")) {
                        System.out.println("Sorry, no Black Coffee available.");
                        continue;
                    }
                    // make BlackCoffee the default case
                    decreaseIngredientCount(Ingredient.BLACK_COFFEE);
                    coffee = new BlackCoffee();
                }

                // prompt user for any customizations
                while (option != 0 && option != 3) {
                    System.out.println(String.format("Coffee brewing: %s.", coffee.printCoffee()));
                    System.out.println("Would you like to add anything to your coffee?");
                    System.out.println("\t1. Flavored Syrup");
                    System.out.println("\t2. Hot Water");
                    System.out.println("\t3. Milk");
                    System.out.println("\t4. Sugar");
                    System.out.println("\t5. Whipped Cream");
                    System.out.println("\t0. NO - Finish Coffee");

                    while (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    }
                    option = input.nextInt();
                    input.nextLine();
                    coffee = switch (option) {
                        case 1 -> {
                            System.out.println("Please select a flavor:");
                            for (WithFlavor.Syrup flavor : WithFlavor.Syrup.values()) {
                                System.out.println("\t" + String.format("%d. %s", flavor.ordinal() + 1, flavor));
                            }
                            int max = WithFlavor.Syrup.values().length;
                            option = 0;
                            while (option < 1 || option > max) {
                                if (!input.hasNextInt()) {
                                    System.out.println("Please enter a valid number.");
                                    input.nextLine();
                                } else {
                                    option = input.nextInt();
                                    if (option < 1 || option > max) System.out.println("Please enter a valid option.");
                                }
                            }
                            input.nextLine();
                            WithFlavor.Syrup syrup = getSyrup(option);
                            Ingredient syrupIngredient = WithFlavor.getSyrupIngredient(syrup);
                            if (!isInInventory(syrupIngredient)) {
                                System.out.println("Sorry, no " + syrup + " syrup available.");
                                yield coffee;
                            }

                            decreaseIngredientCount(syrupIngredient);
                            yield new WithFlavor(coffee, syrup);
                        }
                        case 2 -> {
                            if (!isInInventory(Ingredient.HOT_WATER)) {
                                System.out.println("Sorry, no Hot Water available.");
                                yield coffee;
                            }
                            decreaseIngredientCount(Ingredient.HOT_WATER);
                            yield new WithHotWater(coffee);
                        }

                        case 3 -> {
                            if (!isInInventory(Ingredient.MILK)) {
                                System.out.println("Sorry, no Milk available.");
                                yield coffee;
                            }
                            decreaseIngredientCount(Ingredient.MILK);
                            yield new WithMilk(coffee);
                        }
                        case 4 -> {
                            if (!isInInventory(Ingredient.SUGAR)) {
                                System.out.println("Sorry, no Sugar available.");
                                yield coffee;
                            }
                            decreaseIngredientCount(Ingredient.SUGAR);
                            yield new WithSugar(coffee);
                        }
                        case 5 -> {
                            if (!isInInventory(Ingredient.WHIPPED_CREAM)) {
                                System.out.println("Sorry, no Whipped Cream available.");
                                yield coffee;
                            }
                            decreaseIngredientCount(Ingredient.WHIPPED_CREAM);
                            yield new WithWhippedCream(coffee);
                        }
                        default -> {
                            if (option != 0) System.out.println("Please enter valid option.");
                            yield coffee;
                        }
                    };
                }
                order.addCoffee(coffee);

                System.out.println("Would you like to order another coffee (Y or N)?");
                String yn = input.nextLine();
                while (!(yn.equalsIgnoreCase("N") || yn.equalsIgnoreCase("Y"))) {
                    System.out.println("Please enter Y or N.");
                    yn = input.nextLine();
                }
                addCoffee = !yn.equalsIgnoreCase("N");
            }
        } catch (Exception e) {
            System.out.println("Error building order: " + e.getMessage());
        }
        return order;
    }

    public static WithFlavor.Syrup getSyrup(int i) {
        if (i == 1) {
            return WithFlavor.Syrup.CARAMEL;
        }
        else if (i == 2) {
            return WithFlavor.Syrup.MOCHA;
        }
        else if (i == 3) {
            return WithFlavor.Syrup.VANILLA;
        }
        else {
            throw new RuntimeException("Invalid flavor");
        }
    }

    private static Map<String, Integer> readInventory(String filePath) {
        Map<String, Integer> inventory = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] parts = line.split(" = ");
                inventory.put(parts[0], Integer.valueOf(parts[1]));
            }
        } catch (Exception e) {
            System.out.println("Error reading inventory: " + e.getMessage());
        }

        return inventory;
    }

    private static void writeInventory(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                writer.write(String.format("%s = %d", entry.getKey(), entry.getValue()));
                writer.newLine();
            }
            //inventory.clear();
            System.out.println("Inventory file saved successfully");
        } catch (Exception e) {
            System.out.println("Error writing inventory: " + e.getMessage());
        }
    }

    private static void printInventory(Map<String, Integer> inventory) {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    private static List<CoffeeOrder> readOrderLog(String filePath) {
        return null;
    }

    private static void writeOrderLog(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (CoffeeOrder order : orders) {
                writer.write(order.printOrder());
                writer.newLine();
            }
            orders.clear();
            System.out.println("Order log successfully updated.");
        } catch (Exception e) {
            System.out.println("Error writing order log: " + e.getMessage());
        }
    }

    private static boolean isInInventory(Ingredient ingredient) {
        return isInInventory(ingredient.toString());
    }

    private static boolean isInInventory(String i) {
        if (inventory.containsKey(i)) {
            Integer value = inventory.get(i);
            if (value > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInInventory(int i) {
        Ingredient ingredient = Ingredient.getById(i);
        return isInInventory(ingredient);
    }

    private static void decreaseIngredientCount(Ingredient ingredient) {
        int decreasedCount = inventory.get(ingredient.toString()) - 1;
        inventory.put(ingredient.toString(), decreasedCount);
    }

    private static Coffee buildBaristaChoice() {
        Coffee coffee;
        Random rand = new Random();
        int randNum = -1;
        int randOption = rand.nextInt(2);
        String coffeeBase = null;

        if (randOption == 0 && isInInventory("Black Coffee")) {
            coffee = new BlackCoffee();
            decreaseIngredientCount(Ingredient.BLACK_COFFEE);
            coffeeBase = "Black Coffee";
        }
        else if (randOption == 1 && isInInventory("Espresso")) {
            coffee = new Espresso();
            decreaseIngredientCount(Ingredient.ESPRESSO);
            coffeeBase = "Espresso";
        }
        else {
            return null;
        }

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            int amount = entry.getValue();
            if (amount < 2) {
                return null;
            }
        }

        randNum = rand.nextInt(7);
        String coffeeDec1 = String.valueOf((Ingredient.getById(randNum)));
        coffee = getRandDecorators(coffee, randNum);
        decreaseIngredientCount(Ingredient.getById(randNum));
        int prevRandNum = randNum;

        do {
            randNum = rand.nextInt(7);
        } while (randNum == prevRandNum);
        String coffeeDec2 = String.valueOf((Ingredient.getById(randNum)));
        coffee = getRandDecorators(coffee, randNum);
        decreaseIngredientCount(Ingredient.getById(randNum));

        System.out.printf("Your order is: %s with %s and %s\n", coffeeBase, coffeeDec1, coffeeDec2);

        return coffee;
    }

    private static Coffee getRandDecorators(Coffee coffee, int option) {

        return switch (option) {
            case 0 -> new WithFlavor(coffee, getSyrup(1));
            case 1 -> new WithFlavor(coffee, getSyrup(2));
            case 2 -> new WithFlavor(coffee, getSyrup(3));
            case 3 -> new WithHotWater(coffee);
            case 4 -> new WithMilk(coffee);
            case 5 -> new WithSugar(coffee);
            case 6 -> new WithWhippedCream(coffee);
            default -> coffee;
        };
    }
}