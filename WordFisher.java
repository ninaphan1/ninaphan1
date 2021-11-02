package lab13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.nio.file.Files; 
import java.nio.file.Paths;

public class WordFisher {

	// Please note these variables. they are the state of the object.
	public HashMap<String, Integer> vocabulary;
	public List<String> stopwords; // User ArrayList for initialization
	private String inputTextFile;
	private String stopwordsFile;

	WordFisher(String inputTextFile, String stopwordsFile) {
		this.inputTextFile = inputTextFile;
		this.stopwordsFile = stopwordsFile;

		buildVocabulary();
		getStopwords();
	}

	public void buildVocabulary() {
		vocabulary = new HashMap<String, Integer>();
		try {
			String reader = new String(Files.readAllBytes(Paths.get(inputTextFile))).toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
			String[] allWords = reader.split("\\s+");
			for (int i = 0; i < allWords.length; i++ ) {
				if (vocabulary.isEmpty() || !vocabulary.containsKey(allWords[i])) {
					vocabulary.put(allWords[i], 1);
				}
				else {
					int old = vocabulary.get(allWords[i]);
					vocabulary.put(allWords[i], old+1);
				}
			}
			vocabulary.remove("");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void seeErrors() {
		for (String word : vocabulary.keySet()) {
			System.out.println(word + " ");
		}
	}

		// TODO: load in each word from inputTextFile into the vocabulary.
		// By the end of this method, vocabulary should map each word to the number of
		// times it occurs in inputTextFile.
		// Therefore, as you iterate over words, increase the value that the word maps
		// to in vocabulary by 1.
		// If it's not in the vocabulary, then add it with an occurrence of 1.
		// Use getStopwords as an example of reading from files.


	public void getStopwords() {
		stopwords = new ArrayList<String>();
		String word;

		try {
			BufferedReader input = new BufferedReader(new FileReader(stopwordsFile));
			while ((word = input.readLine()) != null) {
				stopwords.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWordCount() {
		// TODO: Return the total number of words in inputTextFile.
		// This can be calculated using vocabulary.
		int total = 0;
		for (Integer value : vocabulary.values()) {
			total += value;
		}
		return total;
	}

	public int getNumUniqueWords() {
		// TODO: Return the number of unique words.
		// This should be the same as the number of keys in vocabulary.
		return vocabulary.size();
	}

	public int getFrequency(String word) {
		if(vocabulary.containsKey(word)){
			// TODO: Return the number of times word occurs. 
			// (Should be one simple line of code.)
			// Think about what vocabulary stores.
			return vocabulary.get(word);
		}
		
		return -1;
	}

	public void pruneVocabulary() {
		// TODO: remove stopwords from the vocabulary.
		for(int i = 0; i < stopwords.size(); i++) {
			if (vocabulary.containsKey(stopwords.get(i))) {
				vocabulary.remove(stopwords.get(i));
			}
		}
	}

	public ArrayList<String> getTopWords(int n) {
		// TODO: get the top n words.
		ArrayList<String> topWords = new ArrayList<String>();
		PriorityQueue<WordNode> orderedWords = new PriorityQueue<WordNode>(vocabulary.size(), new OrderByFrequency());
		
		Iterator<Entry<String, Integer>> iterator = vocabulary.entrySet().iterator();
		while (iterator.hasNext()) {
			HashMap.Entry<String,Integer> word = (HashMap.Entry<String, Integer>)iterator.next();
			orderedWords.add(new WordNode(word.getKey(), word.getValue()));
		}
		while (topWords.size() < n) {
			topWords.add(orderedWords.poll().word);
		}
		return topWords;
	}
	
	private class WordNode {
		private String word;
		private Integer frequency;
		
		WordNode (String word, int frequency) {
			this.word = word;
			this.frequency = frequency;
		}
	}
	
	private class OrderByFrequency implements Comparator<WordNode>{
		public int compare(WordNode l, WordNode r) {
			return (r.frequency.compareTo(l.frequency));
		}
	}

	public ArrayList<String> commonPopularWords(int n, WordFisher other) {
		ArrayList<String> commonPopWords = new ArrayList<String>();
		ArrayList<String> topWordsOne = getTopWords(n);
		ArrayList<String> topWordsOther = other.getTopWords(n);
		
		for (int i = 0; i < topWordsOne.size(); i++) {
			for (int j = 0; j < topWordsOther.size(); j++) {
				if (topWordsOne.get(i).equals(topWordsOther.get(j))) {
					commonPopWords.add(topWordsOther.get(j));
				}
			}
		}
		// TODO: get the common popular words.
		return commonPopWords;
	}

}
