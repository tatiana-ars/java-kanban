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
//        Task taskCopy = new Task(task);
//        this.history.add(taskCopy);
//        В список List<Task> history клалась копия задачи для того,
//        чтобы при изменении объекта в истории сохранялась копия без изменений.

        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }

    }

    public List<Task> getHistory() {
        return new ArrayList(history);
    }
}