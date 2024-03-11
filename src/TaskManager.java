import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtascs();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    Task getTaskById(int taskId);

    Task getEpicById(int epicId);

    Task getSubtaskById(int subtaskId);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task updateTask);

    void updateEpic(Epic updateEpic);

    void updateSubtask(Subtask updateSubtask);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(int subtaskId);

    ArrayList<Subtask> getSubtasksFromEpic(int epicId);

    List<Task> getHistory();
}