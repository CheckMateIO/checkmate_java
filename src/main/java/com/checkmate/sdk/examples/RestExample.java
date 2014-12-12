package com.checkmate.sdk.examples;

import com.checkmate.sdk.CheckmateClient;
import com.checkmate.sdk.CheckmateResponse;
import com.checkmate.sdk.reservations.Reservation;
import com.checkmate.sdk.reservations.Property;

/**
* Contains examples of how to use the CheckmateClient.
*/
public class RestExample {

  private static final String API_KEY = "NOTAREALKEY";

  public static void main(String[] args) throws Exception {

    // Create a rest client
    CheckmateClient client = new CheckmateClient(API_KEY);

    Reservation res = new Reservation.Builder()
        .setExternalId("externalId12")
        .setConfirmationNumber("2rfsdfsf2sddj433")
        .setLastName("Smith")
        .setEmail("John@smith.com")
        .setStartOn("09/14/2015")
        .setEndOn("09/16/2015")
        .setProperty(new Property.Builder()
            .setName("New Hotel")
            .setFullAddress("487 Bryant St, San Francisco, CA 94115, US")
            .build())
        .build();

    // Create a reservation
    CheckmateResponse response = client.createReservation(res);
    System.out.println(response.getBodyAsString());
  }
}
