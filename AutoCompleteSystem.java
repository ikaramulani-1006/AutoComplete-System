// 1012411204:Ikara 
//1012411214: Nirmayee
//1012411220: Jagruti
//1012411238: Tanishka
//1012411215: Chinmay

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
