package com.checkmate.sdk.wrappers;

import com.checkmate.sdk.entities.Reservation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Wraps a list of reservations for sending to a bulk create call.
*/
public class BulkReservationWrapper implements ResourceWrapper {
  private Reservations reservations;
  private String webhook;

  public BulkReservationWrapper(Collection<Reservation> reservations,
      String webhook) {
    this.reservations = new Reservations(reservations);
    this.webhook = webhook;
  }

  public BulkReservationWrapper(Collection<Reservation> reservations) {
    this.reservations = new Reservations(reservations);
  }

  public Object getResource() {
    return reservations;
  }

  public String getPath() {
    return "/reservations/bulk_create";
  }

  // No query parameters for this call
  public List<NameValuePair> toQueryParams() {
    if (webhook != null) {
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("webhook", webhook));
      return params;
    }
    return null;
  }

  // This class exists to create the correct JSON.
  private static class Reservations {
    private Collection<Reservation> reservations;

    public Reservations(Collection<Reservation> reservations) {
      this.reservations = reservations;
    }
  }
}
