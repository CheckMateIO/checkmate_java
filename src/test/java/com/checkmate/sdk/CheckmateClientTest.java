package com.checkmate.sdk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.net.URI;

import com.checkmate.sdk.CheckmateClient;
import com.checkmate.sdk.reservations.Property;
import com.checkmate.sdk.reservations.Reservation;
import com.checkmate.sdk.reservations.ReservationsOptions;

/**
 * Unit tests for the CheckmateClient.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckmateClientTest {
  private static final String API_KEY = "NOTAREALKEY";
  private static final String RESERVATION_ID = "skdjf";
  private static final String PROPERTY_ID = "93";
  private static final String CONFIRMATION_NUMBER = "werlskdfj";

  @Mock HttpClient httpClient;
  @Mock BasicHttpResponse httpResponse;
  @Mock BasicStatusLine statusLine;

  private CheckmateClient client;
  private Reservation reservation;

  @Before
  public void setupVariables() {
    client = new CheckmateClient(API_KEY);
    client.setHttpClient(httpClient);

    reservation = new Reservation.Builder()
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
  }

  @Before
  public void setupMocks() {
    when(statusLine.getStatusCode()).thenReturn(200);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
  }

  @Test
  public void createReservation() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.createReservation(reservation);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations"),
        actualRequest.getURI());
    assertEquals("POST", actualRequest.getMethod());
    assertHeaders(actualRequest);
    assertEquals(CheckmateClient.CONTENT_HEADER_VALUE,
        actualRequest.getFirstHeader(CheckmateClient.CONTENT_HEADER_KEY).getValue());
  }

  @Test
  public void showReservation() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.showReservation(RESERVATION_ID);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations/" + RESERVATION_ID),
        actualRequest.getURI());
    assertEquals("GET", actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  @Test
  public void listReservationsPropertyId() throws Exception {
    ReservationsOptions options = new ReservationsOptions.Builder()
        .setPropertyId(PROPERTY_ID)
        .build();

    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.listReservations(options);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/properties/" + PROPERTY_ID + "/reservations"),
        actualRequest.getURI());
    assertEquals("GET", actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  @Test
  public void listReservationsConfirmationExcludeProperty() throws Exception {
    ReservationsOptions options = new ReservationsOptions.Builder()
        .setConfirmationNumber(CONFIRMATION_NUMBER)
        .setExcludeProperties(true)
        .build();

    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.listReservations(options);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations?confirmation_number=" +
        CONFIRMATION_NUMBER + "&exclude_properties=true"), actualRequest.getURI());
    assertEquals("GET", actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  private void assertHeaders(HttpUriRequest request) {
    assertEquals(CheckmateClient.ACCEPT_HEADER_VALUE,
        request.getFirstHeader(CheckmateClient.ACCEPT_HEADER_KEY).getValue());
    assertEquals(API_KEY,
        request.getFirstHeader(CheckmateClient.API_TOKEN_HEADER_KEY).getValue());
  }
}
