package com.checkmate.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import java.util.Arrays;

import com.checkmate.sdk.CheckmateClient;
import com.checkmate.sdk.entities.Address;
import com.checkmate.sdk.entities.Property;
import com.checkmate.sdk.entities.Reservation;
import com.checkmate.sdk.entities.ReservationsOptions;

/**
 * Unit tests for the CheckmateClient.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckmateClientTest {
  private static final String API_KEY = "NOTAREALKEY";
  private static final String RESERVATION_ID = "skdjf";
  private static final String PROPERTY_ID = "93";
  private static final String CONFIRMATION_NUMBER = "werlskdfj";
  private static final String GET_METHOD = "GET";
  private static final String POST_METHOD = "POST";
  private static final String PATCH_METHOD = "PATCH";
  private static final String DELETE_METHOD = "DELETE";

  private static final String HOTEL_NAME = "Hotel Kabuki";
  private static final String PHONE = "12345678901";
  private static final String STREET_ADDRESS = "487 Bryant St";
  private static final String CITY = "San Francisco";
  private static final String REGION = "CA";
  private static final String POSTAL_CODE = "94105";
  private static final String COUNTRY_CODE = "US";
  private static final String WEBHOOK = "webhook";

  @Mock HttpClient httpClient;
  @Mock BasicHttpResponse httpResponse;
  @Mock BasicStatusLine statusLine;

  private CheckmateClient client;
  private Reservation reservation1;
  private Reservation reservation2;
  private Address address;

  @Before
  public void setupVariables() {
    client = new CheckmateClient(API_KEY);
    client.setHttpClient(httpClient);

    Reservation.Builder reservationBuilder = new Reservation.Builder()
        .setExternalId("externalId12")
        .setConfirmationNumber("2rfsdfsf2sddj433")
        .setLastName("Smith")
        .setEmail("John@smith.com")
        .setStartOn("09/14/2015")
        .setEndOn("09/16/2015")
        .setProperty(new Property.Builder()
            .setName(HOTEL_NAME)
            .setFullAddress("487 Bryant St, San Francisco, CA 94115, US")
            .build());
    reservation1 = reservationBuilder.build();
    reservation2 = reservationBuilder.setExternalId("externalId13").build();

    address = new Address.Builder()
        .setStreet(STREET_ADDRESS)
        .setCity(CITY)
        .setRegion(REGION)
        .setPostalCode(POSTAL_CODE)
        .setCountryCode(COUNTRY_CODE)
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
    CheckmateResponse response = client.createReservation(reservation1);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations"),
        actualRequest.getURI());
    assertEquals(POST_METHOD, actualRequest.getMethod());
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
    assertEquals(GET_METHOD, actualRequest.getMethod());
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
    assertEquals(GET_METHOD, actualRequest.getMethod());
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
    assertEquals("/reservations", actualRequest.getURI().getPath());
    assertEquals("confirmation_number=" + CONFIRMATION_NUMBER + "&exclude_properties=true",
        actualRequest.getURI().getQuery());
    assertEquals(GET_METHOD, actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  @Test
  public void getProperty() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.getProperty(HOTEL_NAME, PHONE, address);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();

    assertEquals("/properties", actualRequest.getURI().getPath());
    assertNotNull(actualRequest.getURI().getQuery());
    assertEquals(GET_METHOD, actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  @Test
  public void updateReservation() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.updateReservation(RESERVATION_ID, reservation1);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations/" + RESERVATION_ID),
        actualRequest.getURI());
    assertEquals(PATCH_METHOD, actualRequest.getMethod());
    assertHeaders(actualRequest);
    assertEquals(CheckmateClient.CONTENT_HEADER_VALUE,
        actualRequest.getFirstHeader(CheckmateClient.CONTENT_HEADER_KEY).getValue());
  }

  @Test
  public void deleteReservation() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.deleteReservation(RESERVATION_ID);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations/" + RESERVATION_ID),
        actualRequest.getURI());
    assertEquals(DELETE_METHOD, actualRequest.getMethod());
    assertHeaders(actualRequest);
  }

  @Test
  public void createBulkReservationsNoWebhook() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.bulkCreateReservations(
        Arrays.asList(reservation1, reservation2), "" /* no webhook */);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations/bulk_create"),
        actualRequest.getURI());
    assertEquals(POST_METHOD, actualRequest.getMethod());
    assertHeaders(actualRequest);
    assertEquals(CheckmateClient.CONTENT_HEADER_VALUE,
        actualRequest.getFirstHeader(CheckmateClient.CONTENT_HEADER_KEY).getValue());
  }

  @Test
  public void createBulkReservationsWithWebhook() throws Exception {
    ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    CheckmateResponse response = client.bulkCreateReservations(
        Arrays.asList(reservation1, reservation2), WEBHOOK);

    verify(httpClient).execute(request.capture());

    HttpUriRequest actualRequest = request.getValue();
    assertEquals(new URI(CheckmateClient.DEFAULT_ENDPOINT + "/reservations/bulk_create" +
        "?webhook=" + WEBHOOK), actualRequest.getURI());
  }

  private void assertHeaders(HttpUriRequest request) {
    assertEquals(CheckmateClient.ACCEPT_HEADER_VALUE,
        request.getFirstHeader(CheckmateClient.ACCEPT_HEADER_KEY).getValue());
    assertEquals(API_KEY,
        request.getFirstHeader(CheckmateClient.API_TOKEN_HEADER_KEY).getValue());
  }
}
