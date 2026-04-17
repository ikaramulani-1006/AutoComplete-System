// This is Auto Complete System - 'Helper' which will suggest words based on the prefix enterd by the user.

//1012411238: Tanishka
import java.util.*;
//We are Using HashMap for children 
class TrieNode {
    java.util.Map<Character, TrieNode> children = new java.util.HashMap<>();
    boolean isEnd = false;
    int frequency = 0;
}

class Trie {
    TrieNode root = new TrieNode();
    int wordCount = 0;

    boolean insert(String word) {
        word = word.toLowerCase().trim();
        if (word.isEmpty()) return false;
        
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        
        if (!node.isEnd) {
            node.isEnd = true;
            node.frequency = 1;
            wordCount++;
            return true;
        }
        return false;
    }

    boolean search(String word) {
        word = word.toLowerCase().trim();
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!node.children.containsKey(ch)) return false;
            node = node.children.get(ch);
        }
        return node.isEnd;
    }

    boolean startsWith(String prefix) {
        prefix = prefix.toLowerCase().trim();
        TrieNode node = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (!node.children.containsKey(ch)) return false;
            node = node.children.get(ch);
        }
        return true;
    }

    boolean delete(String word) {
        word = word.toLowerCase().trim();
        return deleteHelper(root, word.toCharArray(), 0);
    }

    private boolean deleteHelper(TrieNode node, char[] word, int depth) {
        if (depth == word.length) {
            if (!node.isEnd) return false;
            node.isEnd = false;
            wordCount--;
            return node.children.isEmpty();
        }
        
        char ch = word[depth];
        if (!node.children.containsKey(ch)) return false;
        
        boolean shouldDelete = deleteHelper(node.children.get(ch), word, depth + 1);
        if (shouldDelete) {
            node.children.remove(ch);
        }
        return shouldDelete && !node.isEnd && node.children.isEmpty();
    }
}
// 1012411204:Ikara
class AutoComplete {
    Trie trie;

    AutoComplete(Trie trie) { 
        this.trie = trie; 
    }

    private void dfs(TrieNode node, char[] prefix, StringBuilder currentPrefix, String[][] results, int[] resultCount, int maxResults) {
        if (node.isEnd && maxResults > 0) {
            results[resultCount[0]] = new String[]{currentPrefix.toString(), String.valueOf(node.frequency)};
            resultCount[0]++;
        }
        
        // Converting HashMap keys to char array
        Character[] keyArray = new Character[node.children.size()];
        node.children.keySet().toArray(keyArray);
        
        // bubble sort for alphabetical order
        for (int i = 0; i < keyArray.length - 1; i++) {
            for (int j = 0; j < keyArray.length - i - 1; j++) {
                if (keyArray[j] > keyArray[j + 1]) {
                    Character temp = keyArray[j];
                    keyArray[j] = keyArray[j + 1];
                    keyArray[j + 1] = temp;
                }
            }
        }
        
        for (int i = 0; i < keyArray.length; i++) {
            char ch = keyArray[i];
            if (resultCount[0] < maxResults) {
                TrieNode child = node.children.get(ch);
                currentPrefix.append(ch);
                dfs(child, prefix, currentPrefix, results, resultCount, maxResults);
                currentPrefix.deleteCharAt(currentPrefix.length() - 1);
            }
        }
    }

    String[] getSuggestions(String prefix, int maxResults, String sortBy) {
        prefix = prefix.toLowerCase().trim();
        TrieNode node = trie.root;
        
        // Finding prefix node 
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (!node.children.containsKey(ch)) {
                return new String[0]; // It is Empty array
            }
            node = node.children.get(ch);
        }
        
        //array for results
        String[][] results = new String[maxResults][2];
        int[] resultCount = {0};
        StringBuilder currentPrefix = new StringBuilder(prefix);
        
        dfs(node, prefix.toCharArray(), currentPrefix, results, resultCount, maxResults);
        
        // Converting to output array
        String[] output = new String[resultCount[0]];
        for (int i = 0; i < resultCount[0]; i++) {
            output[i] = results[i][0];
        }
        
