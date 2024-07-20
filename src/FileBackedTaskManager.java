import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл" + e.getMessage());
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, new InMemoryHistoryManager());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String taskLine;
            reader.readLine();
            while ((taskLine = reader.readLine()) != null) {
                Task task = fromString(taskLine);
                if (task != null) {
                    if (task instanceof Subtask) {
                        manager.createSubtask((Subtask) task);
                    } else if (task instanceof Epic) {
                        manager.createEpic((Epic) task);
                    } else {
                        manager.createTask(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла" + e.getMessage());
        }
        return manager;
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d",
                    task.getId(), "SUBTASK", task.getName(), task.getStatus(), task.getDescription(),
                    subtask.getEpicId());
        } else if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s,", task.getId(), "EPIC", task.getName(), task.getStatus(),
                    task.getDescription());
        } else {
            return String.format("%d,%s,%s,%s,%s,", task.getId(), "TASK", task.getName(), task.getStatus(),
                    task.getDescription());
        }
    }

    private static Task fromString(String str) {
        String[] fields = str.split(",");
        int id = Integer.parseInt(fields[0]);
        String typeTask = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        if (typeTask.equals("TASK")) {
            return new Task(name, description, status, id);
        } else if (typeTask.equals("EPIC")) {
            return new Epic(name, description, id);
        } else {
            int epicId = Integer.parseInt(fields[5]);
            return new Subtask(name, description, epicId, id, status);
        }
    }

}
