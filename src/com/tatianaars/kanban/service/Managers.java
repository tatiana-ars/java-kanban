package com.tatianaars.kanban.service;

import com.tatianaars.kanban.http.HttpTaskServer;
import com.tatianaars.kanban.service.HistoryManager;
import com.tatianaars.kanban.service.InMemoryHistoryManager;
import com.tatianaars.kanban.service.InMemoryTaskManager;
import com.tatianaars.kanban.service.TaskManager;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskServer getDefaultTaskServer() {
        return new HttpTaskServer(getDefaultTaskManager());
    }
}
