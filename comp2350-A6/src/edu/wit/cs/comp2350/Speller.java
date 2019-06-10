package edu.wit.cs.comp2350;

public abstract class Speller {

	public abstract void insertWord(String s);
	public abstract boolean contains(String s);
	public abstract String[] getSuggestions(String s);

}
