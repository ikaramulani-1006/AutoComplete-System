// This is Auto Complete System - 'Helper' which will suggest words based on the prefix enterd by the user.
import java.util.*;

public class AutoCompleteSystem {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord =false;
        String word =null;
    }
    private TrieNode root;
    
    public AutoCompleteSystem() {
        root = new TrieNode();
        loadDataset();
    }
    
    private void loadDataset() {
        String[] words = {
            "apple", "application", "apps", "apply", "appointment",
            "computer", "company", "communication", "community", "complete",
            "data", "database", "development", "developer", "design",
            "education", "email", "enterprise", "environment", "error",
            "function", "file", "folder", "format", "framework",
            "google", "github", "game", "graphics", "group",
            "hello", "help", "home", "html", "http",
            "internet", "information", "interface", "image", "input",
            "java", "javascript", "json", "job", "join",
            "keyboard", "knowledge", "key", "kit", "known"
        };
        System.out.println(" Loading " + words.length + " words...");
        for (String word : words) {
            insert(word);
        }
        System.out.println("Ready!\n");
    }
    //Method to insert a word into a Trie
       public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.word = word;
    }
    // Method to get word suggestions based on a given prefix
    public List<String> getSuggestions(String prefix) {
        List<String> result = new ArrayList<>();
        
        // Step 1: Find prefix node
        TrieNode node = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                return result; 
            }
            node = node.children.get(c);
        }
        
        // Step 2: Collect words 
        collectWords(node, prefix, result);
        Collections.sort(result);
        return result.size() > 5 ? result.subList(0, 5) : result;
    }
    // Recursively collecting words 
    private void collectWords(TrieNode node, String current, List<String> result) {
        if (node.isEndOfWord && node.word != null) {
            result.add(node.word);
        }
        
        //  Direct iteration 
        List<Character> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);
        
        for (char c : keys) {
            if (result.size() < 5) {
                TrieNode child = node.children.get(c);
                collectWords(child, current + c, result);
        }
    }
}
public void Display() {
        System.out.println("\n" + "_".repeat(50));
        System.out.println("...AUTO COMPLETE SYSTEM...");
        System.out.println("_".repeat(50));
        System.out.println("1. Get Suggestions");
        System.out.println("2. Add Word");
        System.out.println("3. Test Speed");
        System.out.println("4. Exit");
        System.out.println("_".repeat(50));
    }
    
    public static void main(String[] args) {
        AutoCompleteSystem ac = new AutoCompleteSystem();
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            ac.Display();
            System.out.print("Enter choice (1-4): ");
            
            String choice = sc.nextLine().trim();
            
            if (choice.equals("1")) {   //gives suggestions
                System.out.print("\n Prefix: ");
                String prefix = sc.nextLine().trim();
                List<String> suggestions = ac.getSuggestions(prefix);
                
                if (suggestions.isEmpty()) {
                    System.out.println(" No matches");
                } else {
                    System.out.println("\n Top Suggestions:");
                    System.out.println("-".repeat(30));
                    for (int i = 0; i < suggestions.size(); i++) {
                        System.out.println((i+1) + ". " + suggestions.get(i));
                    }
                }
                
            } else if (choice.equals("2")) {      //add word
                System.out.print("\n New word: ");
                String word = sc.nextLine().trim();
                if (!word.isEmpty()) {
                    ac.insert(word);
                    System.out.println(" Added!");
                }
                
            } else if (choice.equals("3")) {      // test speed
                long start = System.currentTimeMillis();
                ac.getSuggestions("app");
                ac.getSuggestions("com");
                ac.getSuggestions("dat");
                long end = System.currentTimeMillis();
                System.out.printf(" Speed: %d ms%n", (end - start));
                
            } else if (choice.equals("4")) {
                System.out.println("\n Bye! Have a nice Day!");
                break;
            } else {
                System.out.println(" Invalid!");
            }
            System.out.println();
        }
       sc.close();
    }
}
