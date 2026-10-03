package dataStructures;



public class DynamicArray {
    private int[] arr;
    private int size;

    public DynamicArray(){
        arr = new int[10];
        size = 0;
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
            arr[i] = arr[i - 1];
        }
        arr[index] = x;
        size++;
    }

    public void remove(int index){
        if(index >= size|| index < 0){
            throw new IndexOutOfBoundsException("Invalid index");
        }
        for (int i = index; i < size - 1; i++) {
            arr[i] = arr[i + 1];
        }
        size--;
    }

    public int get(int index){
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Invalid index");
        } else {
            return arr[index];
        }
    }

    public boolean contains(int x){
        for (int i = 0; i < size; i++) {
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
