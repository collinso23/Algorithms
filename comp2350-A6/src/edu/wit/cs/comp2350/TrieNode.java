package edu.wit.cs.comp2350;

public class TrieNode {
    boolean isWord;
    TrieNode[] children;

    TrieNode() {
        this.isWord = false;
        this.children = new TrieNode[26];
    }
}