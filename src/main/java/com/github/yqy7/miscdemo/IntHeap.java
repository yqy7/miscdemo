package com.github.yqy7.miscdemo;

/**
 * @author qiyun.yqy
 * @date 2020/7/4
 */
public class IntHeap {
    int[] queue;
    int size;

    public IntHeap(int capacity) {
        this.queue = new int[capacity];
    }

    public boolean enqueue(int n) {
        if (queue.length == size) {
            return false;
        }

        queue[size] = n;
        size++;
        siftUp(size - 1);
        return true;
    }

    private void siftUp(int i) {
        int tmp = queue[i];
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (queue[i] >= queue[parent]) {
                break;
            }
            queue[i] = queue[parent];
            i = parent;
        }
        queue[i] = tmp;
    }

    public int dequeue() {
        int t = queue[0];
        queue[0] = queue[size-1];
        size--;
        siftDown(0);
        return t;
    }

    private void siftDown(int i) {
        int tmp = queue[i];
        int half = size / 2;
        while (i < half) {
            int child = i * 2 + 1;
            int right = child + 1;
            if (right < size && queue[right] < queue[child]) {
                child = right;
            }

            if (queue[i] <= queue[child]) {
                break;
            }

            queue[i] = queue[child];
            i = child;
        }
        queue[i] = tmp;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        IntHeap heap = new IntHeap(3);
        heap.enqueue(6);
        heap.enqueue(5);
        heap.enqueue(8);
        heap.enqueue(9);
        while (heap.size() > 0) {
            System.out.println(heap.dequeue());
        }
    }
}
