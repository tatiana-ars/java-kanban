import com.tatianaars.kanban.service.InMemoryTaskManager;
import com.tatianaars.kanban.service.Managers;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
    }
}
