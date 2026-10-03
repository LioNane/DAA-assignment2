package dataStructures;

public class MyLinkedList {
    private static class Node{
        int value;
        Node next;
        Node prev;

        public Node(int value){
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public MyLinkedList(){
        head = null;
        tail = null;
        size = 0;
    }

    private Node getNode(int index){Node current;
        if (index < size / 2){
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    public void add(int x){
        Node newNode = new Node(x);
        if(head == null){
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, int x){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Invalid index");
        }

        if (index == size){
            add(x);
            return;
        }

        Node newNode = new Node(x);

        if (index == 0){
            if (head == null){
                head = tail = newNode;;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        } else {

            Node current = getNode(index);

            newNode.next = current.next;
            newNode.prev = current.prev;

            current.prev.next = newNode;
            current.prev = newNode;
        }
        size++;
    }

    public void remove(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Invalid index");
        }

        Node current = getNode(index);

        Node prevNode = current.prev;
        Node nextNode = current.next;
        
        if (prevNode == null){
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            current.prev = null;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            current.next = null;
        }

        size--;
    }

    public int get(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Invalid index");
        }

        return getNode(index).value;
    }

    public boolean contains(int x){
        Node current = head;
        while (current != null){
            if (current.value == x){
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
