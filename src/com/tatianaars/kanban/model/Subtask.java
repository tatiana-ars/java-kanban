package com.tatianaars.kanban.model;

import com.tatianaars.kanban.model.Task;
import com.tatianaars.kanban.util.Status;
import com.tatianaars.kanban.util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(Status.NEW);
    }

    public Subtask(String name, String description, int epicId, int id, Status status) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(status);
        this.setId(id);
    }

    public Subtask(String name, String description, int epicId, int id, Status status, LocalDateTime startTime,
                   Duration duration) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(status);
        this.setId(id);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    public Subtask(String name, String description, int epicId, Status status, LocalDateTime startTime,
                   Duration duration) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(status);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "com.tatianaars.kanban.model.Subtask{epicId=" + epicId + ", name='" + getName() + "', description='" + getDescription()
                + "', id=" + getId() + ", status=" + String.valueOf(getStatus()) + "', startTime ="
                + getStartTime() + "', duration='" + getDuration() + "} \n";
    }
}

