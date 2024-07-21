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

    public void setId(int newId) {
        this.id = newId;
    }

    public void putTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void putEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void putSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic thisEpic = epics.get(subtask.getEpicId());
        if (thisEpic != null) {
            thisEpic.getSubtasksIdInEpic().add(subtask.getId());
            checkEpic(subtask.getEpicId());
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
        for (Task task: tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Subtask subtask: subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        for (Epic epic: epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask: subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIdInEpic().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
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
            tasks.put(updateTask.getId(), updateTask);
        }

    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.put(updateEpic.getId(), updateEpic);
        }

    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        if (updateSubtask != null) {
            int epicId = updateSubtask.getEpicId();
            if (subtasks.containsKey(updateSubtask.getId())) {
                subtasks.put(updateSubtask.getId(), updateSubtask);
                checkEpic(epicId);
            }


        }
    }

    @Override
    public void deleteTaskById(int taskId) {
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}