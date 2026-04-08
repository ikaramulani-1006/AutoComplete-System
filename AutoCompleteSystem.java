// This is Auto Complete System - 'Helper' which will suggest words based on the prefix enterd by the user.
import java.util.*;

static class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    String word = null;
    int frequency = 0;
}

static class WordScore {
    String word;
    int score;

    WordScore(String w, int s) {
        word = w;
        score = s;
    }
}

public class AutoCompleteSystem {

    // Trie structure to store words efficiently
    private TrieNode root;
    private String[] dictionary = new String[1000];
    private int dictSize = 0;
    private int totalQueries = 0;
    private String[] queries = new String[1000];
    private int[] queryCount = new int[1000];
    private int querySize = 0;
    
    public AutoCompleteSystem() {
        root = new TrieNode();
        loadDataset(); 
    }

    // Loads sample words and inserts them into Trie
    private void loadDataset() {
        String[] words = {
            "apple","application","apps","apply","appointment",
            "computer","company","communication","community","complete",
            "data","database","development","developer","design",
            "education","email","enterprise","environment","error",
            "function","file","folder","format","framework",
            "google","github","game","graphics","group",
            "hello","help","home","html","http",
            "internet","information","interface","image","input",
            "java","javascript","json","job","join",
            "keyboard","knowledge","key","kit","known"
        };
        for (int i = 0; i < words.length; i++) {
            insert(words[i]);
        }
        System.out.println(words.length + " words loaded.\n");
    }
    
    //Method to insert a word into a Trie
    public void insert(String word) {
        for (int i = 0; i < dictSize; i++) {
            if (dictionary[i].equals(word)) return;
        }
    
        if (dictSize < dictionary.length) {
            dictionary[dictSize++] = word;
        }
    
        TrieNode node = root;
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
        node.word = word;
        node.frequency = 1;
    }
    
    // DELETE
    public void delete(String word) {
        deleteHelper(root, word.toLowerCase(), 0);
        for (int i = 0; i < dictSize; i++) {
            if (dictionary[i].equals(word)) {
                for (int j = i; j < dictSize - 1; j++) {
                    dictionary[j] = dictionary[j+1];
                }
                dictSize--;
                break;
            }
        }
    }
    
    public boolean deleteHelper(TrieNode node, String word, int depth) {
        if (node == null) return false;
    
        if (depth == word.length()) {
            if (!node.isEndOfWord) return false;
            node.isEndOfWord = false;
            return isEmpty(node);
        }
    
        int index = word.charAt(depth) - 'a';
        if (deleteHelper(node.children[index], word, depth + 1)) {
            node.children[index] = null;
            return !node.isEndOfWord && isEmpty(node);
        }
        return false;
    }
    
    public boolean isEmpty(TrieNode node) {
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) return false;
        }
        return true;
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
    
    public static void main(String[] args) {                // main starts
    AutoCompleteSystem ac = new AutoCompleteSystem();
    Scanner sc = new Scanner(System.in);
    String choice;

    do {
        System.out.println("\n1.Search  2.Add  3.Delete  4.Exit");
        System.out.print("Choice: ");
        choice = sc.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Prefix: ");
                String[] res = ac.getSuggestions(sc.nextLine());
                for (int i = 0; i < res.length; i++) {
                    System.out.println(res[i]);
                }
                break;
            case "2":
                System.out.print("Word: ");
                ac.insert(sc.nextLine());
                break;
            case "3":
                System.out.print("Word: ");
                ac.delete(sc.nextLine());
                break;
            case "4":
                System.out.println("Bye! Have a great day!");
                break;
            default:
                System.out.println("Invalid choice!");
        }

    } while (choice!=4);

    sc.close();
}
