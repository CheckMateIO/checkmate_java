package com.checkmate.sdk;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.checkmate.sdk.entities.Address;
import com.checkmate.sdk.entities.Reservation;
import com.checkmate.sdk.entities.ReservationsOptions;
import com.checkmate.sdk.wrappers.BulkReservationWrapper;
import com.checkmate.sdk.wrappers.CreateReservationWrapper;
import com.checkmate.sdk.wrappers.DeleteReservationWrapper;
import com.checkmate.sdk.wrappers.ListReservationsWrapper;
import com.checkmate.sdk.wrappers.PropertyWrapper;
import com.checkmate.sdk.wrappers.ResourceWrapper;
import com.checkmate.sdk.wrappers.ShowReservationWrapper;
import com.checkmate.sdk.wrappers.UpdateReservationWrapper;


/**
 * The Checkmate REST client, used for creating and retrieving reservation
 * and property data in the Checkmate database.
 */
public class CheckmateClient {

  protected static final String DEFAULT_ENDPOINT = "https://partners-staging.checkmate.io";

  // Keys and values for headers
  protected static final String CONTENT_HEADER_KEY = "Content-Type";
  protected static final String CONTENT_HEADER_VALUE = "application/json";
  protected static final String ACCEPT_HEADER_KEY = "Accept";
  protected static final String ACCEPT_HEADER_VALUE = "application/json";
  protected static final String API_TOKEN_HEADER_KEY = "X-CheckMate-API-Token";

  private String apiEndpoint;
  private String apiKey;
  private HttpClient httpClient;
  private Gson gson;

  // This constructor connects to the default endpoint.
  public CheckmateClient(final String apiKey) {
    this(apiKey, DEFAULT_ENDPOINT);
  }

  // Use this constructor if you'd like to provide a custom endpoint.
  public CheckmateClient(final String apiKey, final String apiEndpoint) {
    this.apiKey = apiKey;
    this.apiEndpoint = apiEndpoint;

    // create HTTP client & Gson helper
    this.httpClient = new DefaultHttpClient();
    this.gson = createGson();
  }

  /**
   * Creates a reservation in the Checkmate system using an existing property.
   */
  public CheckmateResponse createReservation(final Reservation reservation,
      String propertyId) {
    CreateReservationWrapper wrapper = new CreateReservationWrapper(reservation, propertyId);
    HttpUriRequest request = createPostRequest(wrapper);
    return handleResponse(request);
  }

  /**
  * Creates a reservation in the Checkmate system using a property passed in
  * on the Reservation object.
  */
  public CheckmateResponse createReservation(final Reservation reservation) {
    CreateReservationWrapper wrapper = new CreateReservationWrapper(reservation);
    HttpUriRequest request = createPostRequest(wrapper);
    return handleResponse(request);
  }

  /**
   * Fetches the reservation that corresponds to the reservation id.
   */
  public CheckmateResponse showReservation(final String reservationId) {
    ShowReservationWrapper wrapper = new ShowReservationWrapper(reservationId);
    HttpUriRequest request = createGetRequest(wrapper);
    return handleResponse(request);
  }

  /**
   * Updates the reservation with the fields set on the reservation object.
   */
  public CheckmateResponse updateReservation(final String reservationId,
      final Reservation reservation) {
    UpdateReservationWrapper wrapper = new UpdateReservationWrapper(reservationId, reservation);
    HttpUriRequest request = createPatchRequest(wrapper);
    return handleResponse(request);
  }

  /**
   * Deletes a reservation.
   */
   public CheckmateResponse deleteReservation(final String reservationId) {
     DeleteReservationWrapper wrapper = new DeleteReservationWrapper(reservationId);
     HttpUriRequest request = createDeleteRequest(wrapper);
     return handleResponse(request);
   }

  /**
   * Fetches a list of reservations.
   */
   public CheckmateResponse listReservations(final ReservationsOptions options) {
     ListReservationsWrapper wrapper = new ListReservationsWrapper(options);
     HttpUriRequest request = createGetRequest(wrapper);
     return handleResponse(request);
   }

  /**
   * Fetches a property based on the property name, phone number, and address.
   */
   public CheckmateResponse getProperty(final String name, final String phone,
      final Address address) {
     PropertyWrapper wrapper = new PropertyWrapper(name, phone, address);
     HttpUriRequest request = createGetRequest(wrapper);
     return handleResponse(request);
   }

  /**
   * Submits a bulk request to create reservations.
   */
   public CheckmateResponse bulkCreateReservations(final Collection<Reservation> reservations) {
     BulkReservationWrapper wrapper = new BulkReservationWrapper(reservations);
     HttpUriRequest request = createPostRequest(wrapper);
     return handleResponse(request);
   }

