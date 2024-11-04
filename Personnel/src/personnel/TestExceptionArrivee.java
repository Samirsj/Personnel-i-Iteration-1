package personnel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestExceptionArrivee {

    @Test
    void testExceptionArrivee() {
        ExceptionArrivee e = new ExceptionArrivee();
        String message = "L'arrivée est avant le départ.";
        assertEquals(message, e.getMessage());
        assertThrows(ExceptionArrivee.class, () -> {
            throw e;
        });
    }
}