public class Task {
    public String name;
    public String description;
    public int id;
    public Status status;

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = Status.NEW;
    }


}
