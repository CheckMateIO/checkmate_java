package com.checkmate.sdk.wrappers;

import org.apache.http.NameValuePair;
import java.util.List;

/**
 * Defines a common interface for HTTP request creation for various types
 * of requests to the Checkmate API.
 */
public interface ResourceWrapper {
  String getPath();
  List<NameValuePair> toQueryParams();
  Object getResource();
}
