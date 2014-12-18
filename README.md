checkmate-java
==============


# CheckmateClient

A Java wrapper for the CheckMate REST API.

## Installation

This wrapper uses Maven. You can get maven [here](http://maven.apache.org/download.html).

Use the following dependency in your project:

    <dependency>
      <groupId>com.checkmate.sdk</groupId>
      <artifactId>checkmate-java-sdk</artifactId>
      <version>1.0.0</version>
      <scope>compile</scope>
    </dependency>

If you want to compile it yourself, here's how:

    $ git clone git@github.com:CheckMateIO/checkmate_java
    $ cd checkmate_java
    $ mvn install       # Requires maven, download from http://maven.apache.org/download.html

## Examples

You will need your Checkmate API private key to use the client.

Below are some examples (also found in [RestExample.java](https://github.com/CheckMateIO/checkmate_java/blob/master/src/main/java/com/checkmate/sdk/examples/RestExample.java))

```java
package com.checkmate.sdk.examples;

import com.checkmate.sdk.CheckmateClient;
import com.checkmate.sdk.CheckmateResponse;
import com.checkmate.sdk.entities.Address;
import com.checkmate.sdk.entities.Property;
import com.checkmate.sdk.entities.Reservation;
import com.checkmate.sdk.entities.ReservationsOptions;

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

    // Retrieves a reservation
    response = client.showReservation("123345slfdkj");
    System.out.println(response.getBodyAsString());

    // Lists reservations
    ReservationsOptions options = new ReservationsOptions.Builder()
        .setExcludeProperties(true)
        .setConfirmationNumber("234908sdf")
        .build();

    response = client.listReservations(options);
    System.out.println(response.getBodyAsString());

    // Fetches a property
    Address propertyAddress = new Address.Builder()
        .setStreet("123 Happy Ln")
        .setCity("San Antonio")
        .setRegion("TX")
        .setPostalCode("34567")
        .setCountryCode("US")
        .build();
    response = client.getProperty("Hotel Sun", "12345674567", propertyAddress);
    System.out.println(response.getBodyAsString());
  }
}
```
