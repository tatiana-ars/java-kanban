package com.tatianaars.kanban.service;

import com.tatianaars.kanban.exception.NotFoundException;
import com.tatianaars.kanban.model.Epic;
import com.tatianaars.kanban.model.Subtask;
import com.tatianaars.kanban.model.Task;
import com.tatianaars.kanban.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap();
    private HashMap<Integer, Epic> epics = new HashMap();
    private HashMap<Integer, Subtask> subtasks = new HashMap();
    private HistoryManager historyManager;
    private int id = 1;
    private TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void putTask(Task task) {
        tasks.put(task.getId(), task);
        addTaskToSortedTasks(task);
    }

    public void putEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void putSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        addTaskToSortedTasks(subtask);
        Epic thisEpic = epics.get(subtask.getEpicId());
        if (thisEpic != null) {
            thisEpic.getSubtasksIdInEpic().add(subtask.getId());
            checkEpic(subtask.getEpicId());
        }
    }

    private void addTaskToSortedTasks(Task task) {
        if (task.getStartTime() != null) {
            for (Task taskS : sortedTasks) {
                if (!isNotOverlay(task, taskS)) {
                    return;
                }
            }
            sortedTasks.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    public static boolean isNotOverlay(Task task1, Task task2) {
        if (task1.getEndTime().isBefore(task2.getStartTime()) || task1.getStartTime().isAfter(task2.getEndTime())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.values().stream()
                .map(Task::getId)
                .forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.values().stream()
                .map(Subtask::getId)
                .forEach(historyManager::remove);

        epics.values().stream()
                .map(Epic::getId)
                .forEach(historyManager::remove);

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.values().stream()
                .map(Subtask::getId)
                .forEach(historyManager::remove);

        subtasks.clear();

        epics.values().stream()
                .forEach(epic -> {
                    epic.getSubtasksIdInEpic().clear();
                    epic.setStatus(Status.NEW);
                });
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException("Задача с id " + taskId + " не найдена.");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new NotFoundException("Эпик с id " + epicId + " не найден.");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с id " + subtaskId + " не найдена.");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            addTaskToSortedTasks(task);
            task.setId(id);
            tasks.put(id, task);
            id++;
        }

    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(id);
            epics.put(id, epic);
            id++;
        }

    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            addTaskToSortedTasks(subtask);
            Epic thisEpic = epics.get(subtask.getEpicId());
            if (thisEpic != null) {
                subtask.setId(id);
                subtasks.put(id, subtask);
                thisEpic.getSubtasksIdInEpic().add(id);
                checkEpic(subtask.getEpicId());
                id++;
            }
        }

    }

    @Override
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            try {
                sortedTasks.remove(tasks.get(updateTask.getId()));
            } catch (NullPointerException e) {
                System.out.println("Ошибка удаления задачи из TreeSet sortedTasks: " + e.getMessage());
            }
            tasks.put(updateTask.getId(), updateTask);
            addTaskToSortedTasks(updateTask);
        } else {
            throw new NotFoundException("Задача с id " + updateTask.getId() + " не найдена.");
        }

    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.put(updateEpic.getId(), updateEpic);
        } else {
            throw new NotFoundException("Эпик с id " + updateEpic.getId() + " не найден.");
        }
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        if (updateSubtask != null) {
            int epicId = updateSubtask.getEpicId();
            if (subtasks.containsKey(updateSubtask.getId())) {
                try {
                    sortedTasks.remove(subtasks.get(updateSubtask.getId()));
                } catch (NullPointerException e) {
                    System.out.println("Ошибка удаления подзадачи из TreeSet sortedTasks:" + e.getMessage());
                }
                addTaskToSortedTasks(updateSubtask);
                subtasks.put(updateSubtask.getId(), updateSubtask);
                checkEpic(epicId);
            } else {
                throw new NotFoundException("Эпик с id " + updateSubtask.getId() + " не найден.");
            }
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        sortedTasks.remove(getTaskById(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic thisEpic = epics.remove(epicId);
        ArrayList<Integer> subtusksId = thisEpic.getSubtasksIdInEpic();

        for (int id : subtusksId) {
            subtasks.remove(id);
            historyManager.remove(id);
        }
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        sortedTasks.remove(getSubtaskById(subtaskId));
        int epicId = (subtasks.get(subtaskId)).getEpicId();
        subtasks.remove(subtaskId);
        ArrayList<Integer> substaksId = (epics.get(epicId)).getSubtasksIdInEpic();
        substaksId.remove(Integer.valueOf(subtaskId));
        checkEpic(epicId);
        historyManager.remove(subtaskId);
    }

    @Override
    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        ArrayList<Subtask> subtasksInEpic = new ArrayList();
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtasksIdInEpic = thisEpic.getSubtasksIdInEpic();

        for (int subtaskId : subtasksIdInEpic) {
            subtasksInEpic.add(subtasks.get(subtaskId));
        }

        return subtasksInEpic;
    }

    private void checkEpic(int epicId) {
        checkStatusEpic(epicId);
        checkTimeEpic(epicId);
    }

    private void checkStatusEpic(int epicId) {
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtasksIdInThisEpic = thisEpic.getSubtasksIdInEpic();
        if (subtasksIdInThisEpic.size() == 0) {
            thisEpic.setStatus(Status.NEW);
            return;
        }

        int checkDone = 0;

        for (int id : subtasksIdInThisEpic) {
            Subtask thisSubtask = subtasks.get(id);
            if (thisSubtask.getStatus() == Status.IN_PROGRESS) {
                thisEpic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (thisSubtask.getStatus() == Status.DONE) {
                checkDone++;
            }
        }

        if (subtasksIdInThisEpic.size() == checkDone) {
            thisEpic.setStatus(Status.DONE);
        } else if (checkDone != 0) {
            thisEpic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void checkTimeEpic(int epicId) {
        Epic thisEpic = epics.get(epicId);
        thisEpic.setStartTime(LocalDateTime.of(0, 1, 1, 0, 0, 0));
        Duration totalDuration = Duration.ofMinutes(0);
        for (int id : thisEpic.getSubtasksIdInEpic()) {
            Subtask thisSubtask = subtasks.get(id);
            try {
                if (thisSubtask.getStartTime().isBefore(thisEpic.getStartTime())) {
                    thisEpic.setStartTime(thisSubtask.getStartTime());
                }
            } catch (NullPointerException e) {
                System.out.println("Ошибка при сравнении StartTime: " + e.getMessage());
            }
            try {
                if (thisSubtask.getEndTime().isAfter(thisEpic.getEndTime())) {
                    thisEpic.setEndTime(thisSubtask.getEndTime());
                }
            } catch (NullPointerException e) {
                System.out.println("Ошибка при сравнении EndTime: " + e.getMessage());
            }
            try {
                totalDuration = totalDuration.plus(thisSubtask.getDuration());
            } catch (NullPointerException e) {
                System.out.println("Ошибка при расчете totalDuration: " + e.getMessage());
            }

        }
        thisEpic.setDuration(totalDuration);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}