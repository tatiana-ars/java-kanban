public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task1 = new Task("Выпить чай", "А то остынет");
        Task task2 = new Task("Выспаться", "Полезно");
        Epic epic1 = new Epic("Закончить спринт", "пора");
        Epic epic2 = new Epic("Схлдить в магазин","Можно завтра");
        Subtask subtask1 = new Subtask("Сдать финальное задание", "надо!", 3);
        Subtask subtask2 = new Subtask("Купить молоко", "1%", 4);
        Subtask subtask3 = new Subtask("Купить хлеб", ")))", 4);

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);

        TaskManager taskManager = new TaskManager();

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        System.out.println(taskManager);


        Subtask updatedSubtask = new Subtask("Сдать финальное задание", "надо!", 3, 5,Status.DONE);
        taskManager.updateSubtask(updatedSubtask);
        Subtask updatedSubtask2 = new Subtask("Купить молоко", "1%", 4, 6, Status.IN_PROGRESS);
        taskManager.updateSubtask(updatedSubtask2);
        taskManager.deleteTasks();

        System.out.println("_____________________________________________");

        System.out.println(taskManager);

    }

}
