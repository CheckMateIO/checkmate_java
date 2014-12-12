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

/**
 * Unit tests for the CheckmateClient.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckmateClientTest {
  private static final String API_KEY = "NOTAREALKEY";

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
    assertEquals(new URI("https://partners-staging.checkmate.io/reservations"),
        actualRequest.getURI());
    assertEquals("POST", actualRequest.getMethod());
    assertEquals(CheckmateClient.CONTENT_HEADER_VALUE,
        actualRequest.getFirstHeader(CheckmateClient.CONTENT_HEADER_KEY).getValue());
    assertEquals(CheckmateClient.ACCEPT_HEADER_VALUE,
        actualRequest.getFirstHeader(CheckmateClient.ACCEPT_HEADER_KEY).getValue());
    assertEquals(API_KEY,
        actualRequest.getFirstHeader(CheckmateClient.API_TOKEN_HEADER_KEY).getValue());
  }
}
