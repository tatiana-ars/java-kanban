import java.util.ArrayList;
import java.time.LocalDateTime;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIdInEpic = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description);
        setId(id);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtasksIdInEpic() {
        return subtasksIdInEpic;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        String subtasksId = String.valueOf(subtasksIdInEpic);
        return "Epic{subtasksIdInEpic=" + subtasksId + ", id=" + getId() + ", name='" + getName() + "', description='" + getDescription() + "', status=" + String.valueOf(getStatus()) + "}";
    }
}