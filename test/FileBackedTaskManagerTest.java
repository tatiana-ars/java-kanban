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

        List<Task> assertList = new ArrayList<>(Arrays.asList(task1, task2, task3, task4));

        FileBackedTaskManager fbTaskManager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(fbTaskManager.getTasks(), taskManager.getTasks());
    }

}
