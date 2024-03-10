import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEquals_TasksShouldBeEqualsIfIdEquals() {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Task task = new Task("a", "b", Status.IN_PROGRESS, 1);
        Task task1 = new Task("c","d", Status.NEW, 1);

        Assertions.assertTrue(task.equals(task1));

    }
}