import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpTaskManagerTest {
    TaskManager manager = Managers.getDefaultTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTest() throws IOException {
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
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task("Test 3", "Testing task 3", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createTask(task);
        manager.createTask(task2);
        manager.getTaskById(1);
        manager.getTaskById(2);
        List<Task> assertionHistory = new ArrayList<>(Arrays.asList(task2, task));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> historyFromResponse = gson.fromJson(response.body(), taskListType);

        Assertions.assertEquals(assertionHistory, historyFromResponse);
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("1", "задача 1", Status.NEW, 1,
                LocalDateTime.of(2024, 7, 1, 7, 0, 0), Duration.ofMinutes(20));
        Task task2 = new Task("2", "задача 2", Status.NEW, 2,
                LocalDateTime.of(2024, 7, 1, 7, 0, 0), Duration.ofMinutes(60));
        Task task3 = new Task("3", "задача 3", Status.NEW, 3,
                LocalDateTime.of(2024, 7, 10, 7, 0, 0), Duration.ofMinutes(60));
        Task task4 = new Task("4", "задача 4", Status.NEW, 4,
                LocalDateTime.of(2024, 7, 9, 7, 0, 0), Duration.ofMinutes(60));
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);

        List<Task> assertList = new ArrayList<>(Arrays.asList(task1, task4, task3));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritizedFromResponse = gson.fromJson(response.body(), taskListType);

        Assertions.assertEquals(assertList, prioritizedFromResponse);
    }

}