package com.checkmate.sdk.entities;

/**
 * Used to represent a strutured address as part of a Property.
 */
public class Address {
  String street;
  String city;
  String region;
  String postalCode;
  String countryCode;

  private Address(String street, String city, String region, String postalCode,
      String countryCode) {
    this.street = street;
    this.city = city;
    this.region = region;
    this.postalCode = postalCode;
    this.countryCode = countryCode;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getRegion() {
    return region;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public static class Builder {
    String street;
    String city;
    String region;
    String postalCode;
    String countryCode;

    public Builder() {}

    public Builder setStreet(String street) {
      this.street = street;
      return this;
    }

    public Builder setCity(String city) {
      this.city = city;
      return this;
    }

    public Builder setRegion(String region) {
      this.region = region;
      return this;
    }

    public Builder setPostalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    public Builder setCountryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Address build() {
      return new Address(street, city, region, postalCode, countryCode);
    }
  }
}
