import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
public class ConsistentHash<T> {

	public final static int MAX_DUPES = 10;
	private final int numberOfReplicas;
	private final HashFunction hashFunction;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
		this(new MD5HashFunction(), numberOfReplicas, nodes);
	}

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,
			Collection<T> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			addBin(node);
		}
	}

	public void addBin(T bin) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(bin.toString() + i), bin);
		}
	}

	public void removeBin(T bin) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(bin.toString() + i));
		}
	}

	public T getBinFor(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunction.hash(key);
		T bin = circle.get(hash);

		if (bin == null) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
			bin = circle.get(hash);
		}
		return bin;
	}

	public List<T> getNBinsFor(Object key, int n) {
		if (circle.isEmpty()) {
			return Collections.<T> emptyList();
		}

		List<T> list = new ArrayList<T>(n);
		int hash = hashFunction.hash(key);
		for (int i = 0; i < n; i++) {
			if (!circle.containsKey(hash)) {
				SortedMap<Integer, T> tailMap = circle.tailMap(hash);
				hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
			}
			list.add(circle.get(hash));
			hash++;
		}
		return list;
	}

	public List<T> getNUniqueBinsFor(Object key, int n) {
		if (circle.isEmpty()) {
			return Collections.<T> emptyList();
		}

		List<T> list = new ArrayList<T>(n);
		int hash = hashFunction.hash(key);
		int duped = 0;
		for (int i = 0; i < n; i++) {
			if (!circle.containsKey(hash)) {
				SortedMap<Integer, T> tailMap = circle.tailMap(hash);
				hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
			}
			T candidate = circle.get(hash);
			if (!list.contains(candidate)) {
				duped = 0;
				list.add(candidate);
			} else {
				duped++;
				i--;
				if (duped > MAX_DUPES) {
					i++;
				}
			}
			hash++;
		}
		return list;
	}
}