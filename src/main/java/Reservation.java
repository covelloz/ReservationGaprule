import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Reservation {
    private int campsiteId;
    private Date startDate;
    private Date endDate;

    public Reservation(int campsiteId, String startDate, String endDate) {
        this.campsiteId = campsiteId;
        try {
            this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getCampsiteId() {
        return campsiteId;
    }

    public String getStartDate() {
        return new SimpleDateFormat("MM/dd/YYYY").format(startDate);
    }

    public String getEndDate() {
        return new SimpleDateFormat("MM/dd/YYYY").format(endDate);
    }
}
