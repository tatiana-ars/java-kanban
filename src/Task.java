public class Task {
    public String name;
    public String description;
    public int id;
    public Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }


}
