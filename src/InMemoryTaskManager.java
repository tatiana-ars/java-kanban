import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap();
    private HashMap<Integer, Epic> epics = new HashMap();
    private HashMap<Integer, Subtask> subtasks = new HashMap();
    private HistoryManager historyManager;
    private int id = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList(epics.values());
    }

    public ArrayList<Subtask> getSubtascs() {
        return new ArrayList(subtasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIdInEpic().clear();
            epic.setStatus(Status.NEW);
        }
    }

    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(id);
            tasks.put(id, task);
            id++;
        }

    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(id);
            epics.put(id, epic);
            id++;
        }

    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
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

    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            tasks.put(updateTask.getId(), updateTask);
        }

    }

    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.put(updateEpic.getId(), updateEpic);
        }

    }

    public void updateSubtask(Subtask updateSubtask) {
        if (updateSubtask != null) {
            int epicId = updateSubtask.getEpicId();
            if (epics.get(epicId) != null) {
                if (subtasks.containsKey(updateSubtask.getId())) {
                    subtasks.put(updateSubtask.getId(), updateSubtask);
                    checkEpic(epicId);
                }

            }
        }
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        Epic thisEpic = epics.remove(epicId);
        ArrayList<Integer> subtusksId = thisEpic.getSubtasksIdInEpic();

        for (int id : subtusksId) {
            subtasks.remove(id);
        }
    }

    public void deleteSubtaskById(int subtaskId) {
        int epicId = (subtasks.get(subtaskId)).getEpicId();
        subtasks.remove(subtaskId);
        ArrayList<Integer> substaksId = (epics.get(epicId)).getSubtasksIdInEpic();
        substaksId.remove(subtaskId);
        checkEpic(epicId);
    }

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
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}