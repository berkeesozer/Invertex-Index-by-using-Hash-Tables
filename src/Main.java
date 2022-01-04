import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		
		HashedDictionary<String,ArrayList<Data>> hashTable = new HashedDictionary<>(1000);
		//these arraylists holds word of that index (for example: Business(1) holds business 1.txt words)
		ArrayList<String[]> business = new ArrayList<>(1000);
		ArrayList<String[]> entertainment = new ArrayList<>(1000);
		ArrayList<String[]> politics = new ArrayList<>(1000);
		ArrayList<String[]> sport = new ArrayList<>(1000);
		ArrayList<String[]> tech = new ArrayList<>(1000);
		
		
		FileRead.ReadFile("Business", business);
		FileRead.ReadFile("Entertainment", entertainment);
		FileRead.ReadFile("Politics", politics);
		FileRead.ReadFile("Sport", sport);
		FileRead.ReadFile("Tech", tech);
		
		
		// all the hash table implementations executed in this method
		// you can change the hash function and collision type from here
		// (line 30 and line 44)
		
		FileRead.searchFile("SSF","LP",business,entertainment,politics,sport,tech,hashTable);
				
		
		while (true) {
			// getting input to search that word in the hash table
			Scanner input = new Scanner(System.in);
			System.out.println("Enter a word to search: ");
			String searchword = input.nextLine();

			if (searchword.equals("exit")) break;

			// searching that key in the hash table
			// you should change the hash function type  from here as well (line 30 and line 44)
			long starttime = System.nanoTime();
			ArrayList<Data> value = hashTable.get(searchword, "SSF");			
			long endtime = System.nanoTime();
			
			if (value !=null) {
				for (Data data : value) {
					if (data.getGenre() != null) {
						System.out.println(data.getGenre() + " " + data.getTxtNumber() + ".txt " +" Count:" + data.getCount());
					}
				}
			} else {
				System.out.println("Not found!");
			}
			System.out.println(" ");
			System.out.print("Search time by nanoseconds: ");
			System.out.println(endtime-starttime);
			System.out.println("Collision Count: " + hashTable.getCollisionCount());
		}
			
	
		

	}
}
