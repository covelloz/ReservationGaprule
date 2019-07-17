import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Search {
    private Date startDate;
    private Date endDate;

    public Search(String startDate, String endDate) {
        try {
            this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStartDate() {
        return new SimpleDateFormat("MM/dd/YYYY").format(startDate);
    }

    public String getEndDate() {
        return new SimpleDateFormat("MM/dd/YYYY").format(endDate);
    }

    public void setStartDate(String newStartDate) {
        try {
            this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(newStartDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setEndDate (String newEndDate) {
        try {
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(newEndDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensures that the Search's date window will not
     * result in a reservation that makes a vacant gap within size of gapRule
     * @param reservations (List<Reservation>) - list of existing reservations
     * @param campsites (List<Campsite>) - list of existing Campsites
     * @return (List<Campsite>) - a list of available campsites that do not violate gapRule
     */
    public List<Campsite> getAvailableCampsites(
            List<Reservation> reservations,
            List<Campsite> campsites)
    {
        List<Integer> unavailableCampsiteIds = new ArrayList<>();
        List<Campsite> availableCampsites = new ArrayList<>();
        List<Reservation> nearestReservations = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        // Unavailable due to overlap with Search's date window
        for (Reservation reservation : reservations) {
            try {
                Date checkEndDate = sdf.parse(reservation.getEndDate());
                Date checkStartDate = sdf.parse(reservation.getStartDate());

                long diffTimeLeft = this.startDate.getTime() - checkEndDate.getTime();
                long diffDaysLeft = TimeUnit.DAYS.convert(diffTimeLeft, TimeUnit.MILLISECONDS);

                long diffTimeRight = checkStartDate.getTime() - this.endDate.getTime();
                long diffDaysRight = TimeUnit.DAYS.convert(diffTimeRight, TimeUnit.MILLISECONDS);

                if (diffDaysLeft <= 0 && diffDaysRight <= 0) {
                    unavailableCampsiteIds.add(reservation.getCampsiteId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Check gapRule for each nearest reservation
        for (Campsite campsite : campsites) {
            List<Reservation> campsiteReservations = filterReservationsForCampsite(campsite, reservations);
            if (!campsiteReservations.isEmpty()) {
                if(closestLeftBoundReservation(campsiteReservations) != null) {
                    nearestReservations.add(closestLeftBoundReservation(campsiteReservations));
                }
                if(closestRightBoundReservation(campsiteReservations) != null) {
                    nearestReservations.add(closestRightBoundReservation(campsiteReservations));
                }
            }
        }

        for (Reservation reservation : nearestReservations) {
            try {
                int gapRule = getCampsiteGapRule(reservation, campsites);
                Date checkEndDate = sdf.parse(reservation.getEndDate());
                Date checkStartDate = sdf.parse(reservation.getStartDate());

                long diffTimeLeft = this.startDate.getTime() - checkEndDate.getTime();
                long diffDaysLeft = TimeUnit.DAYS.convert(diffTimeLeft, TimeUnit.MILLISECONDS);

                long diffTimeRight = checkStartDate.getTime() - this.endDate.getTime();
                long diffDaysRight = TimeUnit.DAYS.convert(diffTimeRight, TimeUnit.MILLISECONDS);

                if (diffDaysLeft > 0 && diffDaysLeft - 1 == gapRule) {
                    unavailableCampsiteIds.add(reservation.getCampsiteId());
                }

                if (diffDaysRight > 0 && diffDaysRight - 1 == gapRule) {
                    unavailableCampsiteIds.add(reservation.getCampsiteId());
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Select available campsites
        for (Campsite campsite : campsites) {
            if (!unavailableCampsiteIds.contains(campsite.getCampsiteId())) {
                availableCampsites.add(campsite);
            }
        }
        return availableCampsites;
    }

    /**
     * Identifies the reservation closest to Search's left bound (startDate).
     * @param filteredReservations (List<Reservation>) - list of reservations filtered by campsiteId
     * @return (Reservation)
     */
    private Reservation closestLeftBoundReservation(List<Reservation> filteredReservations) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        long minDiffDaysLeft = Long.MAX_VALUE;
        Reservation minLeftBoundReservation = null; //filteredReservations.get(0);

        for (Reservation reservation : filteredReservations) {
            try {
                Date checkEndDate = sdf.parse(reservation.getEndDate());

                long diffTimeLeft = this.startDate.getTime() - checkEndDate.getTime();
                long diffDaysLeft = TimeUnit.DAYS.convert(diffTimeLeft, TimeUnit.MILLISECONDS);

                if (diffDaysLeft > 0 && diffDaysLeft <= minDiffDaysLeft) {
                    minDiffDaysLeft = diffDaysLeft;
                    minLeftBoundReservation = reservation;
                }
            }
            catch (ParseException e){
                e.printStackTrace();
            }
        }
        return minLeftBoundReservation;
    }

    /**
     * Identifies the reservation closest to Search's right bound (endDate).
     * @param filteredReservations (List<Reservation>) - list of reservations filtered by campsiteId
     * @return (Reservation)
     */
    private Reservation closestRightBoundReservation(List<Reservation> filteredReservations) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        long minDiffDaysRight = Long.MAX_VALUE;
        Reservation minRightBoundReservation = null;

        for (Reservation reservation : filteredReservations) {
            try {
                Date checkStartDate = sdf.parse(reservation.getStartDate());

                long diffTimeRight = checkStartDate.getTime() - this.endDate.getTime();
                long diffDaysRight = TimeUnit.DAYS.convert(diffTimeRight, TimeUnit.MILLISECONDS);

                if (diffDaysRight > 0 && diffDaysRight <= minDiffDaysRight) {
                    minDiffDaysRight = diffDaysRight;
                    minRightBoundReservation = reservation;
                }
            }
            catch (ParseException e){
                e.printStackTrace();
            }
        }
        return minRightBoundReservation;
    }

    /**
     * Filters a List of Reservation objects by a given campsite
     * @param campsite (Campsite)
     * @param campsite (List<Reservation><)
     * @return List(Reservation)
     */
    private List<Reservation> filterReservationsForCampsite(Campsite campsite, List<Reservation> reservations) {
        List<Reservation> filteredReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCampsiteId() == campsite.getCampsiteId()) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }

    /**
     * Gets the gapRule of a campsite of for a specific Reservation
     * @param reservation (Reservation)
     * @param campsites (List<Campsite>)
     * @return List(Reservation)
     */
    private int getCampsiteGapRule(Reservation reservation, List<Campsite> campsites) {
        int gapRule = 0;
        for(Campsite campsite : campsites) {
            if (campsite.getCampsiteId() == reservation.getCampsiteId()) {
                gapRule = campsite.getGapRule();
            }
        }
        return gapRule;
    }
}
