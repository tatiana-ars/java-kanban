import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddShouldAddNotNullTaskToHistory() {
        Task task = new Task("a", "b");
        historyManager.add(task);
        Assertions.assertNotNull(historyManager.getHistory().get(0));
    }

    @Test
    void testAddHistoryShouldNotBeLongerThan10() {
        int i = 0;
        while (i < 11) {
            historyManager.add(new Task("a", "b"));
            i++;
        }
        Assertions.assertTrue(historyManager.getHistory().size() == 10);
    }
}