   /**
   * Submits a bulk request to create reservations. Checkmate will post the response
   * to the webhook url provided.
   */
   public CheckmateResponse bulkCreateReservations(final Collection<Reservation> reservations,
   final String webhook) {
     BulkReservationWrapper wrapper = new BulkReservationWrapper(reservations, webhook);
     HttpUriRequest request = createPostRequest(wrapper);
     return handleResponse(request);
   }

  // ------- private methods ----------------

  /**
   * Creates a post request, with the resource set as the body.
   */
  private HttpUriRequest createPostRequest(final ResourceWrapper resourceWrapper) {
    URI uri = buildUri(resourceWrapper.getPath(), resourceWrapper.toQueryParams());

    HttpPost request = new HttpPost(uri);
    setHeadersAndEntities(request, resourceWrapper.getResource());
    return request;
  }

  /**
  * Creates a patch request, with the resource set as the body.
  */
  private HttpUriRequest createPatchRequest(final ResourceWrapper resourceWrapper) {
    URI uri = buildUri(resourceWrapper.getPath(), resourceWrapper.toQueryParams());

    HttpPatch request = new HttpPatch(uri);
    setHeadersAndEntities(request, resourceWrapper.getResource());
    return request;
  }

  /**
   * Creates a get request, using the path and query parameters provided by the wrapper.
   */
  private HttpUriRequest createGetRequest(final ResourceWrapper resourceWrapper) {
    URI uri = buildUri(resourceWrapper.getPath(), resourceWrapper.toQueryParams());

    HttpGet request = new HttpGet(uri);
    request.setHeaders(defaultHeaders());
    return request;
  }

  /**
  * Creates a delete request, using the path and query parameters provided by the wrapper.
  */
  private HttpUriRequest createDeleteRequest(final ResourceWrapper resourceWrapper) {
    URI uri = buildUri(resourceWrapper.getPath(), resourceWrapper.toQueryParams());

    HttpDelete request = new HttpDelete(uri);
    request.setHeaders(defaultHeaders());
    return request;
  }

  /**
   * Executes the request and returns the response.
   */
  private CheckmateResponse handleResponse(HttpUriRequest request) {
    try {
      HttpResponse response = httpClient.execute(request);

      HttpEntity entity = response.getEntity();
      String responseBody = entity != null ? EntityUtils.toString(entity) : "";
      int statusCode = response.getStatusLine().getStatusCode();

      return new CheckmateResponse(statusCode, responseBody);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets a header specifying a json body, and sets the entity on the request.
   */
  private void setHeadersAndEntities(HttpEntityEnclosingRequestBase request, Object entity) {
    request.setHeaders(defaultHeaders());
    request.addHeader(new BasicHeader(CONTENT_HEADER_KEY, CONTENT_HEADER_VALUE));

    try {
      request.setEntity(new StringEntity(createHappyJson(entity)));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The encoding provided is not supported", e);
    }
  }

  /**
   * The Checkmate API requires the name of the top-level POJO in the request,
   * but GSON doesn't provide that, so we tack it on here.
   */
  private String createHappyJson(Object resource) {
    JsonElement je = gson.toJsonTree(resource);
    JsonObject root = new JsonObject();
    root.add(resource.getClass().getSimpleName().toLowerCase(), je);

    return root.toString();
  }

  /**
   * Creates a new JSON converter, transforming all the Java camelCase
   * field names to snake_case.
   */
  private Gson createGson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  /**
   * Returns the default headers sent along with every request.
   */
  private Header[] defaultHeaders() {
    return new Header[] {
        new BasicHeader(ACCEPT_HEADER_KEY, ACCEPT_HEADER_VALUE),
        new BasicHeader(API_TOKEN_HEADER_KEY, apiKey)};
  }

  /**
   * Builds a URI by appending the query parameters onto the end of the path
   */
  private URI buildUri(final String path, final List<NameValuePair> queryStringParams) {
    StringBuilder sb = new StringBuilder();
    sb.append(apiEndpoint);
    sb.append(path);

    if (queryStringParams != null && queryStringParams.size() > 0) {
      sb.append("?");
      sb.append(URLEncodedUtils.format(queryStringParams, "UTF-8"));
    }

    try {
      return new URI(sb.toString());
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Invalid uri", e);
    }
  }

  // --------- Helper methods for testing -------------

  public void setHttpClient(HttpClient client) {
    this.httpClient = client;
  }
}
