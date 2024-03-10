import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        return new ArrayList(this.tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList(this.epics.values());
    }

    public ArrayList<Subtask> getSubtascs() {
        return new ArrayList(this.subtasks.values());
    }

    public void deleteTasks() {
        this.tasks.clear();
    }

    public void deleteEpics() {
        this.epics.clear();
        this.subtasks.clear();
    }

    public void deleteSubtasks() {
        this.subtasks.clear();
        Iterator var1 = this.epics.values().iterator();

        while(var1.hasNext()) {
            Epic epic = (Epic)var1.next();
            epic.getSubtasksIdInEpic().clear();
            epic.setStatus(Status.NEW);
        }

    }

    public Task getTaskById(int taskId) {
        this.historyManager.add(this.tasks.get(taskId));
        return this.tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        this.historyManager.add(this.epics.get(epicId));
        return this.epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        this.historyManager.add(this.subtasks.get(subtaskId));
        return this.subtasks.get(subtaskId);
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(this.id);
            this.tasks.put(this.id, task);
            ++this.id;
        }

    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(this.id);
            this.epics.put(this.id, epic);
            ++this.id;
        }

    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            Epic thisEpic = (Epic)this.epics.get(subtask.getEpicId());
            if (thisEpic != null) {
                subtask.setId(this.id);
                this.subtasks.put(this.id, subtask);
                thisEpic.getSubtasksIdInEpic().add(this.id);
                this.checkEpic(subtask.getEpicId());
                ++this.id;
            }
        }

    }

    public void updateTask(Task updateTask) {
        if (this.tasks.containsKey(updateTask.getId())) {
            this.tasks.put(updateTask.getId(), updateTask);
        }

    }

    public void updateEpic(Epic updateEpic) {
        if (this.epics.containsKey(updateEpic.getId())) {
            this.epics.put(updateEpic.getId(), updateEpic);
        }

    }

    public void updateSubtask(Subtask updateSubtask) {
        if (updateSubtask != null) {
            int epicId = updateSubtask.getEpicId();
            if (this.epics.get(epicId) != null) {
                if (this.subtasks.containsKey(updateSubtask.getId())) {
                    this.subtasks.put(updateSubtask.getId(), updateSubtask);
                    this.checkEpic(epicId);
                }

            }
        }
    }

    public void deleteTaskById(int taskId) {
        this.tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        Epic thisEpic = (Epic)this.epics.remove(epicId);
        ArrayList<Integer> subtusksId = thisEpic.getSubtasksIdInEpic();
        Iterator var4 = subtusksId.iterator();

        while(var4.hasNext()) {
            int id = (Integer)var4.next();
            this.subtasks.remove(id);
        }

    }

    public void deleteSubtaskById(int subtaskId) {
        int epicId = ((Subtask)this.subtasks.get(subtaskId)).getEpicId();
        this.subtasks.remove(subtaskId);
        ArrayList<Integer> substaksId = ((Epic)this.epics.get(epicId)).getSubtasksIdInEpic();
        substaksId.remove(subtaskId);
        this.checkEpic(epicId);
    }

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        ArrayList<Subtask> subtasksInEpic = new ArrayList();
        Epic thisEpic = (Epic)this.epics.get(epicId);
        ArrayList<Integer> subtasksIdInEpic = thisEpic.getSubtasksIdInEpic();
        Iterator var5 = subtasksIdInEpic.iterator();

        while(var5.hasNext()) {
            int substucId = (Integer)var5.next();
            subtasksInEpic.add((Subtask)this.subtasks.get(substucId));
        }

        return subtasksInEpic;
    }

    private void checkEpic(int epicId) {
        Epic thisEpic = (Epic)this.epics.get(epicId);
        ArrayList<Integer> subtasksIdInThisEpic = thisEpic.getSubtasksIdInEpic();
        if (subtasksIdInThisEpic.size() == 0) {
            thisEpic.setStatus(Status.NEW);
        } else {
            int checkDone = 0;
            Iterator var5 = subtasksIdInThisEpic.iterator();

            while(var5.hasNext()) {
                Integer id = (Integer)var5.next();
                Subtask thisSubtask = (Subtask)this.subtasks.get(id);
                if (thisSubtask.getStatus() == Status.IN_PROGRESS) {
                    thisEpic.setStatus(Status.IN_PROGRESS);
                    return;
                }

                if (thisSubtask.getStatus() == Status.DONE) {
                    ++checkDone;
                }
            }

            if (subtasksIdInThisEpic.size() == checkDone) {
                thisEpic.setStatus(Status.DONE);
            }

        }
    }

    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }
}