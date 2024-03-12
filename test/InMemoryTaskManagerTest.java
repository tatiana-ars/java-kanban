import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void testCreateTaskShouldSaveTaskInTasks() {
        int count = taskManager.getTasks().size();
        taskManager.createTask(new Task("a", "b"));
        int count2 = taskManager.getTasks().size();
        Assertions.assertTrue(count2 - count == 1);
    }


    @Test
    void testCreateEpicShouldSaveEpicInEpics() {
        int count = taskManager.getEpics().size();
        taskManager.createEpic(new Epic("a", "b"));
        int count2 = taskManager.getEpics().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void testCreateSubtaskShouldSaveSubtaskInSubtasks() {
        int count = taskManager.getSubtasks().size();
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        taskManager.createSubtask(new Subtask("a", "b", 1));
        int count2 = taskManager.getSubtasks().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void testCreateTaskShouldGenerateId(){
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        Assertions.assertTrue(task.getId() != 0);
    }

    @Test
    void  testGetTaskByIdShouldReturnTaskById() {
        Task task = new Task("a", "b", Status.NEW, 1);
        Task task1 = new Task("a", "b");
        taskManager.createTask(task1);
        Assertions.assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void  testGetEpicByIdShouldReturnEpicById() {
        Epic epic = new Epic("a", "b",  1);
        Epic epic1 = new Epic("a", "b");
        taskManager.createEpic(epic1);
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void  testGetSubtaskByIdShouldReturnSubtaskById() {
        Subtask subtask = new Subtask("a", "b",  1, 2, Status.NEW);
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
    void testCreateTaskTaskWithIdShouldNotConflictWithTask() {
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
}
