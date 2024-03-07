import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIdInEpic;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIdInEpic = new ArrayList<>();
        status = Status.NEW;
    }

    public ArrayList<Integer> getSubtasksIdInEpic() {
        return subtasksIdInEpic;
    }

    public void setSubtasksIdInEpic(ArrayList<Integer> subtasksIdInEpic) {
        this.subtasksIdInEpic = subtasksIdInEpic;
    }
}
