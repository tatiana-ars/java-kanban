package com.tatianaars.kanban.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tatianaars.kanban.exception.NotFoundException;
import com.tatianaars.kanban.model.Epic;
import com.tatianaars.kanban.model.Subtask;
import com.tatianaars.kanban.model.Task;

import java.io.*;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.tatianaars.kanban.service.TaskManager;
import com.tatianaars.kanban.util.*;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String TASKS_PATH = "/tasks";
    private static final String SUBTASKS_PATH = "/subtasks";
    private static final String EPICS_PATH = "/epics";
    private static final String HISTORY_PATH = "/history";
    private static final String PRIORITIZED_PATH = "/prioritized";
    private static HttpServer server;
    private static TaskManager taskManager;

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        start();
    }

    public static Gson getGson() {
        return gson;
    }

    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(TASKS_PATH, new TaskHandler());
        server.createContext(SUBTASKS_PATH, new SubtaskHandler());
        server.createContext(EPICS_PATH, new EpicHandler());
        server.createContext(HISTORY_PATH, new HistoryHandler());
        server.createContext(PRIORITIZED_PATH, new PrioritizedHandler());

        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер на " + PORT + " порту остановлен!");
    }

    private static class TaskHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String[] request = path.split("/");

                switch (HTTPMethod.valueOf(method)) {
                    case GET:
                        if (request.length == 3 && request[1].equals("tasks")) {
                            int id = Integer.parseInt(request[2]);
                            Task task = taskManager.getTaskById(id);
                            sendText(exchange, gson.toJson(task), 200);
                            break;
                        } else {
                            List<Task> tasks = taskManager.getTasks();
                            sendText(exchange, gson.toJson(tasks), 200);
                            break;
                        }
                    case POST:
                        Task newTask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(),
                                StandardCharsets.UTF_8), Task.class);
                        if (newTask.getId() == 0) {
                            taskManager.createTask(newTask);
                            sendText(exchange, "", 201);
                            break;
                        } else {
                            taskManager.updateTask(newTask);
                            sendText(exchange, "", 201);
                            break;
                        }
                    case DELETE:
                        int id = Integer.parseInt(request[2]);
                        taskManager.deleteTaskById(id);
                        sendText(exchange, "", 200);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange);
            }
        }
    }

    private static class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String[] request = path.split("/");

                switch (HTTPMethod.valueOf(method)) {
                    case GET:
                        if (request.length == 3 && request[1].equals("subtasks")) {
                            int id = Integer.parseInt(request[2]);
                            Subtask subtask = (Subtask) taskManager.getSubtaskById(id);
                            sendText(exchange, gson.toJson(subtask), 200);
                            break;
                        } else {
                            List<Subtask> subtasks = taskManager.getSubtasks();
                            sendText(exchange, gson.toJson(subtasks), 200);
                            break;
                        }
                    case POST:
                        Subtask newSubtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(),
                                StandardCharsets.UTF_8), Subtask.class);
                        if (newSubtask.getId() == 0) {
                            taskManager.createSubtask(newSubtask);
                            sendText(exchange, "", 201);
                            break;
                        } else {
                            taskManager.updateSubtask(newSubtask);
                            sendText(exchange, "", 201);
                            break;
                        }
                    case DELETE:
                        int id = Integer.parseInt(request[2]);
                        taskManager.deleteSubtaskById(id);
                        sendText(exchange, "", 200);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange);
            }
        }
    }

    private static class EpicHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String[] request = path.split("/");

                switch (HTTPMethod.valueOf(method)) {
                    case GET:
                        if (request.length == 3 && request[1].equals("epics")) {
                            int id = Integer.parseInt(request[2]);
                            Epic epic = (Epic) taskManager.getEpicById(id);
                            sendText(exchange, gson.toJson(epic), 200);
                            break;
                        } else {
                            List<Epic> epics = taskManager.getEpics();
                            sendText(exchange, gson.toJson(epics), 200);
                            break;
                        }
                    case POST:
                        Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(),
                                StandardCharsets.UTF_8), Epic.class);
                        System.out.println(epic);
                        if (epic.getId() == 0) {
                            taskManager.createEpic(epic);
                            sendText(exchange, "", 201);
                            break;
                        } else {
                            taskManager.updateEpic(epic);
                            sendText(exchange, "", 201);
                            break;
                        }
                    case DELETE:
                        int id = Integer.parseInt(request[2]);
                        taskManager.deleteEpicById(id);
                        sendText(exchange, "", 200);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange);
            }
        }
    }

    private static class HistoryHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();

                switch (HTTPMethod.valueOf(method)) {
                    case GET:
                        List<Task> history = taskManager.getHistory();
                        sendText(exchange, gson.toJson(history), 200);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange);
            }
        }
    }

    private static class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();

                switch (HTTPMethod.valueOf(method)) {
                    case GET:
                        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                        sendText(exchange, gson.toJson(prioritizedTasks), 200);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "{\"error\":\"Invalid JSON\"}", 400);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange);
            }
        }
    }

}
