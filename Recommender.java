/*
 *   [Recommender.java]
 * 
 *  Author: Jim Gao
 *  Date: April 1st, 2015
 * 
 *  Purpose: ...
 *  Requires a file named db.txt containing the database
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Recommender{
        
        /*
         *   The item class that contains basic info
         * 
         *  itemID: The ID of the item inside the array1
         *  itemName: The name of the item
         *  itemURL: The URL for the information of the item
         *  properties: The properties of the item
         */
        
        public static class Item implements Comparable<Item>{
                /*
                 *   Fields of the items
                 */
                
                public int itemID;
                public String itemName;
                public String itemURL;
                public int[] properties;
                
                public int itemRating;
                
                public Item(){}
                
                /*
                 *   Constructor of the item
                 */
                public Item(int id, String name, String url, int[] p){
                        this.itemID = id;
                        this.itemName = name;
                        this.itemURL = url;
                        
                        this.properties = (int[]) p.clone();
                        
                        this.itemRating = 0;
                }
                
                /*
                 *     The comparator of the current item, sorting by 
                 *   preferability
                 */
                public int compareTo(Item i) {
                        return this.itemRating - i.itemRating;
                }
                
                /*
                 Overriding the toString method, making printing the
                 item more user friendly
                 */
                @Override
                public String toString() {
                        return this.itemName + " [" + this.itemURL + "]";
                }
        }
        
        /*
         *   The arrays containing 
         */
        public static ArrayList<String> questions;
        public static ArrayList<Item> items;
        
        public static final String greeting = "Welcome to the recommender. \n"
                + "This program will help you choose the perfect\n"
                + "phone for you, according to your requirements. \n"
                + "\nYou need to answer the questions, and this\n"
                + "program will generate the top 5 choices for a\n"
                + "phone.\n\n";
        
        /*
         *   The method that reads from the database file, and updates the arrays
         * 
         *   If file is not found, returns IOException
         */
        public static void updateDatabase() throws IOException{
                /*
                 *   Opening the file, and reading the number of items
                 */
                
                Scanner file = new Scanner(new File("db.txt"));
                
                int questionCount = Integer.parseInt(file.nextLine());
                
                for (int i=0; i<questionCount; i++){
                        String question = file.nextLine();
                        
                        /*
                         *   Input processing:
                         *    Removing the \\t from the file, and replacing them
                         *    with the "real" windows escape characters like \t
                         */
                        question = question.replace("\\n", "\n");
                        question = question.replace("\\t", "\t");
                        
                        questions.add(question);
                }
                
                System.out.println("Read " + questionCount + " questions.");
                
                int itemCount = Integer.parseInt(file.nextLine());
                
                /*
                 *   After reading the number of items, it starts to read
                 *   the independent properties of the items.
                 * 
                 *   The information will be stored to a ArrayList
                 */
                for (int i=0; i<itemCount; i++){
                        int id = i + 1;
                        String name = file.nextLine().replace("\n", "replaced");
                        String url = file.nextLine();
                        
                        String[] propTokens = file.nextLine().split(" ");
                        int[] prop = new int[propTokens.length];
                        
                        for (int j=0; j<propTokens.length; j++)
                                prop[j] = Integer.parseInt(propTokens[j]);
                        
                        items.add(new Item(id, name, url, prop));
                }
                
                System.out.println("Read " + itemCount + " items.");
                
                file.close();
        }
        
        /*
         *   Method that prints a divider, enhances the user experiences
         */
        public static void printDivider(){
                System.out.println("\n--------------------------------\n");
        }
        
        public static void main(String[] args){
                Scanner keyboard = new Scanner(System.in);
                
                System.out.println(greeting);
                
                questions = new ArrayList<String>();
                items = new ArrayList<Item>();
                
                /*
                 *   Reading the data from the files, or exit
                 *   if the file is not present. 
                 */
                
                try{
                        updateDatabase();
                } catch (IOException ex){
                        System.out.println("Cannot read from file.");
                        System.out.println("Terminating Program....");
                        System.exit(1);
                }
                
                for (int i=0; i<questions.size(); i++){
                        /*
                         *  Calculating the number of options to the current question.
                         */
                        
                        printDivider();
                        
                        int options = 0;
                        for (int j=0; j<questions.get(i).length(); j++)
                                if (questions.get(i).charAt(j) == '\t')
                                options ++;
                        
                        System.out.println(questions.get(i));
                        System.out.print("Your Choice:  ");
                        int response = Integer.parseInt(keyboard.nextLine());
                        
                        /*
                         *   Input validation
                         */
                        if (response <= 0 || response > options){
                                i--;
                                System.out.println("Invalid Choice.");
                                continue;
                        }
                        
                        /*
                         *   Updating the "preferability" of the items
                         */
                        for (int j=0; j<items.size(); j++){
                                if (response == items.get(j).properties[i]) items.get(j).itemRating += 1;
                        }
                }
                
                /*
                 *   Sort the items by the most suitable to the least.
                 */
                Collections.sort(items);
                
                printDivider();
                
                /*
                 *   Printing out the information of the items
                 */
                System.out.println("The top 5 items that matches your criteria(s) are: ");
                for (int i=0; i<5; i++){
                        System.out.println("\t - " + items.get(i));
                }
                
                printDivider();
                
                System.out.println("Thank you for using the recommender. ");
                
        }
}