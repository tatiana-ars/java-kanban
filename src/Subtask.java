import java.util.ArrayList;

public class Subtask extends Epic{
    private int epicId;

    public Subtask(String name, String description, int id, ArrayList<Integer> subtasks, String epic) {
        super(name, description, id, subtasks);
        epicId = super.id;
    }
}
