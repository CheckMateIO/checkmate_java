package com.checkmate.sdk.wrappers;

import org.apache.http.NameValuePair;
import java.util.List;
import com.checkmate.sdk.entities.Reservation;

/**
* Contains a Reservation object and any other parameters that can be associated
* with a request to update a reservation.
*/
public class UpdateReservationWrapper implements ResourceWrapper {
  private Reservation reservation;
  private String reservationId;

  public UpdateReservationWrapper(String reservationId, Reservation reservation) {
    this.reservationId = reservationId;
    this.reservation = reservation;
  }

  public Object getResource() {
    return reservation;
  }

  public String getPath() {
    return String.format("/reservations/%s", reservationId);
  }

  // No query parameters for this call
  public List<NameValuePair> toQueryParams() {
    return null;
  }
}
