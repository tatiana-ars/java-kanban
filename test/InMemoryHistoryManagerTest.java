import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void addShouldAddTaskToHistoryList() {
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        Assertions.assertTrue(taskManager.getTasks().contains(task));
    }

    @Test
    void tasksInHistoryManagerShouldSaveVersion() {
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        Task updatedTask = new Task("c", "d", Status.IN_PROGRESS, 1);
        taskManager.updateTask(updatedTask);
        Assertions.assertEquals(taskManager.getHistory().get(0), task);
    }

}