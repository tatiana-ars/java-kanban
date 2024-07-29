import com.google.gson.Gson;
import com.tatianaars.kanban.http.HttpTaskServer;
import com.tatianaars.kanban.model.Epic;
import com.tatianaars.kanban.model.Subtask;
import com.tatianaars.kanban.service.Managers;
import com.tatianaars.kanban.service.TaskManager;
import com.tatianaars.kanban.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerSubtasksTest {
    TaskManager manager = Managers.getDefaultTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerSubtasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "1 epic");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", 1, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5));
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getSubtasks();

        Assertions.assertNotNull(tasksFromManager, "Подзадачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        Assertions.assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "1 epic");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", 1, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Subtask subtask2 = gson.fromJson(response.body(), Subtask.class);

        Assertions.assertEquals(subtask.getName(), subtask2.getName());
        Assertions.assertEquals(subtask.getDescription(), subtask2.getDescription());
    }

    @Test
    public void testDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "1 epic");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", 1, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE().header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getSubtasks();

        Assertions.assertEquals(0, subtasksFromManager.size());
    }

}