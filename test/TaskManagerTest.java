import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    void testIsOverlay() {
        Task task1 = new Task("task1", "a", Status.NEW, 1,
                LocalDateTime.of(2024, 1,1,0,0),
                Duration.ofHours(100));
        Task task2 = new Task("task2", "a", Status.NEW, 1,
                LocalDateTime.of(2024, 1,1,20,0),
                Duration.ofHours(10000));
        System.out.println(FileBackedTaskManager.isOverlay(task1, task2));
        Assertions.assertTrue(!FileBackedTaskManager.isOverlay(task1, task2));
    }
}
