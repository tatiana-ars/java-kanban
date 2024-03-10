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
        return this.epicId;
    }

    @Override
    public String toString() {
        int var10000 = this.epicId;
        return "Subtask{epicId=" + var10000 + ", name='" + this.getName() + "', description='" + this.getDescription() + "', id=" + this.getId() + ", status=" + String.valueOf(this.getStatus()) + "}";
    }
}

