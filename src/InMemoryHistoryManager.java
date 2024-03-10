import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList();

    public InMemoryHistoryManager() {
    }

    public void add(Task task) {
        if (task == null) {
            return;
        }
        Task taskCopy = new Task(task);
        this.history.add(taskCopy);
        if (this.history.size() > 10) {
            this.history.remove(0);
        }

    }

    public List<Task> getHistory() {
        return new ArrayList(this.history);
    }
}