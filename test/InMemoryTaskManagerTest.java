import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        this.taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void testSreateTaskShouldSaveTask() {
        int count = this.taskManager.getTasks().size();
        this.taskManager.createTask(new Task("a", "b"));
        int count2 = this.taskManager.getTasks().size();
        Assertions.assertTrue(count2 - count == 1);
    }


    @Test
    void createEpicShouldSaveEpic() {
        int count = this.taskManager.getEpics().size();
        this.taskManager.createEpic(new Epic("a", "b"));
        int count2 = this.taskManager.getEpics().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void createSubtaskShouldSaveSubtask() {
        int count = this.taskManager.getSubtascs().size();
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        this.taskManager.createSubtask(new Subtask("a", "b", 1));
        int count2 = this.taskManager.getSubtascs().size();
        Assertions.assertTrue(count2 - count == 1);
    }

    @Test
    void createTaskShouldGenerateId(){
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        Assertions.assertTrue(task.getId() != 0);
    }

    @Test
    void  getTaskByIdShouldReturnTaskById() {
        Task task = new Task("a", "b", Status.NEW, 1);
        Task task1 = new Task("a", "b");
        taskManager.createTask(task1);
        Assertions.assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void  getEpicByIdShouldReturnEpicById() {
        Epic epic = new Epic("a", "b",  1);
        Epic epic1 = new Epic("a", "b");
        taskManager.createEpic(epic1);
        Assertions.assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void  getSubtaskByIdShouldReturnSubtaskById() {
        Subtask subtask = new Subtask("a", "b",  1, 2, Status.NEW);
        Subtask subtask1 = new Subtask("a", "b", 1);
        taskManager.createEpic(new Epic("b", "c"));
        taskManager.createSubtask(subtask1);
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    void deleteTasksShouldDeleteAllTasksFromList() {
        Task task = new Task("a", "b");
        this.taskManager.createTask(task);
        this.taskManager.deleteTasks();
        Assertions.assertEquals(0, this.taskManager.getTasks().size());
    }

    @Test
    void createTaskTaskWithIdShouldNotConflictWithTask() {
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a", "b");
        this.taskManager.createTask(task1);
        this.taskManager.createTask(task2);
        this.taskManager.createTask(task1);
        Assertions.assertTrue(task1.getId() == 3);
    }

    @Test
    void updateTaskTaskInTaskManagerShouldBeEqualsWithUpdatedTask() {
        Task task1 = new Task("a", "b");
        taskManager.createTask(task1);
        int id = task1.getId();
        Task task2 = new Task("c", "d", Status.IN_PROGRESS, id);
        taskManager.updateTask(task2);
        Task taskInManager = taskManager.getTaskById(id);
        Assertions.assertTrue(Objects.equals(task2.getName(), taskInManager.getName()) &&
                Objects.equals(task2.getDescription(), taskInManager.getDescription()));

    }

    @Test
    void deleteTaskById() {
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
        this.taskManager.createTask(task);
        Task task1 = this.taskManager.getTaskById(task.getId());
        Assertions.assertTrue(Objects.equals(task.getName(), task1.getName()) && Objects.equals(task.getDescription(), task1.getDescription()));

        Assertions.assertEquals(task.getName(), task1.getName());
        Assertions.assertEquals(task.getDescription(), task1.getDescription());
    }

    @Test
    void testCreateEpicNewEpicShouldBeEqualsWithEpicInManager() {
        Epic epic = new Epic("a", "b");
        this.taskManager.createEpic(epic);
        Task epic1 = this.taskManager.getEpicById(epic.getId());
        Assertions.assertTrue(Objects.equals(epic.getName(), epic1.getName()) && Objects.equals(epic.getDescription(), epic1.getDescription()));

    }

    @Test
    void testCreateSubtaskNewSubtaskShouldBeEqualsWithSubtaskInManager() {
        Epic epic = new Epic("a", "b");
        Subtask subtask = new Subtask("a", "b", 1);
        this.taskManager.createEpic(epic);
        this.taskManager.createSubtask(subtask);
        Subtask subtask1 = (Subtask)this.taskManager.getSubtaskById(subtask.getId());
        Assertions.assertTrue(Objects.equals(subtask.getName(), subtask1.getName()) && Objects.equals(subtask.getDescription(), subtask1.getDescription()) && Objects.equals(subtask.getEpicId(), subtask1.getEpicId()));
    }

}
