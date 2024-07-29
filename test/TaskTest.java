import com.tatianaars.kanban.model.Task;
import com.tatianaars.kanban.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testEqualsTasksShouldBeEqualsIfIdEquals() {

        Task task = new Task("a", "b", Status.IN_PROGRESS, 1);
        Task task1 = new Task("c","d", Status.NEW, 1);

        Assertions.assertEquals(task, task1);

    }
}