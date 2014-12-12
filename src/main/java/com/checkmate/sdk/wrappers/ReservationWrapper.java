package com.checkmate.sdk.wrappers;

import org.apache.http.NameValuePair;
import java.util.List;
import com.checkmate.sdk.reservations.Reservation;

/**
 * Contains a Reservation object and any other parameters that can be associated
 * with a request to create a reservation.
 */
public class ReservationWrapper implements ResourceWrapper {
  private Reservation reservation;
  private String propertyId;

  public ReservationWrapper(Reservation reservation) {
    this.reservation = reservation;
  }

  public ReservationWrapper(Reservation reservation, String propertyId) {
    this(reservation);
    this.propertyId = propertyId;
  }

  public Object getResource() {
    return reservation;
  }

  public String getPath() {
    if (propertyId == null) {
      return "/reservations";
    } else {
      return String.format("properties/%s/reservations", propertyId);
    }
  }

  // No query parameters for this call
  public List<NameValuePair> toQueryParams() {
    return null;
  }
}
