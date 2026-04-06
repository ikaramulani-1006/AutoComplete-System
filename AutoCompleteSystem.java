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
       public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.word = word;
    }
    
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