        // sorting if frequency requested
        if (sortBy.equals("frequency")) {
            // Bubble sort by frequency (results[i][1])
            for (int i = 0; i < output.length - 1; i++) {
                for (int j = 0; j < output.length - i - 1; j++) {
                    int freq1 = Integer.parseInt(results[j][1]);
                    int freq2 = Integer.parseInt(results[j + 1][1]);
                    if (freq1 < freq2) {
                        String temp = output[j];
                        output[j] = output[j + 1];
                        output[j + 1] = temp;
                    }
                }
            }
        }
        
        return output;
    }

    void recordSearch(String word) {
        word = word.toLowerCase().trim();
        TrieNode node = trie.root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!node.children.containsKey(ch)) return;
            node = node.children.get(ch);
        }
        if (node.isEnd) node.frequency++;
    }

    String[] getAllWords() {
        String[][] results = new String[1000][2]; 
        int[] resultCount = {0};
        StringBuilder sb = new StringBuilder();
        dfsAll(trie.root, sb, results, resultCount);
        
        String[] words = new String[resultCount[0]];
        for (int i = 0; i < resultCount[0]; i++) {
            words[i] = results[i][0];
        }
        return words;
    }
    
    private void dfsAll(TrieNode node, StringBuilder prefix, 
                       String[][] results, int[] resultCount) {
        if (node.isEnd) {
            results[resultCount[0]] = new String[]{prefix.toString(), String.valueOf(node.frequency)};
            resultCount[0]++;
        }
        
        Character[] keyArray = new Character[node.children.size()];
        node.children.keySet().toArray(keyArray);
        
        // Bubble sort
        for (int i = 0; i < keyArray.length - 1; i++) {
            for (int j = 0; j < keyArray.length - i - 1; j++) {
                if (keyArray[j] > keyArray[j + 1]) {
                    Character temp = keyArray[j];
                    keyArray[j] = keyArray[j + 1];
                    keyArray[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < keyArray.length; i++) {
            char ch = keyArray[i];
            prefix.append(ch);
            dfsAll(node.children.get(ch), prefix, results, resultCount);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
//1012411220: Jagruti
class DatasetManager {
    static final String[] DEFAULT_WORDS = {
        "apple","application","apply","apt","approve","april",
        "ball","bat","batman","bath","battle","balance",
        "cat","car","card","care","carry","call","camera",
        "dog","door","dome","data","dark","dance",
        "elephant","enter","email","engine","error",
        "flower","fly","flag","format","frame",
        "google","graph","green","great","grow",
        "happy","hard","hash","head","help","hero",
        "internet","input","install","image","index",
        "java","join","judge","jump","just",
        "key","king","kitchen","knowledge",
        "language","laptop","large","learn","level",
        "module","map","matrix","menu","method",
        "node","network","name","null","number",
        "object","open","output","operator",
        "python","prefix","print","program","process",
        "queue","query","quick","quit",
        "root","run","return","real","read",
        "stack","string","search","system","sort","speed",
        "tree","trie","test","time","text","table",
        "user","update","upload","use","unit",
        "value","variable","vector","valid",
        "word","write","window","work","web"
    };

    static int loadDefaults(Trie trie) {
        int count = 0;
        for (int i = 0; i < DEFAULT_WORDS.length; i++) {
            if (trie.insert(DEFAULT_WORDS[i])) count++;
        }
        return count;
    }

public class AutoCompleteSystem{
    static java.util.Scanner sc = new java.util.Scanner(System.in);
    static String sortMode = "alpha";

    public static void main(String[] args) {
        Trie trie = new Trie();
        AutoComplete ac = new AutoComplete(trie);

        int loaded = DatasetManager.loadDefaults(trie);
        System.out.println(" Successfully! Loaded " + loaded + " words into Trie.");

        while (true) {
            printMenu(trie);
            System.out.print(" Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": 
                    menuAutocomplete(ac); 
                    break;
                case "2": 
                    menuSearch(trie); 
                    break;
                case "3": 
                    menuInsert(trie); 
                    break;
                case "4": 
                    menuDelete(trie); 
                    break;
                case "5": 
                    menuDisplay(ac); 
                    break;
                case "6": 
                    menuStats(trie, ac); 
                    break;
                case "7":
                    sortMode = sortMode.equals("alpha") ? "frequency" : "alpha";
                    System.out.println(" Sort mode: " + sortMode.toUpperCase());
                    break;
                case "8": 
                    menuHelp(); 
                    break;
                case "9":
                    System.out.println("\n Have a nice day ^_^ !\nGreat time working with you.\n");
                    return;
                default:
                    System.out.println(" Invalid choice.!!! Enter 0-9.");
            }
        }
    }
    
    static void printMenu(Trie trie) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println(" AUTO COMPLETE SYSTEM — Trie Data Structure");
        System.out.println("=".repeat(55));
        System.out.println(" Words: " + trie.wordCount + " | Sort: " + sortMode.toUpperCase());
        System.out.println("\n MAIN MENU");
        System.out.println(" " + "-".repeat(40));
        System.out.println(" 1. Get Autocomplete Suggestions");
        System.out.println(" 2. Search Exact Word");
        System.out.println(" 3. Insert New Word");
        System.out.println(" 4. Delete a Word");
        System.out.println(" 5. Display All Words");
        System.out.println(" 6. Trie Statistics");
        System.out.println(" 7. Toggle Sort Mode [current: " + sortMode + "]");
        System.out.println(" 8. Help / Instructions");
        System.out.println(" 9. Exit");
        System.out.println(" " + "_".repeat(40));
    }

    static void menuAutocomplete(AutoComplete ac) {
        System.out.println("\n... Autocomplete Search ...");
        System.out.print(" Enter prefix: ");
        String prefix = sc.nextLine().trim();
        if (prefix.isEmpty()) { 
            System.out.println(" Prefix cannot be empty.!!!"); 
            return; 
        }

        System.out.print(" Max suggestions (default 10): ");
        String maxIn = sc.nextLine().trim();
        int max = 10;
        try { 
            if (!maxIn.isEmpty()) max = Integer.parseInt(maxIn); 
        } catch (Exception e) { 
            max = 10; 
        }

        String[] suggestions = ac.getSuggestions(prefix, max, sortMode);
        System.out.println();
        if (suggestions.length == 0) {
            System.out.println(" [!] No suggestions found for '" + prefix + "'.");
        } else {
            System.out.println(" Suggestions for '" + prefix + "' (" + suggestions.length + " found):");
            System.out.println(" " + "-".repeat(35));
            for (int i = 0; i < suggestions.length; i++) {
                System.out.printf(" %3d. %s%n", i + 1, suggestions[i]);
            }

            System.out.print("\n Select word to add in suggestion or Enter to skip: ");
            String pick = sc.nextLine().trim();
            if (pick.matches("\\d+")) {
                int idx = Integer.parseInt(pick) - 1;
                if (idx >= 0 && idx < suggestions.length) {
                    ac.recordSearch(suggestions[idx]);
                    System.out.println(" Great! Suggestion Recorded  Successfully'" + suggestions[idx] + "'.");
                }
            }
        }
    }

    static void menuSearch(Trie trie) {
        System.out.println("\n... Exact Word Search ...");
        System.out.print(" Enter word: ");
        String word = sc.nextLine().trim().toLowerCase();
        if (word.isEmpty()) { 
            System.out.println(" Input cannot be empty.!!!"); 
            return; 
        }
        if (trie.search(word)) {
            System.out.println(" Great! '" + word + "' EXISTS in the Trie.");
        } else {
            System.out.println(" Sorry! '" + word + "' NOT found.");
            if (trie.startsWith(word)) {
                System.out.println(" Sorry! But words starting with '" + word + "' exist.");
            }
        }
    }
    //1012411214: Nirmayee
 static void menuInsert(Trie trie) {
        System.out.println("\n... Insert Word ...");
        System.out.print(" Enter word: ");
        String word = sc.nextLine().trim().toLowerCase();
        if (word.isEmpty()) { 
            System.out.println(" Input cannot be empty.!!!"); 
            return; 
        }
        if (!word.matches("[a-z]+")) { 
            System.out.println(" Only letters allowed!!!."); 
            return; 
        }
        if (trie.search(word)) {
            System.out.println(" Sorry! '" + word + "' already exists.");
        } else {
            trie.insert(word);
            System.out.println(" Great! '" + word + "' inserted. Total: " + trie.wordCount);
        }
    }

    static void menuDelete(Trie trie) {
        System.out.println("\n... Delete Word ...");
        System.out.print(" Enter word: ");
        String word = sc.nextLine().trim().toLowerCase();
        if (word.isEmpty()) { 
            System.out.println(" Input cannot be empty.!!!"); 
            return; 
        }
        if (trie.delete(word)) {
            System.out.println(" Great!  '" + word + "' deleted. Total: " + trie.wordCount);
        } else {
            System.out.println(" Sorry! '" + word + "' not found.");
        }
    }

    static void menuDisplay(AutoComplete ac) {
        System.out.println("\n... All Words in Trie...");
        String[] words = ac.getAllWords();
        if (words.length == 0) { 
            System.out.println(" Trie is empty!!!."); 
            return; 
        }
        System.out.println(" Total: " + words.length + "\n");
        for (int i = 0; i < words.length; i++) {
            System.out.printf(" %-18s", words[i]);
            if ((i + 1) % 4 == 0) System.out.println();
        }
        System.out.println();
    }
    //1012411215: Chinmay
       static void menuDataset(Trie trie, AutoComplete ac) {
        System.out.println("\n___ Dataset Management___");
        System.out.println(" 1. Load default word list");
        System.out.println(" 2. Load from file");
        System.out.println(" 3. Save to file");
        System.out.println(" 4. Reset Trie");
        System.out.println(" 0. Back");
        System.out.print(" Choice: ");
        String ch = sc.nextLine().trim();
        switch (ch) {
            case "1":
                System.out.println(" Loaded " + DatasetManager.loadDefaults(trie) + " new words.");
                break;
            case "2":
                System.out.print(" Filename: ");
                int cnt = DatasetManager.loadFromFile(trie, sc.nextLine().trim());
                if (cnt >= 0) System.out.println(" Loaded " + cnt + " words.");
                break;
            case "3":
                System.out.print(" Save filename: ");
                DatasetManager.saveToFile(ac, sc.nextLine().trim());
                               break;
            case "4":
                System.out.print(" Confirm reset? (yes/no): ");
                if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                    trie.root = new TrieNode();
                    trie.wordCount = 0;
                    System.out.println(" Trie reset.");
                } else {
                    System.out.println(" Cancelled.");
                }
                break;
            case "10":
                break;
            default:
                System.out.println("Invalid option!!!. Choose a valid Option");
        }
    } 

    //1012411214: Nirmayee
    static void menuStats(Trie trie, AutoComplete ac) {
        System.out.println("\n___Trie Statistics___");
        String[] words = ac.getAllWords();
        if (words.length == 0) {
            System.out.println(" Trie is empty!!!.");
            return;
        }
        
        // for finding longest/shortest word in whole array
        String longest = words[0];
        String shortest = words[0];
        int totalLength = 0;
        
        for (int i = 0; i < words.length; i++) {
            totalLength += words[i].length();
            if (words[i].length() > longest.length()) {
                longest = words[i];
            }
            if (words[i].length() < shortest.length()) {
                shortest = words[i];
            }
        }
        
        double avg = (double) totalLength / words.length;
        
        System.out.println(" Total words : " + trie.wordCount);
        System.out.println(" Longest word : " + longest + " (" + longest.length() + " chars)");
        System.out.println(" Shortest word : " + shortest + " (" + shortest.length() + " chars)");
        System.out.printf(" Average word length : %.2f chars%n", avg);
    }

    static void menuHelp() {
        System.out.println("\n___ Help___");
        System.out.println(" 1. Autocomplete suggestions for a prefix");
        System.out.println(" 2. Check if exact word exists");
        System.out.println(" 3. Add new word to Trie");
        System.out.println(" 4. Remove word from Trie");
        System.out.println(" 5. List all stored words");        
        System.out.println(" 6. View Trie statistics");
        System.out.println(" 7. Toggle sort: alpha or frequency");
        System.out.println(" 8. Show this help");
        System.out.println(" 9. Exit");
        System.out.println(" 6. Load / Save / Reset dataset");
        System.out.println("\n File format (option 6): one word per line (.txt)");
        System.out.println(" Sort frequency: select a word after searching");
        System.out.println(" to increase its frequency rank.");
    }
}
