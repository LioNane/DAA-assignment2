package dataStructures;


import metrics.Metrics;

public class DynamicArray {
    private int[] arr;
    private int size;
    private Metrics metrics;

    public DynamicArray(Metrics metrics){
        this.arr = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    private void grow(){
        int old_len = arr.length;
        int[] new_arr = new int[old_len * 2];
        System.arraycopy(arr, 0, new_arr, 0, old_len);
        arr = new_arr;
    }

    public void add(int x){
        if(size >= arr.length){
            grow();
        }
        metrics.incrementSteps();
        metrics.incrementMoves();
        arr[size] = x;
        size++;
    }

    public void add(int index, int x){
        if(index > size || index < 0){
            throw new IndexOutOfBoundsException("Invalid index");
        }
        if(size >= arr.length){
            grow();
        }
        for (int i = size; i > index; i--) {
            metrics.incrementSteps();
            metrics.incrementSteps();
            metrics.incrementMoves();
            arr[i] = arr[i - 1];
        }

        metrics.incrementSteps();
        metrics.incrementMoves();
        arr[index] = x;
        size++;
    }

    public void remove(int index){
        if(index >= size|| index < 0){
            throw new IndexOutOfBoundsException("Invalid index");
        }
        for (int i = index; i < size - 1; i++) {
            metrics.incrementSteps();
            metrics.incrementSteps();
            metrics.incrementMoves();
            arr[i] = arr[i + 1];
        }
        size--;
    }

    public int get(int index){
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Invalid index");
        } else {
            metrics.incrementSteps();
            return arr[index];
        }
    }

    public boolean contains(int x){
        for (int i = 0; i < size; i++) {
            metrics.incrementSteps();
            metrics.incrementComparisons();
            if (arr[i] == x){
                return true;
            }
        }
        return false;
    }

    public int getSize(){
        return size;
    }
}
