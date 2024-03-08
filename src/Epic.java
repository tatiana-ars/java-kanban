import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIdInEpic;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIdInEpic = new ArrayList<>();
        setStatus(Status.NEW);
    }

    public ArrayList<Integer> getSubtasksIdInEpic() {
        return subtasksIdInEpic;
    }

    public void setSubtasksIdInEpic(ArrayList<Integer> subtasksIdInEpic) {
        this.subtasksIdInEpic = subtasksIdInEpic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIdInEpic=" + subtasksIdInEpic +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
