// This is Auto Complete System - 'Helper' which will suggest words based on the prefix enterd by the user.
import java.util.*;

public class AutoCompleteSystem {
    
    // Trie node structure 
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord =false;
        String word =null;
    }
    public TrieNode root;

    // Constructor- used to initialize Trie and load data
    public AutoCompleteSystem() {
        root = new TrieNode();
        loadDataset();
    }

    // Method to load sample words into Trie
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
    // Method to get word suggestions based on a given prefix.
    public String[] getSuggestions(String prefix) {
        totalQueries++;

        int qidx = -1;       // checking if it was searched before to increase freq count.
        for (int i = 0; i < querySize; i++) {
            if (queries[i].equals(prefix)) {      // checking if prefix was found in previous search.
                qidx = i;
                break;
            }
        }
        if (qidx >= 0) {
            queryCount[qidx]++;       // incrementing frequency of this query
        } else if (querySize < queries.length) {        // storing new query
            queries[querySize] = prefix;
            queryCount[querySize] = 1;
            querySize++;
        }
        // temp array to collect extra words 
        WordScore[] temp = new WordScore[50];
        int count = collectWords(root, prefix.toLowerCase(), 0, temp);
        /*for (int i = 0; i < count - 1; i++) {    //sorting words by freq using bubble sort
        for (int j = i + 1; j < count; j++) {
            if (temp[i].score < temp[j].score || 
               (temp[i].score == temp[j].score && temp[i].word.compareTo(temp[j].word) > 0)) {
                // Swap if second word has higher score or same score but lexicographically smaller
                WordScore t = temp[i]; 
                temp[i] = temp[j]; 
                temp[j] = t;
            }
        }*/
        // sorting words by freq using quick sort
        Public void quickSort(WordScore[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1);  // sorting left part
                quickSort(arr, pi + 1, high); // sorting right part
            }
        }
        // Partition function
        public int partition(WordScore[] arr, int low, int high) {
            WordScore pivot = arr[high]; // it is choosing last element as pivot
            int i = low - 1;
        
            for (int j = low; j < high; j++) {
                if (arr[j].score > pivot.score || 
                   (arr[j].score == pivot.score && arr[j].word.compareTo(pivot.word) < 0)) {
                    i++;
                    WordScore temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            // Swaping pivot to correct position
            WordScore temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            return i + 1;
        }
        // Limiting suggestions to maximum 15 words
        int limit = count < 15 ? count : 15;
        String[] res = new String[limit];
        for (int i = 0; i < limit; i++) {
            res[i] = temp[i].word;   // copying sorted words to result array
        }
        return res; 
    }
    // Traverseing trie to find the node corresponding to the end of the prefix
    private int collectWords(TrieNode node, String prefix, int depth, WordScore[] arr) {
        if (node == null) 
            return 0;      // checking if prefix path does not exist
        int count = 0;
    
        TrieNode n = node;
        // Traversing the trie according to the prefix characters
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';       // map character to index
            if (n.children[index] == null) 
                return 0;     // if prefix not found
            n = n.children[index];          // move it to next node
        }
        // Collecting all words under this prefix node
        return collectFromNode(n, arr, 0);
    }
    
    // Recursively collect all words starting from a trie node
    public int collectFromNode(TrieNode node, WordScore[] arr, int index) {
        if (node.isEndOfWord && node.word != null) {
            arr[index++] = new WordScore(node.word, node.frequency); // it stores word and frequency
        }
    
        // Recursively traverse all 26 children i.e. A-Z
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                index = collectFromNode(node.children[i], arr, index); // it collects words from child
            }
        }
        return index; 
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
