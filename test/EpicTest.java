import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testEqualsEpicsShouldBeEqualsIfIdEquals() {

        Epic epic = new Epic("a", "b", 4);
        Epic epic1 = new Epic("c", "d", 4);

        Assertions.assertTrue(epic.equals(epic1));

    }
}