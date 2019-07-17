import static org.junit.Assert.assertEquals;
import org.junit.*;


public class TestReservation {
    /**
     * Tests that the campsiteId, start date, and end date values are parsed and reformatted
     * properly when a Reservation object is instantiated.
     */
    @Test
    public void testGetters() {
        Reservation testReservation = new Reservation(1, "2018-06-01", "2018-06-03");
        int expectedCampsiteId = 1;
        String expectedStartDate = "06/01/2018";
        String expectedEndDate = "06/03/2018";

        assertEquals(expectedCampsiteId, testReservation.getCampsiteId());
        assertEquals(expectedStartDate, testReservation.getStartDate());
        assertEquals(expectedEndDate, testReservation.getEndDate());
    }
}
