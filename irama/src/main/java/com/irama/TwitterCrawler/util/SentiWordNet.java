package com.irama.TwitterCrawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.data.Synset;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
/**
 * @author Simone Papandrea
 * @modifier by Asep Fajar Firmansyah
 */
public class SentiWordNet {
	private static final String WORDNET_DICTIONARY_PATH = "C:\\Users\\YOGA 520\\Documents\\GitHub\\irama\\irama\\src\\main\\resources\\prop.xml";
	private static final String SENTI_WORD_NET_PATH = "C:\\Users\\YOGA 520\\Documents\\GitHub\\irama\\irama\\src\\main\\resources\\sentiwordnet.txt";
	private Map<String, Double> dictionary;
	private Map<String, Double> mSentiDictionary;
	private Collection<String> mLemmas;
	private Dictionary mDictionary;
	
	SentiWordNet() throws IOException, JWNLException {
		loadWordNetDictionary();
		loadSentiWordnet();
	}
	private void loadWordNetDictionary() throws FileNotFoundException, JWNLException {

		InputStream inputStream = null;

		inputStream = new FileInputStream(WORDNET_DICTIONARY_PATH);

		try {

			JWNL.initialize(inputStream);
			mDictionary = Dictionary.getInstance();

		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}

	}
	private void loadSentiWordnet() throws IOException {

		BufferedReader reader = null;
		String line;
		String[] data;
		double score;

		this.mSentiDictionary = new HashMap<String, Double>();

		try {

			reader = new BufferedReader(new FileReader(SENTI_WORD_NET_PATH));

			while ((line = reader.readLine()) != null) {

				if (!line.trim().startsWith("#")) {

					data = line.split("\t");
					score = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);
					mSentiDictionary.put(data[0] + data[1], score);
				}
			}

		} finally {

			if (reader != null)
				reader.close();

		}
	}
	
	void setContext(Collection<String> lemmas) {

		this.mLemmas = lemmas;
	}
	
	Double get(String lemma, String pos) throws JWNLException {

		Double score = null;
		IndexWord indexWord;
		String key;
		POS tag;
		int max = 0, count;

		tag = getPOS(pos);

		if (tag != null) {

			indexWord = mDictionary.lookupIndexWord(tag,lemma);

			if (indexWord != null) {

				for (Synset synset : indexWord.getSenses()) {

					key = synset.getPOS().getKey() + String.format("%08d", synset.getOffset());

					if (mSentiDictionary.containsKey(key)) {

						count = 0;

						for (Word word : synset.getWords())
							if (mLemmas.contains(word.getLemma()))
								count++;

						if (count > max) {
							
							score = mSentiDictionary.get(key);
							max = count;
						}
					}
				}
			}
		}

		return score;
	}
	private POS getPOS(String pos) {

		POS ps;

		switch (pos.substring(0, 1).toLowerCase()) {

		case "v":
			ps = POS.VERB;
			break;

		case "a":
		case "j":
		case "c":
			ps = POS.ADJECTIVE;
			break;

		case "r":
			ps = POS.ADVERB;
			break;

		default:
			ps = POS.NOUN;
		}

		return ps;
	}

	 void closeDictionary(){
		
		if(mDictionary!=null)
			mDictionary.close();
	}
	
	
}