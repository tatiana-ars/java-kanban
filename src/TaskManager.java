import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtascs() {
        return new ArrayList<>(subtasks.values());
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
        }
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
            if (epics.get(subtask.getEpicId()) != null) {
                subtask.setId(id);
                subtasks.put(id, subtask);
                Epic thisEpic = epics.get(subtask.getEpicId());
                thisEpic.getSubtasksIdInEpic().add(id);
                id++;
                checkEpic(subtask.getEpicId());
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
        if (subtasks.containsKey(updateSubtask.getId())) {
            int epicId = updateSubtask.getEpicId();
            subtasks.put(updateSubtask.getId(), updateSubtask);
            checkEpic(epicId);
        }
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        Epic thisEpic = epics.get(epicId);
        ArrayList<Integer> subtusksId = thisEpic.getSubtasksIdInEpic();
        for (int id : subtusksId) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    public void deleteSubtaskById(int subtaskId) {
        //проверить статус других задач эпика + наличие других эпиков
        int epicId = subtasks.get(subtaskId).getEpicId();
        subtasks.remove(subtaskId);
        ArrayList<Integer> substaksId = epics.get(epicId).getSubtasksIdInEpic();
        substaksId.remove(subtaskId);
        checkEpic(epicId);
    }

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
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

        int checkDone = 0;

        for (Integer id : subtasksIdInThisEpic) {
            Subtask thisSubtask = subtasks.get(id);
            if (thisSubtask.getStatus() == Status.IN_PROGRESS) {
                thisEpic.setStatus(Status.IN_PROGRESS);
            } else if (thisSubtask.getStatus() == Status.DONE) {
                checkDone++;
            } else if (subtasksIdInThisEpic.size() == 0) {
                thisEpic.setStatus(Status.NEW);
            }
        }

        if (subtasksIdInThisEpic.size() == checkDone) {
            thisEpic.setStatus(Status.DONE);
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
