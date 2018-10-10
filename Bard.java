//-----------------------------------------------------------------------------------------------------------------------------------
// Keerthi Krishnan 
// cruz id: kvkrishn 
// This program goes through shakespeare.txt and sorts into a hashtable based on length and frequency. The output is then called to the 
// output file based on length and rank of frequency. Concepts involved were hashtables. 
//------------------------------------------------------------------------------------------------------------------------------------



import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.lang.Object;


public class Bard
{

    public static void main(String[] args) throws IOException, FileNotFoundException
    {
        //Variables
        int counter = 0; //counter 
        boolean notFreq = true; // boolean variable set to true
        String fileName = "shakespeare.txt"; // shakespeare.txt 
        String userInput = args[0]; // takes in input file
                                                
        String outputFile = args[1]; // takes in output file
        PrintWriter output = new PrintWriter(new FileWriter(outputFile)); // PrintWriter variable 
        ArrayList<String> words = new ArrayList<String>(); // holds shakespeare words unparsed 
        ArrayList<String> singleWords = new ArrayList<String>(); // all words from shakespeare parsed and whitespaced

        Hashtable<String, Integer> shakespeare  = new Hashtable<String, Integer>(); // beginning hashtable unsorted
        //new code
        ArrayList<String> userInput1 = new ArrayList<String>(); // takes in input file values 
        ArrayList<String> userInput2 = new ArrayList<String>(); // takes in input file values 
        //final answer
        Hashtable<String, Integer> finalSpeare = new Hashtable<String, Integer>(); // final hashtable sorted with length and alphabetic

        ArrayList<String> finalArray = new ArrayList<String>(); // stores all words in arraylist 
        
        userInput1 = readInput(userInput); // reads the input file 
        
        //end new code
        words = readInput(fileName); // reads in shakespeare.txt
        singleWords = getSingleWords(words); // parses shakespeare.txt

        for (int i = 0; i < singleWords.size(); i++) // goes through words in shakespeare
        {
            String item = singleWords.get(i); // gets the word from arraylist

            if (shakespeare .containsKey(item)) // if hashtable contains that key 
            {
                shakespeare .put(item, shakespeare .get(item) + 1); // put into the hashtable 
            }
            else
            {
                shakespeare .put(item, 1); // else create a frequency of one for the keys 
            }
        }
        
        
        
        for(int i = 0; i < userInput1.size(); i++)
        {
            String userLine = userInput1.get(i); // get the word from userInput and stores in string
            String[] arr = userLine.split("\\s+");   // split by whitespace and stores in array

            for ( String tempLine : arr) // traverses through the array 
            {
                userInput2.add(tempLine); // add to userInput2
            }
        }
        
        ArrayList<Integer> num1 = new ArrayList<Integer>(); // stores length values
        ArrayList<Integer> num2 = new ArrayList<Integer>(); // stores rank values 
            
        for(int i = 0; i < userInput2.size(); i++)
        {   
            num1.add(Integer.parseInt(userInput2.get(i))); // adds to arraylist 
            i++;
        }
        for(int i = 0; i < userInput2.size(); i++)
        {
            i++;
            num2.add(Integer.parseInt(userInput2.get(i))); // adds to arraylist 
        }    


            for(int i = 0; i < num1.size(); i++) //first loop through the total number of times we have to search for words
            {                                   //this will be based on the user's first numbers
                notFreq = true; //set notFreq to true by default so we can later add a "-" if no specified value was found
                

                for (String name: shakespeare .keySet()) //for each key in shakespeare (our first intial hash with all unique values stored in them)
                {
                    if(name.length() == num1.get(i)) //if the length of one of the words in this hash table equals the user's first number
                    {
                        finalSpeare.put(name, shakespeare .get(name)); // place it into our new hash table
                    }
                }

                //convert elements from hashTable to set
                Set<Entry<String, Integer>> set = finalSpeare.entrySet();
                
                //converts from set to another list
                List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
                
                
                //sort through list and compares frequency and then order alphabetically
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                {
                    public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
                    {
                        if(o2.getValue() - o1.getValue() == 0) { // if the frequencies are the same 
                                return o1.getKey().compareTo(o2.getKey()); // compare the keys alphabetically
                            }

                        return o2.getValue() - o1.getValue(); // return the difference value
                    }
                    
                } ); 
                
                // Map stores all the key and values of the hashtable
                for(Map.Entry<String, Integer> entry:list) //NOW WE LOOP THROUGH EVERY VALUE IN THIS MAP
                {
                    
                    if(counter == num2.get(i)) 
                    {                         
                        finalArray.add(entry.getKey()); // add to final arraylist that stores the final sorted words 
                        //set notFreq to false since we did end up find a value if we get into this if statement
                        notFreq = false;
                    }
                    //increment counter to keep track of our spot in the map we created
                    counter++;
                }
                if(notFreq) //if we did not get into the previous if statement, that means we could not find the user's specifications
                {
                    finalArray.add("-");
                    
                    
                }
                finalSpeare.clear();
                counter = 0; //clear counter as well so we can retrack out spot
            }

        printToOutput(finalArray, output); // prints to output file     
        
    }

    //reads input file 
    public static ArrayList<String> readInput(String fileName)
    {
        ArrayList<String> words = new ArrayList<String>();
        String line = null;
        
        try 
        {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
                words.add(line); // add to arraylist 
            }
            
            // close files 
            bufferedReader.close();         
        }

        // file not found exception and io exception if caught 
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) 
        {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
        
        return words; // return the arraylist
    }

    // parses words and whitespace in the input file 
    public static ArrayList<String> getSingleWords(ArrayList<String> words)
    {
        ArrayList<String> newWords = new ArrayList<String>();
        
        for(int i = 0; i<words.size(); i++)
        {
          String wordString = words.get(i);

            wordString = wordString.replaceAll("\\?", " ");
            wordString = wordString.replaceAll("\\,", " ");
            wordString = wordString.replaceAll("\\.", " ");
            wordString = wordString.replaceAll("\\!", " ");
            wordString = wordString.replaceAll("\\:", " ");
            wordString = wordString.replaceAll("\\;", " ");
            wordString = wordString.replaceAll("\\[", " ");
            wordString = wordString.replaceAll("\\]", " ");
            wordString = wordString.toLowerCase();
            
            String[] string1 = wordString.trim().split("\\s+");

            for (String word : string1)
            {
                newWords.add(word);
                
            
            }

        }
        
        return newWords;
    }
    
    // prints to the output file 
    public static void printToOutput(ArrayList<String> words, PrintWriter output)
    {
        for(int i = 0; i<words.size(); i++)
        {
            output.println(words.get(i));
        }
        output.close();
    }
}
