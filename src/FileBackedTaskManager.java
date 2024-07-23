import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static String HEADER = "id,type,name,status,description,startTime,endTime,duration,epic\n";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



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
            writer.write(HEADER);
            for (Task task : getTasks()) {
                if (task.getStartTime() == null) {
                    break;
                }
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                if (epic.getStartTime() == null) {
                    break;
                }
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                if (subtask.getStartTime() == null) {
                    break;
                }
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл" + e.getMessage());
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, Managers.getDefaultHistoryManager());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String taskLine;
            reader.readLine();
            while ((taskLine = reader.readLine()) != null) {
                Task task = fromString(taskLine);
                manager.addTask(task);
            }
            manager.setId(manager.findMaxId() + 1);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла" + e.getMessage());
        }
        return manager;
    }

    private void addTask(Task task) {
        if (task != null) {
            if (task.getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) task;
                super.putSubtask(subtask);
            } else if (task.getType() == TaskType.EPIC) {
                Epic epic = (Epic) task;
                super.putEpic(epic);
            } else {
                super.putTask(task);
            }
        }
    }

    private int findMaxId() {
        int maxId = 1;
        for (Task task : getTasks()) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        for (Epic epic : getEpics()) {
            if (epic.getId() > maxId) {
                maxId = epic.getId();
            }
        }
        for (Subtask subtask : getSubtasks()) {
            if (subtask.getId() > maxId) {
                maxId = subtask.getId();
            }
        }
        return maxId;
    }

    private String toString(Task task) {
        try {
            if (task.getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) task;
                return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%d",
                        task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(),
                        task.getStartTime().format(formatter), task.getEndTime().format(formatter),
                        task.getDuration().toMinutes(), subtask.getEpicId());
            } else if (task.getType() == TaskType.EPIC) {
                return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                        task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(),
                        task.getStartTime().format(formatter), task.getEndTime().format(formatter),
                        task.getDuration().toMinutes());
            } else {
                return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                        task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(),
                        task.getStartTime().format(formatter), task.getEndTime().format(formatter),
                        task.getDuration().toMinutes());
            }
        } catch (NullPointerException e) {
            System.out.println("Ошибка в записи задачи в файл (не установлено время):" + e.getMessage());
            return "";
        }

    }

    private static Task fromString(String str) {
        String[] fields = str.split(",");
        int id = Integer.parseInt(fields[0]);
        String typeTask = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        LocalDateTime startTime = LocalDateTime.parse(fields[5], formatter);
        LocalDateTime endTime = LocalDateTime.parse(fields[6], formatter);
        Duration duration = Duration.ofMinutes(Integer.parseInt(fields[7]));

        final TaskType type = TaskType.valueOf(typeTask);
        if (type == TaskType.TASK) {
            return new Task(name, description, status, id, startTime, duration);
        } else if (type == TaskType.EPIC) {
            return new Epic(name, description, id);
        } else {
            int epicId = Integer.parseInt(fields[8]);
            return new Subtask(name, description, epicId, id, status, startTime, duration);
        }
    }

}
