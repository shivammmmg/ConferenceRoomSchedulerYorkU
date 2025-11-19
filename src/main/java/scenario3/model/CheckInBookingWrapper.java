package scenario3.model;

import shared.model.Booking;

/**
 * A lightweight wrapper used in Scenario 3 for check-in / no-show logic.
 * It does NOT duplicate the Booking model.
 * It only adds UI / sensor specific flags.
 */
public class CheckInBookingWrapper {

    private final Booking booking;

    private boolean checkedIn = false;
    private boolean depositForfeited = false;
    private boolean forfeitPopupShown = false;

    public CheckInBookingWrapper(Booking booking) {
        this.booking = booking;
    }

    public Booking getBooking() { return booking; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    public boolean isDepositForfeited() { return depositForfeited; }
    public void forfeitDeposit() { this.depositForfeited = true; }

    public boolean isForfeitPopupShown() { return forfeitPopupShown; }
    public void setForfeitPopupShown(boolean shown) { this.forfeitPopupShown = shown; }
}
