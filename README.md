# CoffeeShop
 Lab 6 CS160

# How to run application
 Open source code in IntelliJ IDE
 Select Run on IDE software
 After **Main Menu** has been loaded, choose between menu options 1-5


## Menu Option 1 (New Order)
 If menu option 1 is selected, Coffee options will be printed
 After Coffee options have been printed, choose between options 1-3
 Option 1 will create new **Black Coffee**, option 2 will create new **Espresso**, and option 3 will create new **Barista's Choice**

### New Order (Black Coffee and Espresso)
 After Coffee is created, a new menu will pop up with different coffee decorators
 After Decorator options have been printed, choose between options 1-5
 If any Decorator is chosen, it will be added to the Coffee, you can choose as many Decorators as desirable
 If option 1 (Flavored Syrup) is selected, another menu will pop up
 The **Flavored Syrup** menu holds 3 options, choose between 1-3 to add those flavored syrup Decorators to Coffee
 If you wish to not add any Decorators or are finished, select option 0 to finish Coffee

 If you wish to add another Coffee to the Order, select Y or y to do so and repeate process above, if not select N or n
 If N or n is selected, **ORDER RECEIPT** will be printed showing all Coffees with their Decorators and costs plus total cost
 If you would like to add another Order, select Y or y and repeat process above, if not select N or n
 If N or n is selected, **Main Menu** will be printed again

### New Order (Barista's Choice)
 If option 3 is selected from **New Order**, **Barista's Choice** will be created
 **Barista's Choice** is a random Coffee with 2 random Decorators
 The Barista's Choice will be printed and added to the order
 
 If you wish to add another Coffee to the Order, select Y or y to do so and repeate process above, if not select N or n
 If N or n is selected, **ORDER RECEIPT** will be printed showing all Coffees with their Decorators and costs plus total cost
 If you would like to add another Order, select Y or y and repeat process above, if not select N or n
 If N or n is selected, **Main Menu** will be printed again


## Menu Option 2 (Reload Inventory)
 If option 2 is selected, **Inventory.txt** will be printed
 **Inventory.txt** contains all **Ingredients** and their quantities
 If any **Ingredients** are used and **Inventory.txt** is updated, **Ingredients** quantities in **Inventory.txt** will change

## Menu Option 3 (Update Inventory)
 If option 3 is selected, **Inventory.txt** will be updated
 This will change the quantities of **Ingredients** based on how many of those **Ingredients** have been used for coffees

## Menu Option 4 (Update Order Log)
 If option 4 is selected, **OrderLog.txt** will be updated
 **OrderLog.txt** is where all Coffee Order Receipts are documented

## Menu Option 5 (Exit Application)
 If option 5 is selected, IntelliJ IDE will terminate the application and stop running the code
