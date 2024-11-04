package personnel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestExceptionDepart {

    @Test
    void testExceptionDepart() {
        ExceptionDepart e = new ExceptionDepart();
        String message = "Le départ est après l'arrivée.";
        assertEquals(message, e.getMessage());
        assertThrows(ExceptionDepart.class, () -> {
            throw e;
        });
    }
}