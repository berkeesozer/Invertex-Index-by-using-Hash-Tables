import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class FileRead {

	static String delimiters = "[-+=" + " " + "\r\n " + "1234567890" + "’'\"" + "(){}<>\\[\\]" + ":" + "," + "‒–—―" + "…"
			+ "!" + "." + "«»" + "-‐" + "?" + "‘’“”" + ";" + "/" + "⁄" + "␠" + "·" + "&" + "@" + "*" + "\\" + "•"
			+ "^" + "¤¢$€£¥₩₪" + "†‡" + "°" +"¡" +"¿" +"¬" +"#" +"№" +"%‰‱" + "¶" + "′" + "§" + "~" + "¨" + "_" 
			+ "|¦" + "⁂" + "☞" + "∴" + "‽" + "※" + "]";

	private static final ArrayList<String> stopWords = new ArrayList<>();

	static {
		File stopwordsfile = new File("stop_words_en.txt");
		try {
			Scanner sc = new Scanner(stopwordsfile);
			while (sc.hasNext())
			{
				String word = sc.next();
				stopWords.add(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// read method to read words from .txt's and store them in arraylists
	public static void ReadFile(String foldername,ArrayList<String[]> arr)
	{
		try
		{
			File folder = new File("bbc/" + foldername);
			for (File sourceFile : folder.listFiles())
			{			
				Scanner txtfile = new Scanner(sourceFile); // txt files 
				StringBuilder text = new StringBuilder();
				int index = arr.size();
				while (txtfile.hasNext()) 
				{
					String word = txtfile.next();
					if (stopWords.contains(word)) 
					{
						continue;
					}
					text.append(word.toLowerCase(Locale.ENGLISH)).append(" ");
			    }
				 String[] splitted = text.toString().split(delimiters);
			     arr.add(index,splitted);
			}
		}			
		catch(Exception e)
		{
			e.printStackTrace();
		}
				
	}
	
	// search method
	public static void searchFile(String hashFunction, String collisionType,ArrayList<String[]> arr1,ArrayList<String[]> arr2,ArrayList<String[]> arr3,ArrayList<String[]> arr4,ArrayList<String[]> arr5,HashedDictionary<String,ArrayList<Data>> hashTable) throws IOException

{
		File searchtxt = new File("search.txt");	
		Scanner searchsc = new Scanner(searchtxt);
		
		while (searchsc.hasNextLine()) {
			
			String word = searchsc.nextLine(); // key
			ArrayList<Data>word_datas = new ArrayList<>(1000); // data list that stores txtnumber,genre and word count
			
			//BUSINESS
			for (int i = 0; i < arr1.size(); i++) 
			{
				Data word_data = new Data();
				word_data.setCount(0);
				for (String textword : arr1.get(i)) // i'th .txt file
				{
					if (textword.equals(word)) 
					{
						word_data.setGenre("Business");
						word_data.setTxtNumber(i+1);
						word_data.setCount(word_data.getCount()+1);
					}				
				}
				System.out.println("Business " + (i+1) + ".txt search 🗸 ");
				word_datas.add(word_data);
			}
			//ENTERTAINMENT
			for (int i = 0; i < arr2.size(); i++) 
			{
				Data word_data = new Data();
				word_data.setCount(0);
				for (String textword : arr2.get(i)) // i'th .txt file
				{
					if (textword.equals(word)) 
					{
						word_data.setGenre("Entertainment");
						word_data.setTxtNumber(i+1);
						word_data.setCount(word_data.getCount()+1);
					}				
				}
				System.out.println("Entertainment " + (i+1) + ".txt search 🗸 ");
				word_datas.add(word_data);
			}
			//POLITICS
			for (int i = 0; i < arr3.size(); i++) 
			{
				Data word_data = new Data();
				word_data.setCount(0);
				for (String textword : arr3.get(i)) // i'th .txt file
				{
					if (textword.equals(word)) 
					{
						word_data.setGenre("Politics");
						word_data.setTxtNumber(i+1);
						word_data.setCount(word_data.getCount()+1);
					}				
				}
				System.out.println("Politics " + (i+1) + ".txt search 🗸 ");
				word_datas.add(word_data);
			}
			//SPORT
			for (int i = 0; i < arr4.size(); i++) 
			{
				Data word_data = new Data();
				word_data.setCount(0);
				for (String textword : arr4.get(i)) // i'th .txt file
				{
					if (textword.equals(word)) 
					{
						word_data.setGenre("Sport");
						word_data.setTxtNumber(i+1);
						word_data.setCount(word_data.getCount()+1);
					}				
				}
				word_datas.add(word_data);
			}
			//TECH
			for (int i = 0; i < arr5.size(); i++) 
			{
				Data word_data = new Data();
				word_data.setCount(0);
				for (String textword : arr5.get(i)) // i'th .txt file
				{
					if (textword.equals(word)) 
					{
						word_data.setGenre("Tech");
						word_data.setTxtNumber(i+1);
						word_data.setCount(word_data.getCount()+1);
					}				
				}
				System.out.println("Tech " + (i+1) + ".txt search 🗸 ");
				word_datas.add(word_data);
			}
			//probabilities for hashfunction and collision handling type

			if (hashFunction.equals("SSF") || hashFunction.equals("PAF") &&
					collisionType.equals("LP") || collisionType.equals("DH")) {
				hashTable.put(word, word_datas, hashFunction, collisionType);
			}
		}

	}
	
}
