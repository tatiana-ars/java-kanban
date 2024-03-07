import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task value : tasks.values()) {
            allTasks.add(value);
        }
        return allTasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic value : epics.values()) {
            allEpics.add(value);
        }
        return allEpics;
    }

    public ArrayList<Subtask> getSubtascs() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (Subtask value : subtasks.values()) {
            allSubtasks.add(value);
        }
        return allSubtasks;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Task getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Task getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void createTask(Task task) {
        task.id = id;
        tasks.put(id, task);
        id++;
    }

    public void createEpic(Epic epic) {
        epic.id = id;
        epics.put(id, epic);
        id++;
    }

    public void createSubtask(Subtask subtask) {
        subtask.id = id;
        subtasks.put(id, subtask);
        Epic thisEpic = epics.get(subtask.getEpicId());
        thisEpic.getSubtasksIdInEpic().add(id);
        id++;
    }

    public void updateTask(int taskId, Task updateTask) {
        tasks.put(taskId, updateTask);
    }

    public void updateEpic(int epicId, Epic updateEpic) {
        epics.put(epicId, updateEpic);
    }

    public void updateSubtask(int subtaskId, Subtask updateSubtask) {
        int epicId = updateSubtask.getEpicId();
        subtasks.put(subtaskId, updateSubtask);
        checkEpic(epicId);
    }

    public void deleteByIdTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteByIdEpic(int epicId) {
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtusksId = thisEpic.getSubtasksIdInEpic();
        for (int id : subtusksId) {
            deleteByIdSubtask(id);
        }
        // удалить все подзадачи эпика
        epics.remove(epicId);
    }

    public void deleteByIdSubtask(int subtaskId) {
        //проверить статус других задач эпика + наличие других эпиков
        int epicId = subtasks.get(subtaskId).getEpicId();
        subtasks.remove(subtaskId);
    }

    public ArrayList<Subtask> subtasksInEpic(int epicId) {
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtasksIdInEpic = thisEpic.getSubtasksIdInEpic();
        for (int substucId : subtasksIdInEpic) {
            subtasksInEpic.add(subtasks.get(substucId));
        }
        return subtasksInEpic;
    }

    public void checkEpic(int epicId) {
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtasksIdInThisEpic = thisEpic.getSubtasksIdInEpic();
        ArrayList<Subtask> subtasksInThisEpic = new ArrayList<>();
        for (int id : subtasksIdInThisEpic) {
            subtasksInThisEpic.add(subtasks.get(id));
        }
        int checkDone = 0;
        for (Subtask thisSubtask : subtasksInThisEpic) {
            if (thisSubtask.status.equals(Status.IN_PROGRESS)) {
                thisEpic.status = Status.IN_PROGRESS;
            } else if (thisSubtask.status.equals(Status.DONE)) {
                checkDone++;
            }
        }
        if (subtasksIdInThisEpic.size() == checkDone) {
            thisEpic.status = Status.DONE;
        }
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }
}
