import java.util.ArrayList;

public class Subtask extends Epic{
    private int epicId;

    public Subtask(String name, String description, int id, ArrayList<Integer> subtasksId, int epicId) {
        super(name, description, id, subtasksId);
        this.epicId = epicId;
    }
}
