import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtascs();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    Task getTaskById(int var1);

    Task getEpicById(int var1);

    Task getSubtaskById(int var1);

    void createTask(Task var1);

    void createEpic(Epic var1);

    void createSubtask(Subtask var1);

    void updateTask(Task var1);

    void updateEpic(Epic var1);

    void updateSubtask(Subtask var1);

    void deleteTaskById(int var1);

    void deleteEpicById(int var1);

    void deleteSubtaskById(int var1);

    ArrayList<Subtask> getSubtasksFromEpic(int var1);

    List<Task> getHistory();
}