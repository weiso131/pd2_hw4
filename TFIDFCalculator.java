


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TFIDFCalculator {

    public static ArrayList<Trie> docSet = new ArrayList<Trie>();
    public static long startTime = System.currentTimeMillis();
    public static void main(String[] args){
        docSet = getDocString(args[0]);
        System.out.println(String.format("Trie build done, cost: %d", System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        output(args[1]);
    }

    public static ArrayList<String> readLine(String fileName){
        ArrayList<String> lines = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) 
                lines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    } 
    public static ArrayList<Trie> getDocString(String fileName){
        ArrayList<String> lines = readLine(fileName);
        for (int i = 0; i < lines.size(); i++){
            String newLine = lines.get(i).toLowerCase().replaceAll("[^a-z]", " ");
            String[] words =  newLine.split(" ");

            if ((i / 5) + 1 > docSet.size())
                docSet.add(new Trie());

            for (String word : words){
                if (word.length() == 0)
                    continue;
                docSet.get(i / 5).insert(word);
            }
        }
        return docSet;
    }
    public static void output(String fileName){
        
        ArrayList<String> lines = readLine(fileName);
        
        String[] line1 = lines.get(0).split(" ");
        String[] line2 = lines.get(1).split(" ");
        
        String output = "";
        for (int i = 0; i < line1.length; i++){
            String word = line1[i];
            int textNum = Integer.parseInt(line2[i]);
            double TF = 0, IDF = 0, tDcount = 0;

            for (int j = 0;j < docSet.size();j++){

                double frequency = docSet.get(j).search(word);
                TF += frequency / docSet.get(textNum).totalWords * ((j == textNum) ? 1 : 0);
                tDcount += (frequency != 0) ? 1 : 0;//can re use
            }
            if (tDcount != 0)
                IDF = Math.log((double) docSet.size() / tDcount);
            output += String.format("%.5f ", TF * IDF);
            
        }
        System.out.println(String.format("search done, cost: %d", System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("write done, cost: %d", System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }
    
}


class TrieNode{
    double Frequency = 0;
    TrieNode[] children = new TrieNode[26];
}
class Trie {
    TrieNode root = new TrieNode();
    double totalWords = 0;
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.Frequency++;
        totalWords++;
    }

    public double search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return 0;
            }
        }
        return (double)node.Frequency;
    }
}
