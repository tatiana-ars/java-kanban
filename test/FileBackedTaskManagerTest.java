import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    private File file;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("temp", ".txt");
        } catch (IOException e) {
            e.getMessage();
        }
        taskManager = new FileBackedTaskManager(file, Managers.getDefaultHistoryManager());
    }

    @Test
    public void testLoadFromFile() {
        Task task1 = new Task("1", "задача 1", Status.NEW, 1,
                LocalDateTime.of(2024, 7, 10, 7, 0, 0), Duration.ofMinutes(20));
        Task task2 = new Task("2", "задача 2", Status.NEW, 2,
                LocalDateTime.of(2024, 7, 1, 7, 0, 0), Duration.ofMinutes(60));
        Epic epic = new Epic("Эпик", "эпик 1", 3);
        Subtask subtask1 = new Subtask("1", "подзадача 1", 3, 9, Status.NEW,
                LocalDateTime.of(2024, 7, 11, 7, 0, 0), Duration.ofMinutes(60));
        Subtask subtask2 = new Subtask("2", "подзадача 2", 3, 9, Status.NEW,
                LocalDateTime.of(2024, 7, 11, 7, 0, 0), Duration.ofMinutes(60));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        FileBackedTaskManager fbTaskManager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(fbTaskManager.getPrioritizedTasks(), taskManager.getPrioritizedTasks());
        Assertions.assertEquals(fbTaskManager.getTasks(), taskManager.getTasks());
        Assertions.assertEquals(fbTaskManager.getSubtasks(), taskManager.getSubtasks());
        Assertions.assertEquals(fbTaskManager.getEpics(), taskManager.getEpics());
    }

}
