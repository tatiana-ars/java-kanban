import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testEquals_SubtasksShouldBeEqualsIfIdEquals() {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Subtask subtask = new Subtask("a", "b", 1, 5, Status.IN_PROGRESS);
        Subtask subtask1 = new Subtask("c","d",8,5,  Status.NEW);

        Assertions.assertTrue(subtask.equals(subtask1));

    }

}