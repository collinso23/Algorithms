package edu.wit.cs.comp2350;
import java.util.ArrayList;


/* Implements a trie data structure 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 6
 * 
 */

//TODO: document class methods
public class Trie extends Speller {
	private TrieNode root;
	
	//construct root node
	public Trie() {
		root = new TrieNode();
	}

	//Insert word into Trie
	@Override
	public void insertWord(String s) {
		TrieNode trav = root;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int index = c - 'a';

			if (trav.children[index] == null) {
				TrieNode temp = new TrieNode();
				trav.children[index] = temp;
				trav = temp;
			}

			else {trav = trav.children[index];}
		}

		trav.isWord= true;
	}

	//Calls find on particular string
	@Override
	public boolean contains(String s) {
		TrieNode trav = _find(s);

		if (trav == null) {return false;} 

		else {
			if (trav.isWord) {return true;}
			
			return false;
		}
	}

	//Get suggestions
	@Override
	public String[] getSuggestions(String s) {
		ArrayList<String> suggestions = new ArrayList<String>();
		
		int numEdits = 2;
		
		findSugg(s, suggestions, 0, numEdits);
		
		return suggestions.toArray(new String[0]);
	}
	
	//_______________________________Private Helper Methods _________________________________//

	//private help method to find
	private TrieNode _find(String s) {
		TrieNode trav = root;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			int ind = ch - 'a';

			if (trav.children[ind] != null) {trav = trav.children[ind];}

			else {return null;}
		}

		if (trav == root) {return null;}

		return trav;
	}
	
	//private help method to find possible suggestions
	private void findSugg(String s, ArrayList<String> suggestions, int pos, int numEdits) {
		if (pos == s.length()) {
			if (this.contains(s))
				if (!suggestions.contains(s))
					suggestions.add(s);
			return;
		}
		
		for (int i = 0; i < 26; i++) {
			if (s.charAt(pos) == 'a' + i)
				findSugg(s, suggestions, pos + 1, numEdits);
	
			else if (numEdits > 0) {
				findSugg(s.substring(0, pos) + (char) ('a' + i) + s.substring(pos + 1, s.length()),
						suggestions, pos + 1, numEdits - 1);
			}
		}
	}   


}