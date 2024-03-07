import java.util.Objects;

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

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
