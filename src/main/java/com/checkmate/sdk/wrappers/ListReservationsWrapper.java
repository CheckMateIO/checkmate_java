package com.checkmate.sdk.wrappers;

import com.checkmate.sdk.reservations.ReservationsOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

public class ListReservationsWrapper implements ResourceWrapper {
  private ReservationsOptions options;

  public ListReservationsWrapper(ReservationsOptions options) {
    this.options = options;
  }

  public String getPath() {
    String propertyId = options.getPropertyId();
    if (propertyId != null) {
      return String.format("/properties/%s/reservations", propertyId);
    } else {
      return "/reservations";
    }
  }

  // No resource here
  public Object getResource() {
    return null;
  }

  public List<NameValuePair> toQueryParams() {
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    String confirmationNumber = options.getConfirmationNumber();
    Boolean excludeProperties = options.getExcludeProperties();

    if (confirmationNumber != null) {
      params.add(new BasicNameValuePair("confirmation_number", confirmationNumber));
    }

    if (excludeProperties != null) {
      params.add(new BasicNameValuePair("exclude_properties", excludeProperties.toString()));
    }

    return params;
  }
}
