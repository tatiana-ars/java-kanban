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
    void testRemoveShouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task1", "a");
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(0, history.size());
    }

    @Test
    void testAddShouldRemoveOldVersionOfTaskFromHistory() {
        Task task = new Task("Task", "a");
        historyManager.add(task);
        task = new Task("Task", "b");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals("b", history.get(0).getDescription());
    }

    @Test
    void testGetNullHistory() {
        Assertions.assertEquals(historyManager.getHistory().size(), 0);
    }

}