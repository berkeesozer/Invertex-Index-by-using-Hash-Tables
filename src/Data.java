
public class Data {
	
	int txtnumber;
	String genre;
	int count;
	
	public Data(int txtnumber, String genre, int count) {
		super();
		this.txtnumber = txtnumber;
		this.genre = genre;
		this.count = count;
	}
	public Data()
	{
		
	}

	public int getTxtNumber() {
		return txtnumber;
	}

	public void setTxtNumber(int txtnumber) {
		this.txtnumber = txtnumber;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	
}
