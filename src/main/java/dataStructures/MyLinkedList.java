package dataStructures;

import metrics.Metrics;

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
    private Metrics metrics;

    public MyLinkedList(Metrics metrics){
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.metrics = metrics;
    }

    private Node getNode(int index){
        Node current;
        if (index < size / 2){
            current = head;
            for (int i = 0; i < index; i++) {
                metrics.incrementSteps();
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                metrics.incrementSteps();
                current = current.prev;
            }
        }
        return current;
    }

    public void add(int x){
        Node newNode = new Node(x);
        if(head == null){
            metrics.incrementMoves();
            metrics.incrementMoves();
            head = tail = newNode;
        } else {
            metrics.incrementMoves();
            metrics.incrementMoves();
            metrics.incrementMoves();
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
                metrics.incrementMoves();
                metrics.incrementMoves();
                head = tail = newNode;
            } else {
                metrics.incrementMoves();
                metrics.incrementMoves();
                metrics.incrementMoves();
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        } else {

            Node current = getNode(index);

            metrics.incrementMoves();
            metrics.incrementMoves();

            newNode.next = current.next;
            newNode.prev = current.prev;

            metrics.incrementMoves();
            metrics.incrementMoves();

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
            metrics.incrementMoves();
            head = nextNode;
        } else {
            metrics.incrementMoves();
            metrics.incrementMoves();
            prevNode.next = nextNode;
            current.prev = null;
        }

        if (nextNode == null) {
            metrics.incrementMoves();
            tail = prevNode;
        } else {
            metrics.incrementMoves();
            metrics.incrementMoves();
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
            metrics.incrementSteps();
            metrics.incrementComparisons();
            if (current.value == x){
                return true;
            }
            metrics.incrementSteps();
            current = current.next;
        }
        return false;
    }
}
