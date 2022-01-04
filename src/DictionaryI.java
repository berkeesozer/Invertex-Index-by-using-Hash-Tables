import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface DictionaryI<K, V> extends Iterable<HashedDictionary.TableEntry<K, V>> {

	V put(K key, V value,String hashFunction,String collisionType);
	V remove(K key,String hashFunction);
	V get(K key,String hashFunction);
	int locate(int index, K key);
	void enlargeHashTable();
	int getHashCodeSSF(K key);
	int getHashIndexSSF(K key);
	boolean isHashTableTooFull();
	int getNextPrime(int num);
	boolean isPrime(int n);

	
}
