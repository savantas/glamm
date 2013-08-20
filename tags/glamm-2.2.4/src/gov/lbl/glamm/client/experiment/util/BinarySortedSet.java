package gov.lbl.glamm.client.experiment.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


/**
 * List implementation backed by an ArrayList where additions and removals keep
 * elements in sorted order.
 * TODO: Compare performance with recursive and non-recursive algorithms.
 * TODO: Comparable vs Comparator
 * @author DHAP Digital, Inc - angie
 * @param <T>
 *
 */
public class BinarySortedSet<E>
		implements Set<E> {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	ArrayList<E> baseList = null;
	Comparator<E> comparator = null;

	public BinarySortedSet(Comparator<E> comparator) {
		super();
		baseList = new ArrayList<E>();
		this.comparator = comparator;
	}

	/**
	 * Note: if o is equivalent to one of the values in the list,
	 * the index of that value is returned.
	 * Note: if there are multiple values in list equal to o,
	 * there are no guarantees on which of those indices are returned.
	 * 
	 * @param o
	 * @return index if the element is found. Otherwise, returns (-(insertion)-1)
	 */
	public int indexOf( E o ) {
		if ( o == null ) {
			return baseList.size();
		}
		if ( baseList.size() == 0 ) {
			return -1;
		}

		int left = 0;
		int right = baseList.size()-1;
		if ( comparator.compare(o, baseList.get(left)) == 0 ) {
			return left; // o <= [left]
		} else if ( comparator.compare(o, baseList.get(left)) < 0 ) {
			return -left - 1;
		}
		if ( comparator.compare(o, baseList.get(right)) > 0 ) {
			return -(right+1) - 1; // o > [right]
		} else if ( comparator.compare(o, baseList.get(right)) == 0 ) {
			return right; // o == [right]
		}
		return this.indexOf(o, baseList, left, right);
	}
	/**
	 * Recursive function.
	 * Note: if there are multiple values in list equal to o,
	 * there are no guarantees on which of those indices are returned.
	 * 
	 * @param o
	 * @param list
	 * @param left must satisfy: list[left] < o and left >= 0
	 * @param right must satisfy: o < list[right] (and transitively left < right) and right >= 0
	 * @return index if the element is found. Otherwise, returns (-(insertion)-1)
	 */
	protected int indexOf( E o, ArrayList<E> list, int left, int right ) {
		if ( o == null ) {
			return baseList.size();
		}
		int middle = (right+left) >> 1; // divide by 2
		E middleObject = baseList.get(middle);
		if ( comparator.compare(middleObject, o) == 0 ) {
			return middle;
		} else if ( comparator.compare(middleObject, o) < 0 ) {
			// middleObject < o
			if ( middle+1 == right ) {
				// middleObject strictly between two adjacent objects
				return -right - 1;
			}
			return this.indexOf(o, list, middle, right);
		} else {
			// o < middleObject
			return this.indexOf(o, list, left, middle);
		}
	}

	@Override
	public int size() {
		return baseList.size();
	}

	@Override
	public boolean isEmpty() {
		return baseList.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		int index = this.indexOf((E)o);
		if ( index >= 0 ) {
			return true;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return baseList.iterator();
	}

	@Override
	public Object[] toArray() {
		return baseList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return baseList.toArray(a);
	}

	/** Disallows null arguments.
	 * @see java.util.Set#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		if ( e == null ) {
			throw new RuntimeException("Error: null argument");
		}
		int index = this.indexOf(e);
		if ( index >= 0 ) {
			return false;
		} else {
			int insertIndex = -index - 1;
			this.baseList.add(insertIndex, e);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if ( o == null ) {
			throw new RuntimeException("Error: null argument");
		}
		int index = this.indexOf((E)o);
		if ( index >= 0 ) {
			this.baseList.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for ( Object obj : c ) {
			if ( !this.contains(obj) ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = false;
		for ( E obj : c ) {
			if ( this.add(obj) ) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for ( Object obj : c ) {
			if ( this.remove(obj) ) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Error: BinarySortedSet.retainAll not implemented.");
		// TODO Auto-generated method stub
	}

	@Override
	public void clear() {
		this.baseList.clear();
	}

	public E get(int index) {
		return this.baseList.get(index);
	}
}
