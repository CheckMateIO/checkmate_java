package com.checkmate.sdk.examples;

import com.checkmate.sdk.CheckmateClient;
import com.checkmate.sdk.CheckmateResponse;
import com.checkmate.sdk.entities.Address;
import com.checkmate.sdk.entities.Property;
import com.checkmate.sdk.entities.Reservation;
import com.checkmate.sdk.entities.ReservationsOptions;

/**
* Contains examples of how to use the CheckmateClient.
*/
public class RestExample {

  private static final String API_KEY = "NOTAREALKEY";

  public static void main(String[] args) throws Exception {

    // Create a rest client
    CheckmateClient client = new CheckmateClient(API_KEY);

    createReservation(client);
    showReservation(client);
    listReservations(client);
    updateReservation(client);
    deleteReservation(client);
    getProperty(client);
  }

  private static void createReservation(CheckmateClient client) {
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

  private static void showReservation(CheckmateClient client) {
    CheckmateResponse response = client.showReservation("123345slfdkj");
    System.out.println(response.getBodyAsString());
  }

  private static void listReservations(CheckmateClient client) {
    // Lists reservations
    ReservationsOptions options = new ReservationsOptions.Builder()
        .setExcludeProperties(true)
        .setConfirmationNumber("234908sdf")
        .build();

    CheckmateResponse response = client.listReservations(options);
    System.out.println(response.getBodyAsString());
  }

  private static void updateReservation(CheckmateClient client) {
    // Updates a reservation
    Reservation updateRes = new Reservation.Builder()
        .setEmail("Frank@smith.com")
        .build();
    CheckmateResponse response = client.updateReservation("2394709", updateRes);
    System.out.println(response.getBodyAsString());
  }

  private static void deleteReservation(CheckmateClient client) {
    // Deletes a reservation
    CheckmateResponse response = client.deleteReservation("2384089");
    System.out.println(response.getBodyAsString());
  }

  private static void getProperty(CheckmateClient client) {
    // Fetches a property
    Address propertyAddress = new Address.Builder()
        .setStreet("123 Happy Ln")
        .setCity("San Antonio")
        .setRegion("TX")
        .setPostalCode("34567")
        .setCountryCode("US")
        .build();
    CheckmateResponse response = client.getProperty("Hotel Sun", "12345674567", propertyAddress);
    System.out.println(response.getBodyAsString());
  }
}
