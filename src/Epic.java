import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIdInEpic = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description);
        this.setId(id);
    }

    public ArrayList<Integer> getSubtasksIdInEpic() {
        return this.subtasksIdInEpic;
    }

    @Override
    public String toString() {
        String subtasksId = String.valueOf(this.subtasksIdInEpic);
        return "Epic{subtasksIdInEpic=" + subtasksId + ", id=" + this.getId() + ", name='" + this.getName() + "', description='" + this.getDescription() + "', status=" + String.valueOf(this.getStatus()) + "}";
    }
}