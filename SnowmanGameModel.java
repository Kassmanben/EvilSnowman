import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnowmanGameModel {
	private int length;
	private int guesses;
	private String guessedWord = "";
	private String prevGuessedWord = "";
	private int noDuplicateLetters = 0;
	ArrayList<String> dictionary = new ArrayList<String>();
	private ArrayList<Character> guessedLetters = new ArrayList<Character>();

	/**
	 * Initializes the game model. Sets the length of the word being guessed,
	 * and the number of guesses allowed Creates the game dictionary
	 */
	public SnowmanGameModel() {
		boolean remain = false;

		// Continues looping through until it gets a satisfactory value for
		// length
		while (!remain) {
			StdOut.println("Please enter the length of the word you are guessing");
			length = StdIn.readInt();

			// Max length of a word in the dictionary is 29,
			// but there are no 26/27 letter long words
			if (length > 0 && length < 29 && length != 26 && length != 27) {
				remain = true;
				for (int i = 0; i < length; i++) {
					guessedWord += "_";
					prevGuessedWord += "_";
				}
			}
		}
		// Continues looping through until it gets a satisfactory value for
		// guesses
		// Can't be more than 26, since there are only 26 letters
		while (remain) {
			StdOut.println("Please enter the number of guesses you want");
			guesses = StdIn.readInt();
			if (guesses > 0 && length < 27) {
				remain = false;
			}
		}
		initializeGameDictionary();
		run();
	}

	/** The actual game, contains the turn() and updateGameDictionary() methods */
	public void run() {
		// Only runs for the number of guesses allotted
		while (guesses > 0) {

			// Player only wins when they have narrowed the dictionary down to
			// one value
			if (guessedWord.equals(dictionary.get(0))) {
				StdOut.println(guessedWord);
				StdOut.println("YOU WIN!");
				break;
			}

			StdOut.println("\n" + "Guessed Letters: "
					+ guessedLetters.toString());
			StdOut.println("You have " + guesses + " guesses left.");
			StdOut.println("Guessed Word: " + guessedWord);
			StdOut.println("There are " + dictionary.size()
					+ " words consistant with that");
			StdOut.println("Guess a letter: ");

			turn();

			// noDuplicateLetters takes the previous size of the guessedLetters
			// list
			// This keeps the player from guessing duplicate letters
			if (guessedLetters.size() > noDuplicateLetters) {
				updateGameDict();

				// Only decrements the number of guesses if the guess was
				// "wrong"
				if (guessedWord.equals(prevGuessedWord)) {
					guesses--;
				}

				// Increments noDuplicateLetters here, since the fact that they
				// entered the loop implies that they guessed a unique letter
				noDuplicateLetters++;
			} else {
				StdOut.println("Please enter a new letter");
			}
		}

		// If they don't get the right word by the end of their guesses,
		// pick a random word from the current dictionary and claim that was the
		// original
		if (guesses == 0) {
			int k = (int) (Math.random() * dictionary.size());
			StdOut.println("YOU LOSE! The word was: " + dictionary.get(k));
		}
	}

	/**
	 * Takes in a letter as a guess, doesn't allow for
	 * empty/whitespace/duplicate letters as guesses
	 */
	public void turn() {
		if (!StdIn.isEmpty() && StdIn.readChar() != '\r') {
			char letter = StdIn.readChar();
			for (char l : guessedLetters) {
				if (l == letter) {
					return;
				}
			}
			guessedLetters.add(letter);
		}
	}

	/**
	 * Finds the largest set of words that fits the set of guesses up to this
	 * point
	 */
	public void updateGameDict() {

		// Select the most recent guessed letter
		char newLetter = guessedLetters.get(guessedLetters.size() - 1);
		String pseudoHashCode = "";
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		// Creates a string of "binary" where each '1' represents the most
		// recent guessed letter
		// and '0' represents any other letter.
		for (String word : dictionary) {
			for (char letter : word.toCharArray()) {
				if (letter == newLetter) {
					pseudoHashCode += "1";
				} else {
					pseudoHashCode += "0";
				}
			}

			// Uses this "binary" string to add words to an arraylist of
			// other words in that word family
			if (map.containsKey(pseudoHashCode)) {
				ArrayList<String> temp = map.get(pseudoHashCode);
				temp.add(word);
				map.put(pseudoHashCode, temp);
			} else {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(word);
				map.put(pseudoHashCode, temp);
			}
			pseudoHashCode = "";
		}

		int longestEntry = 0;
		String longestEntryKey = "";

		// Finds the key corresponding to the largest word family
		for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
			if (m.getValue().size() > longestEntry) {
				longestEntry = m.getValue().size();
				longestEntryKey = m.getKey();
			}
		}

		// Makes the largest word family the new dictionary
		dictionary.clear();
		for (String word : map.get(longestEntryKey)) {
			dictionary.add(word);
			// StdOut.println(word);
		}
		prevGuessedWord = guessedWord;

		// If the new word family contains the most recent guessed letter,
		// show that in the player's guesses
		for (int i = 0; i < longestEntryKey.toCharArray().length; i++) {
			char binaryNum = longestEntryKey.toCharArray()[i];
			if (binaryNum == '1') {
				guessedWord = guessedWord.substring(0, i) + newLetter
						+ guessedWord.substring(i + 1);
			}

		}
	}

	/**
	 * Reads in dictionary file, adds words of the correct length to the
	 * starting dictionary
	 */
	public void initializeGameDictionary() {
		try (BufferedReader br = new BufferedReader(new FileReader(
				"enable1.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() == length) {
					dictionary.add(line);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/** Does what it says on the tin */
	public int getLength() {
		return length;
	}

	/** Does what it says on the tin */
	public int getGuesses() {
		return guesses;
	}

	public static void main(String[] args) {
		SnowmanGameModel game = new SnowmanGameModel();
	}

	public void setGuesses(int i) {
		guesses = i;
	}

	public void setLength(int i) {
		length = i;
	}
	
	public int getDictionaySize(){
		return dictionary.size();
	}

}
