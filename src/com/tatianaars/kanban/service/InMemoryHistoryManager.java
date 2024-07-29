package com.tatianaars.kanban.service;

import com.tatianaars.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        final Node node = historyMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        Node newNode = new Node(task);
        linkLast(newNode);

        historyMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        LinkedList<Task> history = new LinkedList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.addFirst(currentNode.getTask());
            currentNode = currentNode.getNext();
        }
        return history;
    }

    @Override
    public void remove(int id) {
        final Node node = historyMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    private void linkLast(Node newNode) {
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        if (head == null && tail == null) {
            return;
        }

        Node nextNode = node.getNext();
        Node prevNode = node.getPrev();

        if (node == head) {
            head = nextNode;
        } else {
            prevNode.setNext(nextNode);
        }
        if (node == tail) {
            tail = prevNode;
        } else {
            nextNode.setPrev(prevNode);
        }
        historyMap.remove(node.getTask().getId());
    }
}