import com.tatianaars.kanban.model.Subtask;
import com.tatianaars.kanban.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void testEqualsSubtasksShouldBeEqualsIfIdEquals() {

        Subtask subtask = new Subtask("a", "b", 1, 5, Status.IN_PROGRESS);
        Subtask subtask1 = new Subtask("c","d",8,5,  Status.NEW);

        Assertions.assertEquals(subtask, subtask1);

    }

}