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
