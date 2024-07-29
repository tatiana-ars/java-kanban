import com.tatianaars.kanban.model.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    @Test
    void testEqualsEpicsShouldBeEqualsIfIdEquals() {

        Epic epic = new Epic("a", "b", 4);
        Epic epic1 = new Epic("c", "d", 4);

        Assertions.assertEquals(epic, epic1);

    }
}