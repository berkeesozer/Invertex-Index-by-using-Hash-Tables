import java.util.Iterator;

public class HashedDictionary<K,V> implements DictionaryI<K,V> {

	private int locationsUsed;
	private int collisionCount = 0;
	private TableEntry<K, V>[] hashTable;
	private static final double load_factor = 0.5;


	public HashedDictionary() {
		this(2);
	}

	public HashedDictionary(int initialCapacity) {
		// checking if the table size is prime or not
		int tableSize = isPrime(initialCapacity) ? initialCapacity : getNextPrime(initialCapacity);

		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[tableSize];
		hashTable = temp;
	}

	// methods
	public V remove(K key,String hashFunction)
	{
		V removedValue = null;
		int index;
		if (hashFunction.equals("SSF")) {
			index = getHashIndexSSF(key);
		} else {
			index = getHashIndexPAF(key);
		}
		index = locate(index, key);
		if (index != -1)
		{ // Key found; flag entry as removed and return its value
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
		} // end if
		// Else key not found; return null
		return removedValue;
	} // end remove

	public V put(K key, V value, String hashFunction, String collisionType) 
	{
		if ((key == null) || (value == null)) throw new IllegalArgumentException("Key or value mustn't be null.");

		V oldValue; // Value to return
		int index = hashFunction.equals("SSF") ? getHashIndexSSF(key) :
				hashFunction.equals("PAF") ? getHashIndexPAF(key) : -1;

		if (index == -1) throw new IllegalArgumentException("HashFunction must be SSF or PAF.");

		assert (index >= 0) && (index < hashTable.length);

		// firstly check size
		if (isHashTableTooFull()) {
			enlargeHashTable();
		}

		// IF WE NEED COLLISION HANDLING (IF THAT INDEX IS NOT EMPTY)
		
		// linear probing
		if (collisionType.equals("LP"))
		{
			do {
				if (hashTable[index] == null || hashTable[index].getKey() == key ) { break;}
				index = ++index % hashTable.length;
				collisionCount++;

			} while (hashTable[index] != null);
		}
		
		// double hashing
		if (collisionType.equals("DH")) {
			int prime = 7;
			int j = 1	;
			int secondhash = 0;
			if (hashFunction.equals("SSF")) 
			{
				secondhash = prime - (getHashCodeSSF(key)%prime); // getting the hashcode (not modded version)
			} 
			else if(hashFunction.equals("PAF"))
			{
				secondhash = prime - (getHashCodePAF(key)%prime); // getting the hashcode (not modded version)
			}
			else
				System.out.println("This hash function is not defined.");
			int firstindex = index;
			while (hashTable[index] !=null)
			{
				if (hashTable[index] == null || hashTable[index].getKey() == key ) { break;}
				index = (firstindex + j * secondhash) % hashTable.length;
				j++;
				collisionCount++;
			}
		
		}
		
		

		if ((hashTable[index] == null) || (hashTable[index].state == TableEntry.States.REMOVED))
		{ // Key not found, so insert new entry
			locationsUsed++;
			oldValue = null;
		} else
		{ // Key found; get old value for return and then replace it
			oldValue = hashTable[index].getValue();
		}

		hashTable[index] = new TableEntry<>(key, value);

		

		return oldValue;

	}


	public V get(K key, String hashFunction)
	{
		V result = null;
		int index;
		if (hashFunction.equals("SSF")) {
			index = getHashIndexSSF(key);
		} else {
			index = getHashIndexPAF(key);
		}
		index = locate(index, key);
		if (index != -1)
			result = hashTable[index].getValue(); // Key found; get value
		// Else key not found; return null
		return result;
	} // end get

	public int locate(int index, K key)
	{
		boolean found = false;
		while ( !found && (hashTable[index] != null) )
		{
			if ((hashTable[index].getState().toString()).equals("CURRENT") &&
					key.equals(hashTable[index].getKey()) )
				found = true;
			else
				index = (index + 1) % hashTable.length; // Linear probing
		}
		// Assertion: Either key or null is found at hashTable[index]
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	public void enlargeHashTable()
	{
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize + oldSize);
		// The cast is safe because the new array contains null entries
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] newTable = (TableEntry<K, V>[])new TableEntry[newSize];
		hashTable = newTable;

		System.arraycopy(oldTable, 0, newTable, 0, oldSize);
	} //


	public int getHashCodeSSF(K key)
	{
		String stringkey = (String) key;
		int hashCodeSSF = 0;
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for(int i=0; i < stringkey.length(); i++)
		{
			hashCodeSSF += (alphabet.indexOf(stringkey.charAt(i))+1);
		}

		return hashCodeSSF;
	}

	public int getHashIndexSSF(K key)
	{
		int hashIndex = getHashCodeSSF(key) % hashTable.length;
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	private int getHashIndexPAF(K key)
	{
		int hashIndex = getHashCodePAF(key) % hashTable.length;
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	private int getHashCodePAF(K key)
	{
		int hashCodePAF = 0;
		String stringkey = (String) key;
		char[] ch = stringkey.toCharArray();
		int prime = 31;
		for (int i = 0; i < ch.length; i++)
		{
			int temp = 0;
			temp = ch[i]-96;
			temp = (int) (temp * Math.pow(prime, ch.length-(i+1)));
			hashCodePAF+=temp;
		}

		return hashCodePAF;
	}

	public boolean isHashTableTooFull() {
		return (double) locationsUsed / hashTable.length >= load_factor;
	}

	public int getNextPrime(int num)
	{
		num++;
		for (int i = 2; i < num; i++) {
			if(num%i == 0) {
				num++;
				i=2;
			} else {
				continue;
			}
		}
		return num;
	}

	public boolean isPrime(int n)
	{
		// Corner case
		if (n <= 1)
			return false;

		// Check from 2 to n-1
		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;

		return true;
	}
	public void displayHashTable() {
		for (int index = 0; index < hashTable.length; index++) {
			if (hashTable[index] == null) {
				System.out.println("null ");
			} else if ((hashTable[index].getState().toString()).equals("REMOVED")) {
				System.out.println("Removed Entry");
			} else {
				System.out.println(index +" "+ hashTable[index].getKey() + " " + hashTable[index].getValue());
			}

		}
		System.out.println();
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		// private TableEntry<K, V>[] hashTable;
		final int lastIndex = hashTable.length-1;
		return new Iterator<>() {
			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return hashTable != null && currentIndex != lastIndex && checkIt();
			}

			private boolean checkIt() {
				if (hashTable[currentIndex] == null || hashTable[currentIndex].state == TableEntry.States.REMOVED) {
					currentIndex++;
					TableEntry<K, V> index;
					while ((index = hashTable[currentIndex]) == null || index.state == TableEntry.States.REMOVED) {
						if (currentIndex < lastIndex) currentIndex++;
						else return false;
					}
				}
				return true;
			}

			@Override
			public TableEntry<K, V> next() {
				return hashTable[currentIndex++];
			}
		};
	}

	public int getCollisionCount()
	{
		return collisionCount;
	}

	public static class TableEntry<K, V>
	{
		private K key;
		private V value;
		private States state; // Flags whether this entry is in the hash table
		private enum States {CURRENT, REMOVED} // Possible values of state

		private TableEntry(K searchKey, V dataValue)
		{
			key = searchKey;
			value = dataValue;
			state = States.CURRENT;
		}



		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public States getState() {
			return state;
		}

		public void setState(States state) {
			this.state = state;
		}

		public void setToRemoved() {
			state = States.REMOVED;
		}
	}
}