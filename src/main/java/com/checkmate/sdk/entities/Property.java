package com.checkmate.sdk.entities;

/**
 * Used to represent a property in the context of creating a new reservation.
 */
public class Property {
  String name;
  String phone;
  String fullAddress;
  String latitude;
  String longitude;
  Address address;

  private Property(String name, String phone, String fullAddress, String latitude,
      String longitude, Address address) {
    this.name = name;
    this.phone = phone;
    this.fullAddress = fullAddress;
    this.latitude = latitude;
    this.longitude = longitude;
    this.address = address;
  }

  public static class Builder {
    String name;
    String phone;
    String fullAddress;
    String latitude;
    String longitude;
    Address address;

    public Builder() {}

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder setFullAddress(String fullAddress) {
      this.fullAddress = fullAddress;
      return this;
    }

    public Builder setLatitude(String latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder setLongitude(String longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public Property build() {
      return new Property(name, phone, fullAddress, latitude, longitude, address);
    }
  }
}
