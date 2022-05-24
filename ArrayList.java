import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class ArrayList<E> implements List<E> {
  // instance variables
  /** Default array capacity. */
  public static final int CAPACITY=16;     // default array capacity

  /** Generic array used for storage of list elements. */
  private E[] data;                        // generic array used for storage

  /** Current number of elements in the list. */
  private int size = 0;                    // current number of elements

  // constructors
  /** Creates an array list with default initial capacity. */
  public ArrayList() { this(CAPACITY); }   // constructs list with default capacity

  /** Creates an array list with given initial capacity. */
  @SuppressWarnings({"unchecked"})
  public ArrayList(int capacity) {         // constructs list with given capacity
    data = (E[]) new Object[capacity];     // safe cast; compiler may give warning
  }

  // public methods
  /**
   * Returns the number of elements in the list.
   * @return number of elements in the list
   */
  public int size() { return size; }

  /**
   * Tests whether the array list is empty.
   * @return true if the array list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Returns (but does not remove) the element at index i.
   * @param  i   the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E get(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    return data[i];
  }

  /**
   * Replaces the element at the specified index, and returns the element previously stored.
   * @param  i   the index of the element to replace
   * @param  e   the new element to be stored
   * @return the previously stored element
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
   */
  public E set(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    data[i] = e;
    return temp;
  }

  /**
   * Inserts the given element at the specified index of the list, shifting all
   * subsequent elements in the list one position further to make room.
   * @param  i   the index at which the new element should be stored
   * @param  e   the new element to be stored
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public void add(int i, E e) throws IndexOutOfBoundsException {
    checkIndex(i, size + 1);
    if (size == data.length)               // not enough capacity
      resize(2 * data.length);             // so double the current capacity
    for (int k=size-1; k >= i; k--)        // start by shifting rightmost
      data[k+1] = data[k];
    data[i] = e;                           // ready to place the new element
    size++;
  }

  /**
   * Removes and returns the element at the given index, shifting all subsequent
   * elements in the list one position closer to the front.
   * @param  i   the index of the element to be removed
   * @return the element that had be stored at the given index
   * @throws IndexOutOfBoundsException if the index is negative or greater than size()
   */
  public E remove(int i) throws IndexOutOfBoundsException {
    checkIndex(i, size);
    E temp = data[i];
    for (int k=i; k < size-1; k++)         // shift elements to fill hole
      data[k] = data[k+1];
    data[size-1] = null;                   // help garbage collection
    size--;
    return temp;
  }

  // utility methods
  /** Checks whether the given index is in the range [0, n-1]. */
  protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
    if (i < 0 || i >= n)
      throw new IndexOutOfBoundsException("Illegal index: " + i);
  }

  /** Resizes internal array to have given capacity >= size. */
  @SuppressWarnings({"unchecked"})
  protected void resize(int capacity) {
    E[] temp = (E[]) new Object[capacity];     // safe cast; compiler may give warning
    for (int k=0; k < size; k++)
      temp[k] = data[k];
    data = temp;                               // start using the new array
  }

  //---------------- nested ArrayIterator class ----------------
  /**
   * A (nonstatic) inner class. Note well that each instance contains an implicit
   * reference to the containing list, allowing it to access the list's members.
   */
  private class ArrayIterator implements Iterator<E> {
    /** Index of the next element to report. */
    private int j = 0;                   // index of the next element to report
    private boolean removable = false;   // can remove be called at this time?

    /**
     * Tests whether the iterator has a next object.
     * @return true if there are further objects, false otherwise
     */
    public boolean hasNext() { return j < size; }   // size is field of outer instance

    /**
     * Returns the next object in the iterator.
     *
     * @return next object
     * @throws NoSuchElementException if there are no further elements
     */
    public E next() throws NoSuchElementException {
      if (j == size) throw new NoSuchElementException("No next element");
      removable = true;   // this element can subsequently be removed
      return data[j++];   // post-increment j, so it is ready for future call to next
    }

    /**
     * Removes the element returned by most recent call to next.
     * @throws IllegalStateException if next has not yet been called
     * @throws IllegalStateException if remove was already called since recent next
     */
     public void remove() throws IllegalStateException {
      if (!removable) throw new IllegalStateException("nothing to remove");
      ArrayList.this.remove(j-1);  // that was the last one returned
      j--;                         // next element has shifted one cell to the left
      removable = false;           // do not allow remove again until next is called
      } 
  } //------------ end of nested ArrayIterator class ------------
  
  private class MyListIterator implements ListIterator<E> {

	private int j = 0;                   
	private boolean removable = false;   
	private int setTemp=0;
	
	public MyListIterator(){
		new MyListIterator(0);
	}
	public MyListIterator(int i){
		for(int j=0;j<i;j++)
			next();
	}
	    
	@Override
	public void add(E e) {
		data[j]=e;
		j++;
		setTemp=0;
		removable=true;
	}

	@Override
	public boolean hasNext() {
		return j<size;
	}

	@Override
	public boolean hasPrevious() {
		return j>0;
	}

	@Override
	public E next() throws NoSuchElementException {
		if(j==size) throw new NoSuchElementException("No next element");
		removable = true;
		setTemp=1;
		return data[j++];
	}

	@Override
	public int nextIndex() {
		if(j==size) throw new NoSuchElementException("No next element");
		return j;
	}

	@Override
	public E previous() {
		if(j==0) throw new NoSuchElementException("No previous element");
		if(j==1)
			removable=false;
		setTemp=1;
		return data[--j];
	}

	@Override
	public int previousIndex() {
		if(j==0) throw new NoSuchElementException("No previous element");
		return j-1;
	}

	@Override
	public void remove() {
		if (!removable) throw new IllegalStateException("nothing to remove");
		ArrayList.this.remove(j-1);  
	    j--;     
	    setTemp=0;
	    removable = false;
	}

	@Override
	public void set(E e) {
		if(setTemp==0) throw new IllegalStateException("nothing to set");
		else
			data[j]=e;
	}
  }

  /**
   * Returns an iterator of the elements stored in the list.
   * @return iterator of the list's elements
   */
  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator();     // create a new instance of the inner class
  }
  
  public ListIterator<E> myListIterator() {
	return new MyListIterator();     
  }
  
  public ListIterator<E> myListIterator(int i) {
	  return new MyListIterator(i);     
  }

  /**
   * Produces a string representation of the contents of the indexed list.
   * This exists for debugging purposes only.
   *
   * @return textual representation of the array list
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    for (int j = 0; j < size; j++) {
      if (j > 0) sb.append(", ");
      sb.append(data[j]);
    }
    sb.append(")");
    return sb.toString();
  }
}