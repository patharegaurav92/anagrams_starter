/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static android.media.CamcorderProfile.get;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList ;
    private HashSet<String> wordSet ;
    private HashMap<String,ArrayList<String>> lettersToWord ;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    public int wordLength;
    private boolean isTwoLetterModeOn;

    public AnagramDictionary(Reader reader,boolean b) throws IOException {
        wordList = new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String,ArrayList<String>>();
        wordLength = DEFAULT_WORD_LENGTH;
        isTwoLetterModeOn = b;
        sizeToWords = new HashMap<Integer, ArrayList<String>>();
        ArrayList<String> wLength = new ArrayList<String>();
        BufferedReader in = new BufferedReader(reader);
        String line;
        int wordLength;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordLength = word.length();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if(!lettersToWord.containsKey(sortedWord)){
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(word);
                lettersToWord.put(sortedWord,arrayList);
            }
            else{
                ArrayList<String> arrayList = lettersToWord.get(sortedWord);
                arrayList.add(word);
                lettersToWord.put(sortedWord,arrayList);
            }
            if(sizeToWords.containsKey(wordLength)){
                wLength = sizeToWords.get(wordLength);
                wLength.add(word);
                sizeToWords.put(wordLength,wLength);
            }else{
                wLength = new ArrayList<String>();
                wLength.add(word);
                sizeToWords.put(wordLength,wLength);
            }
        }


    }

    public boolean isGoodWord(String word, String base) {

        if(wordSet.contains(word)){
            System.out.println(word+" "+base);
            if(word.toLowerCase().contains(base.toLowerCase())){
                return false;
            }
        }
        if (word.contains(base)) {
            return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        String word = sortLetters(targetWord);
        if(lettersToWord.containsKey(word)){
            result= lettersToWord.get(word);
        }
        /*for(int i = 0; i< wordList.size();i++){
            String word = wordList.get(i);

            if(word.length() == targetWord.length()){
                String sortedWord = sortLetters(word);
                if(!lettersToWord.containsKey(sortedWord)){
                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.add(word);
                    lettersToWord.put(sortedWord,arrayList);
                }
                else{
                    ArrayList<String> arrayList = lettersToWord.get(sortedWord);
                    arrayList.add(word);
                    lettersToWord.put(sortedWord,arrayList);
                }
            }
        }
        result = lettersToWord.get(sortLetters());*/
        return result;
    }

    private String sortLetters(String word) {
        char[] cArray = word.toCharArray();
        Arrays.sort(cArray);
        String sortedString = new String(cArray);

        return sortedString;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word,boolean check) {
        isTwoLetterModeOn =check;
        ArrayList<String> result = new ArrayList<String>();
        String[] alphabets= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r"
                ,"s","t","u","v","w","x","y","z"};
        ArrayList<String> tempResult = new ArrayList<String>();
        if(!check) {
            for (int i = 0; i < alphabets.length; i++) {
                String w = sortLetters(word + alphabets[i]);
                if (lettersToWord.containsKey(w)) {
                    tempResult = lettersToWord.get(w);
                    for (int j = 0; j < tempResult.size(); j++) {
                        if (isGoodWord(tempResult.get(j), word))
                            result.add(tempResult.get(j));
                    }
                }
            }
        }else{
            for (int i = 0; i < alphabets.length; i++) {
                for(int k=0;k<alphabets.length;k++) {
                    String w = sortLetters(word + alphabets[i]+alphabets[k]);
                    if (lettersToWord.containsKey(w)) {
                        tempResult = lettersToWord.get(w);
                        for (int j = 0; j < tempResult.size(); j++) {
                            if (isGoodWord(tempResult.get(j), word))
                                result.add(tempResult.get(j));
                        }
                    }
                }
            }

        }
        return result;
    }

    public String pickGoodStarterWord() {
        int noOfAnagrams = 0;
        String word=null;
        ArrayList<String> anagrams;
        int max =wordList.size();
            int n = random.nextInt(max)+1;
        Log.v("pickGoodStarterWord", String.valueOf(random));
            for (int i = n; i < wordList.size(); i++) {
                word = wordList.get(i);
                if(word.length() == wordLength) {
                    anagrams = (ArrayList<String>) getAnagramsWithOneMoreLetter(wordList.get(i),isTwoLetterModeOn);
                    noOfAnagrams = anagrams.size();
                    if (noOfAnagrams > MIN_NUM_ANAGRAMS) {
                        break;
                    }
                }
            }

        if(wordLength<MAX_WORD_LENGTH){
            wordLength++;
        }
        return word;
      /*  return "cinema";*/

    }
}
