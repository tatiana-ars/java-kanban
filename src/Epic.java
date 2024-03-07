import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksId;

    public Epic(String name, String description, int id, ArrayList<Integer> subtasksId) {
        super(name, description, id);
        this.subtasksId = new ArrayList<>();
    }
}
