package com.checkmate.sdk;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
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
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.checkmate.sdk.reservations.Reservation;
import com.checkmate.sdk.wrappers.ReservationWrapper;
import com.checkmate.sdk.wrappers.ResourceWrapper;

/**
 * The Checkmate REST client, used for creating and retrieving reservation
 * and property data in the Checkmate database.
 */
public class CheckmateClient {

  private static final String DEFAULT_ENDPOINT = "https://partners-staging.checkmate.io";

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
    ReservationWrapper wrapper = new ReservationWrapper(reservation, propertyId);
    HttpUriRequest request = createPostRequest(wrapper);
    return handleResponse(request);
  }

  /**
  * Creates a reservation in the Checkmate system using a property passed in
  * on the Reservation object.
  */
  public CheckmateResponse createReservation(final Reservation reservation) {
    ReservationWrapper wrapper = new ReservationWrapper(reservation);
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
    request.setHeaders(defaultHeaders());
    request.addHeader(new BasicHeader(CONTENT_HEADER_KEY, CONTENT_HEADER_VALUE));

    try {
      request.setEntity(new StringEntity(createHappyJson(resourceWrapper.getResource())));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The encoding provided is not supported", e);
    }
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
