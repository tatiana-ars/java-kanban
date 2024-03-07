
public class Subtask extends Epic{
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        status = Status.NEW;
    }

    public Subtask(String name, String description, int epicId, int id, Status status) {
        super(name, description);
        this.epicId = epicId;
        this.status = status;
        this.id = id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
