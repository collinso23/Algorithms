package edu.wit.cs.comp2350;

public class Heap {
	//init vars
	//private static int HEAP_SIZE = 0;
	//private static int MAX_HEAP = 0;
	private float[] heap;
	//is this var being used or init'd right, arr size keeps getting set to 1.
	private int size;
	//private int lastHeapIndex;
	//replace lastheapindex with just size-1
	
	//heap constructor
	public Heap(float[] a) {
		heap = new float[a.length];
		size = a.length;
		for(int i=0;i<a.length;i++) {
			insert(a[i]);
		}
		//lastHeapIndex = size -1;
	}
	
	//insert value into the heap
	public void insert(float value) {
		/*if (isFull()) {
			size = heap.length * 2;
		} */
		//getting through construction but not building valid heap
		//heap[++size] = value; returns 2.0, 1.0, 1.0, 1.0
		//tried heap[lastHeapIndex] return IndexOutOfBounds: -1
		heap[size] = value;
		pullUp(size);
		size++;
		//lastHeapIndex = size -1;
	}

	// delete value from the heap
	public float delete(int index) {
		/*if (isEmpty()) {
			throw new IndexOutOfBoundsException("Heap empty from del meth");
		}*/
		//int parent = getParent(index);
		float delVal = heap[index];
		//float delVal = heap[0];
		heap[index] = heap[size]; //12.0, 41.0, 21.0, 63.0, 63.0, 41.0, 21.0, 63.0, 41.0, 21.0, 21.0, 41.0, 21.0, 41.0, 41.0, 41.0, "main" ArrayIndexOutOf:-1
		pullDown(index,size-1);//lastHeapIndex
		size--;
		return delVal;
	}
	
	// if val is less than par pullUp
	private void pullUp(int index) {
		float newVal = heap[index];
		while (index > 0 && newVal > heap[getParent(index)]) {
			heap[index] = heap[getParent(index)];
			index = getParent(index);
		}
		heap[index]= newVal;	
	}
		
	//if var is greater than parent pull down
	private void pullDown(int index, int lastHeapIndex) {
		int swap;
		lastHeapIndex = size-1;
		while (index <= lastHeapIndex) {
			int leftChild = getChild(index, true);
			int rightChild = getChild(index, false);
			if (leftChild <= lastHeapIndex) {
				if (rightChild > lastHeapIndex) {
					swap = leftChild;
				}
				else {
					swap = (heap[leftChild] > heap[rightChild] ? leftChild : rightChild);
				}
				if (heap[index] < heap[swap]) {
					float tmp = heap[index];
					heap[index] = heap[swap];
					heap[swap] = tmp;
				}
				else {break;}
				index = swap;
			}
			else {break;}
		}
	} 
	
	
	//get size of heap
	public int getSize() {
		return  heap.length;
	}
	
	//get root of heap
	public float root() {
		/* (isEmpty()) {
			throw new IndexOutOfBoundsException("Heap empty;no root");
		}*/
		return heap[0];
	}
	//is the heap full? t|f
	public boolean isFull() {
		return size == heap.length;
	}
	
	//is the heap empty t|f
	public boolean isEmpty() {
		return size == 0;
	}
	
	//finds parent
	public int getParent(int index) {
		return (index - 1) / 2;
	}
	
	//finds child
	public int getChild(int index, boolean left) {
		return 2 * index  + (left ? 1:2);
	}
	
	//print current heap for testing purpose
	public void printArr() {
		for (int i = 0; i < size; i++) {
			System.err.print(heap[i] + ", ");
		}
	}
}
