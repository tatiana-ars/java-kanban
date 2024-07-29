import com.tatianaars.kanban.model.Epic;
import com.tatianaars.kanban.model.Subtask;
import com.tatianaars.kanban.model.Task;
import com.tatianaars.kanban.service.FileBackedTaskManager;
import com.tatianaars.kanban.service.Managers;
import com.tatianaars.kanban.service.TaskManager;
import com.tatianaars.kanban.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    public void beforeEach() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void testIsOverlay() {
        Task task1 = new Task("task1", "a", Status.NEW, 1,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                Duration.ofHours(100));
        Task task2 = new Task("task2", "a", Status.NEW, 1,
                LocalDateTime.of(2024, 1, 1, 20, 0),
                Duration.ofHours(10000));
        Assertions.assertTrue(!FileBackedTaskManager.isNotOverlay(task1, task2));
    }

    @Test
    void testCreateTaskShouldAddTaskToTasksList() {
        int count = taskManager.getTasks().size();
        taskManager.createTask(new Task("a", "b"));
        int count2 = taskManager.getTasks().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void testCreateSubtaskShouldAddSubtaskToSubtasksList() {
        int count = taskManager.getSubtasks().size();
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        taskManager.createSubtask(new Subtask("a", "b", 1));
        int count2 = taskManager.getSubtasks().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void testCreateEpicShouldAddpicToEpicsList() {
        int count = taskManager.getEpics().size();
        taskManager.createEpic(new Epic("a", "b"));
        int count2 = taskManager.getEpics().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void testGetTaskByIdShouldReturnTaskById() {
        Task task = new Task("a", "b", Status.NEW, 1);
        Task task1 = new Task("a", "b");
        taskManager.createTask(task1);
        Assertions.assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void testGetEpicByIdShouldReturnEpicById() {
        Epic epic = new Epic("a", "b", 1);
        Epic epic1 = new Epic("a", "b");
        taskManager.createEpic(epic1);
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void testGetSubtaskByIdShouldReturnSubtaskById() {
        Subtask subtask = new Subtask("a", "b", 1, 2, Status.NEW);
        Subtask subtask1 = new Subtask("a", "b", 1);
        taskManager.createEpic(new Epic("b", "c"));
        taskManager.createSubtask(subtask1);
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    void testDeleteTasksShouldDeleteAllTasksFromTasksList() {
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        taskManager.deleteTasks();
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void testCreateTaskTaskWithIdShouldNotConflictWithTaskInTaskManager() {
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a", "b");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task1);
        Assertions.assertTrue(task1.getId() == 3);
    }

    @Test
    void testUpdateTaskTaskInTaskManagerShouldBeEqualsWithUpdatedTask() {
        Task task1 = new Task("a", "b");
        taskManager.createTask(task1);
        int id = task1.getId();
        Task task2 = new Task("c", "d", Status.IN_PROGRESS, id);
        taskManager.updateTask(task2);
        Task taskInManager = taskManager.getTaskById(id);
        Assertions.assertEquals(task2.getName(), taskInManager.getName());
        Assertions.assertEquals(task2.getDescription(), taskInManager.getDescription());
    }

    @Test
    void testDeleteTaskByIdShouldDeleteTaskFromTasksList() {
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        int count = taskManager.getTasks().size();
        taskManager.deleteTaskById(task.getId());
        int count2 = taskManager.getTasks().size();
        Assertions.assertEquals(1, count - count2);
    }

    @Test
    void testCreateTaskNewTaskShouldBeEqualsWithTaskInManager() {
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        Task taskInManager = taskManager.getTaskById(task.getId());
        Assertions.assertEquals(task.getName(), taskInManager.getName());
        Assertions.assertEquals(task.getDescription(), taskInManager.getDescription());
    }

    @Test
    void testCreateEpicNewEpicShouldBeEqualsWithEpicInManager() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Task EpicInManager = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(epic.getName(), EpicInManager.getName());
        Assertions.assertEquals(epic.getDescription(), EpicInManager.getDescription());
    }

    @Test
    void testCreateSubtaskNewSubtaskShouldBeEqualsWithSubtaskInManager() {
        Epic epic = new Epic("a", "b");
        Subtask subtask = new Subtask("a", "b", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        Subtask SubtaskInManager = (Subtask) taskManager.getSubtaskById(subtask.getId());
        Assertions.assertEquals(subtask.getName(), SubtaskInManager.getName());
        Assertions.assertEquals(subtask.getDescription(), SubtaskInManager.getDescription());
        Assertions.assertEquals(subtask.getEpicId(), SubtaskInManager.getEpicId());
    }

    @Test
    void testDeleteSubtaskShouldDeleteSubtaskIdFromEpic() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("a", "1", 1);
        Subtask subtask2 = new Subtask("b", "2", 1);
        Subtask subtask3 = new Subtask("c", "3", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.deleteSubtaskById(2);
        assertTrue(epic.getSubtasksIdInEpic().contains(3));
        assertTrue(epic.getSubtasksIdInEpic().contains(4));
        assertTrue(epic.getSubtasksIdInEpic().size() == 2);
    }

    @Test
    void testcheckDONEStatusEpic() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("a", "1", 1, 2, Status.DONE);
        Subtask subtask2 = new Subtask("b", "2", 1, 3, Status.DONE);
        Subtask subtask3 = new Subtask("c", "3", 1, 4, Status.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void testcheckNEWDONEStatusEpic() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("a", "1", 1, 2, Status.NEW);
        Subtask subtask2 = new Subtask("b", "2", 1, 3, Status.DONE);
        Subtask subtask3 = new Subtask("c", "3", 1, 4, Status.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void testcheckINPROGRESSStatusEpic() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("a", "1", 1, 2, Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("b", "2", 1, 3, Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask("c", "3", 1, 4, Status.IN_PROGRESS);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void testcheckNEWStatusEpic() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("a", "1", 1);
        Subtask subtask2 = new Subtask("b", "2", 1);
        Subtask subtask3 = new Subtask("c", "3", 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void testGetPrioritizedTasks() {

        Task task1 = new Task("1", "задача 1", Status.NEW, 1,
                LocalDateTime.of(2024, 7, 1, 7, 0, 0), Duration.ofMinutes(20));
        Task task2 = new Task("2", "задача 2", Status.NEW, 2,
                LocalDateTime.of(2024, 7, 1, 7, 0, 0), Duration.ofMinutes(60));
        Task task3 = new Task("3", "задача 3", Status.NEW, 3,
                LocalDateTime.of(2024, 7, 10, 7, 0, 0), Duration.ofMinutes(60));
        Task task4 = new Task("4", "задача 4", Status.NEW, 4,
                LocalDateTime.of(2024, 7, 9, 7, 0, 0), Duration.ofMinutes(60));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);

        List<Task> assertList = new ArrayList<>(Arrays.asList(task1, task4, task3));

        assertEquals(taskManager.getPrioritizedTasks(), assertList);
    }

}
