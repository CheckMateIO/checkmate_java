package com.checkmate.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Represents a response from the Checkmate API.
 */
public class CheckmateResponse {
  private String responseBody;
  private int statusCode;

  public CheckmateResponse(int statusCode, String responseBody) {
    this.statusCode = statusCode;
    this.responseBody = responseBody;
  }

  public String getBodyAsString() {
    return responseBody;
  }

  public JsonElement getBodyAsJson() {
    return new JsonParser().parse(responseBody);
  }

  public int getStatusCode() {
    return statusCode;
  }
}
