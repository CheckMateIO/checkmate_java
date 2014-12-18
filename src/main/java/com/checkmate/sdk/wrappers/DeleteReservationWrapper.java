package com.checkmate.sdk.wrappers;

import org.apache.http.NameValuePair;
import java.util.List;
import com.checkmate.sdk.entities.Reservation;

/**
* Wraps a reservation id that corresponds to a reservation to delete.
*/
public class DeleteReservationWrapper implements ResourceWrapper {
  private String reservationId;

  public DeleteReservationWrapper(String reservationId) {
    this.reservationId = reservationId;
  }

  // No resource here
  public Object getResource() {
    return null;
  }

  public String getPath() {
    return String.format("/reservations/%s", reservationId);
  }

  // No query parameters for this call
  public List<NameValuePair> toQueryParams() {
    return null;
  }
}
