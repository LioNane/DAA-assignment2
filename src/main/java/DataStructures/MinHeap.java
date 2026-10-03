package DataStructures;

public class MinHeap {
    private int[] data;
    private int size;

    public MinHeap() {
        data = new int[10];
        size = 0;
    }

    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private int getParentIndex(int index){
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index){
        return 2 * index + 1;
    }

    private int getRightChildIndex(int index){
        return 2 * index + 2;
    }

    private void bubbleUp(int index){
        int parent_index = getParentIndex(index);

        while (index > 0 && data[index] < data[parent_index]){
            swap(data, index, parent_index);
            index = parent_index;
            parent_index = getParentIndex(index);
        }
    }

    private void bubbleDown(int index){
        int left_child_index = getLeftChildIndex(index);
        int right_child_index = getRightChildIndex(index);

        int minIndex;

        while (left_child_index < size){
            minIndex = left_child_index;
            if (right_child_index < size && data[right_child_index] < data[minIndex]){
                minIndex = right_child_index;
            }
            if (data[index] <= data[minIndex]){
                break;
            } else {
                swap(data, index, minIndex);
                index = minIndex;
            }
            left_child_index = getLeftChildIndex(index);
            right_child_index = getRightChildIndex(index);
        }
    }

    private void grow(){
        int old_len = data.length;
        int[] new_data = new int[old_len * 2];
        System.arraycopy(data, 0, new_data, 0, old_len);
        data = new_data;
    }

    private void insert(int x){
        if (size >= data.length){
            grow();
        }

        data[size] = x;

        bubbleUp(size);

        size++;
    }

    public int peekMin(){
        if (size == 0){
            throw new IllegalStateException("Heap is empty");
        }
        return data[0];
    }

    public int extractMin(){
        if (size == 0){
            throw new IllegalStateException("Heap is empty");
        }

        int temp_min = data[0];
        data[0] = data[size - 1];
        size--;
        return temp_min;
    }

}
