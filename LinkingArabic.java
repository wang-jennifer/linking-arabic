// This program scans through a diary links file and Arabic translations file and creates
// a new edited links file called d50_links(3).html. New version includes tooltip code that contains the translations
// for each unique id.

import java.util.*;
import java.io.*;

public class ShellScript {
   public static void main(String[] args) throws FileNotFoundException {
      File arabicFile = new File("Diary_50_Arabic_EditsStandardized_20150917 (1).html");
      Scanner arabicScanner = new Scanner(arabicFile);
      File diaryFile = new File("Diary_50_20160719_links.html");
      Scanner diaryScanner = new Scanner(diaryFile);
   
      PrintStream links = new PrintStream("D50_links(3).html");
      Map<String, String> translations = createMap(arabicScanner);
      editDoc(diaryScanner, links, translations);
      
      // addImage(diaryScanner, links);
   }
   
   // Creates a map with the keys as the IDs of each translation, and the values as the English
   // translation of the Arabic document. Assumes each ID is unique (so no duplicates).
   // parameter(s) needed:
   //    input = the Arabic document file scanner
   public static Map<String, String> createMap(Scanner input) {
      Map<String, String> idToTrans = new TreeMap<String, String>(); // [A50_..]=English translation
      while (input.hasNextLine()) {
         String line = input.nextLine();
         String result = "<td class=\"ID-cell\"> <a id=\"";
         if (line.contains("<td class=\"ID-cell\">A50")) {
            String arabicId = line.substring(line.indexOf(">") + 1, line.indexOf("</"));
            String placeholder = input.nextLine();
            while (!placeholder.contains("<td class=\"EN-cell\">")) {
               placeholder = input.nextLine();
            }
            String arabicString = placeholder.substring(placeholder.indexOf(">") + 1, placeholder.indexOf("</"));
            idToTrans.put(arabicId.substring(0, 10), arabicString);
         }
      }
      return idToTrans;
   }
   
	// Scans through input file to find IDs in links file. Adds tooltip code to each ID, and
   // uses the given map to pair similar IDs and output the translation to the tooltip code.
   // Outputs new edited file to given PrintStream.
   // parameter(s) needed:
   //    input        = the diary links document file scanner
   //    links        = the printstream for output file (new edited version of code)
   //    translations = map of IDs and its translations
   public static void editDoc(Scanner input, PrintStream links, Map<String, String> translations) {
      while(input.hasNextLine()) {
         String line = input.nextLine();
         if (line.contains("[A50_")) {
            //need to delete line and then insert above and reinsert below
            String id = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
            id = id.substring(0, 10);
            if (translations.containsKey(id)) {
               links.println("   <div class=\"tooltip\">");
               links.println("   " + line);
               links.println("      <span class=\"tooltiptext\">" + translations.get(id) + "</span>");
               links.println("   </div>");
            }
         } else {
            links.println(line);
         }
      }
   }
   
   public static void addImage(Scanner diaryScanner, PrintStream links) {
      for (int i = 1; i <= 426; i++) {
         links.print("<img src=\"");
         links.print("https://digitalcollections.lib.washington.edu/digital/api/singleitem/image/iraqdiaries/");
         links.print(i);
         links.println("/default.jpg?highlightTerms=\">");
      }
   }
}
