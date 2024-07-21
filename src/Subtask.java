public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(Status.NEW);
    }

    public Subtask(String name, String description, int epicId, int id, Status status) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(status);
        this.setId(id);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{epicId=" + epicId + ", name='" + getName() + "', description='" + getDescription() + "', id=" + getId() + ", status=" + String.valueOf(getStatus()) + "}";
    }
}

