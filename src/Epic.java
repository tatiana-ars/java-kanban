import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIdInEpic = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description);
        setId(id);
    }

    public ArrayList<Integer> getSubtasksIdInEpic() {
        return subtasksIdInEpic;
    }

    @Override
    public String toString() {
        String subtasksId = String.valueOf(subtasksIdInEpic);
        return "Epic{subtasksIdInEpic=" + subtasksId + ", id=" + getId() + ", name='" + getName() + "', description='" + getDescription() + "', status=" + String.valueOf(getStatus()) + "}";
    }
}