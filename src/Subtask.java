
public class Subtask extends Epic{
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        status = Status.NEW;
    }

    public int getEpicId() {
        return epicId;
    }
}
