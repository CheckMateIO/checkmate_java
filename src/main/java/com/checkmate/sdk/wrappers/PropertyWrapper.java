package com.checkmate.sdk.wrappers;

import com.checkmate.sdk.entities.Address;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

/**
* Wraps a set of parameters for looking up a property.
*/
public class PropertyWrapper implements ResourceWrapper {
  private String name;
  private String phone;
  private Address address;

  public PropertyWrapper(String name, String phone, Address address) {
    this.name = name;
    this.phone = phone;
    this.address = address;
  }

  // No resource here
  public Object getResource() {
    return null;
  }

  public String getPath() {
    return "/properties";
  }

  public List<NameValuePair> toQueryParams() {
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(newParam("property[name]", name));
    params.add(newParam("property[phone]", phone));
    params.add(newParam("property[address][street]", address.getStreet()));
    params.add(newParam("property[address][city]", address.getCity()));
    params.add(newParam("property[address][region]", address.getRegion()));
    params.add(newParam("property[address][postal_code]", address.getPostalCode()));
    params.add(newParam("property[address][country_code]", address.getCountryCode()));

    return params;
  }

  private NameValuePair newParam(String key, String value) {
    return new BasicNameValuePair(key, value);
  }
}
