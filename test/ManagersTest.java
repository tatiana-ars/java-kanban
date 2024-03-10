import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void test_getDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Assertions.assertTrue(taskManager instanceof InMemoryTaskManager);
    }
}