import com.tatianaars.kanban.service.InMemoryTaskManager;
import com.tatianaars.kanban.service.Managers;
import com.tatianaars.kanban.service.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void testGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Assertions.assertTrue(taskManager instanceof InMemoryTaskManager);
    }
}