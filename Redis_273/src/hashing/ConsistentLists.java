import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsistentLists<T, V> {

	final ConsistentHash<T> binHash;
	final Map<T, List<V>> valueLists;
	final List<MoveHandler<T, V>> listeners = new ArrayList<MoveHandler<T, V>>();

	public ConsistentLists(int replicationFactor) {
		binHash = new ConsistentHash<T>(replicationFactor, new ArrayList<T>());
		valueLists = new HashMap<T, List<V>>();
	}

	public Map<T, List<V>> getValueLists() {
		return Collections.unmodifiableMap(valueLists);
	}

	public void addMoveListener(MoveHandler<T, V> l) {
		listeners.add(l);
	}

	public void removeMoveListener(MoveHandler<T, V> l) {
		listeners.remove(l);
	}

	private void fireMoved(T fromBin, T toBin, List<V> vals) {
		if (vals == null || vals.isEmpty())
			return;
		for (MoveHandler<T, V> l : listeners) {
			l.moved(fromBin, toBin, vals);
		}
	}

	public void rebuild() {
		for (Map.Entry<T, List<V>> ent : valueLists.entrySet()) {
			List<V> vals = ent.getValue();
			T fromBin = ent.getKey();
			fireRebuild(fromBin, vals);
		}
	}

	private void fireRebuild(T fromBin, List<V> allVals) {
		for (MoveHandler<T, V> l : listeners) {
			l.rebuild(fromBin, allVals);
		}
	}

	public void addValue(V exp) {
		T w = binHash.getBinFor(exp);
		List<V> valList = valueLists.get(w);
		if (valList == null) {
			valList = new ArrayList<V>();
			valueLists.put(w, valList);
		}
		valList.add(exp);
		List<V> vals = new ArrayList<V>();
		vals.add(exp);
		fireMoved(null, w, vals);
	}

	public void removeValue(V exp) {
		T w = binHash.getBinFor(exp);
		List<V> valList = valueLists.get(w);
		if (valList == null) {
			throw new IllegalStateException(
					"Weird! cannot remove item that doesn't exist: " + exp);
		}
		valList.remove(exp);
		List<V> vals = new ArrayList<V>();
		vals.add(exp);
		fireMoved(w, null, vals);
	}
	public void addBin(T newBin) {
		binHash.addBin(newBin);
		List<V> newBinValues = new ArrayList<V>();
		for (Map.Entry<T, List<V>> ent : valueLists.entrySet()) {
			T bin = ent.getKey();
			List<V> valList = ent.getValue();
			List<V> movingList = new ArrayList<V>();
			for (V v : valList) {
				T curBin = binHash.getBinFor(v);
				if (curBin == newBin) {
					movingList.add(v);

				} else if (bin != curBin) {
					throw new RuntimeException("wtf;  value moved to a random bin!");
				}
			}
			newBinValues.addAll(movingList);
			valList.removeAll(movingList);
			fireMoved(bin, newBin, movingList);
		}
		valueLists.put(newBin, newBinValues);
	}

	public void removeBin(T bin) {
		List<V> moving = valueLists.get(bin);
		binHash.removeBin(bin);
		for (Map.Entry<T, List<V>> ent : valueLists.entrySet()) {
			T dstBin = ent.getKey();
			List<V> oldList = ent.getValue();
			List<V> movedList = new ArrayList<V>();
			for (V v : moving) {
				T newBin = binHash.getBinFor(v);
				if (newBin.equals(dstBin)) {
					movedList.add(v);
				}
			}
			oldList.addAll(movedList);
			fireMoved(bin, dstBin, movedList);
		}
		valueLists.remove(bin);
	}
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Map.Entry<T, List<V>> ent : valueLists.entrySet()) {
			T bin = ent.getKey();
			List<V> vs = ent.getValue();
			buf.append(bin);
			buf.append(" => ");
			buf.append(vs);
			buf.append('\n');
		}
		return buf.toString();
	}

	public List<T> keys() {
		List<T> ks = new ArrayList<T>();
		ks.addAll(valueLists.keySet());
		return ks;
	}
}