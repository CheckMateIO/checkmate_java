package com.checkmate.sdk.wrappers;

import org.apache.http.NameValuePair;
import java.util.List;

/**
* A wrapper for a request to show a reservation.
*/
public class ShowReservationWrapper implements ResourceWrapper {
  private String reservationId;

  public ShowReservationWrapper(String reservationId) {
    this.reservationId = reservationId;
  }

  public String getPath() {
    return String.format("/reservations/%s", reservationId);
  }

  // No resource here
  public Object getResource() {
    return null;
  }

  // No query parameters for this call
  public List<NameValuePair> toQueryParams() {
    return null;
  }
}
