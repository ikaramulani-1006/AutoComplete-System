// 1012411204:Ikara 
//1012411214: Nirmayee
//1012411220: Jagruti
//1012411238: Tanishka
//1012411215: Chinmay

import java.util.*;
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
