package com.checkmate.sdk.reservations;

/**
* Represents a reservation to be created in the Checkmate system.
*
* Your life will be easier if you use the provided Builder class for
* creating Reservation objects.
*/
public class Reservation {
  private String externalId;
  private String confirmationNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String averageDailyRate;
  private String currency;
  private String roomCode;
  private String roomDescription;
  private String startOn;
  private String endOn;
  private String loyaltyNumber;
  private String defaultLocale;
  private Property property;

  private Reservation(String externalId, String confirmationNumber, String firstName,
      String lastName, String email, String averageDailyRate, String currency,
      String roomCode, String roomDescription, String startOn, String endOn,
      String loyaltyNumber, String defaultLocale, Property property) {
    this.externalId = externalId;
    this.confirmationNumber  = confirmationNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.averageDailyRate = averageDailyRate;
    this.currency = currency;
    this.roomCode = roomCode;
    this.roomDescription = roomDescription;
    this.startOn = startOn;
    this.endOn = endOn;
    this.loyaltyNumber = loyaltyNumber;
    this.defaultLocale = defaultLocale;
    this.property = property;
  }

  public static class Builder {
    private String externalId;
    private String confirmationNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String averageDailyRate;
    private String currency;
    private String roomCode;
    private String roomDescription;
    private String startOn;
    private String endOn;
    private String loyaltyNumber;
    private String defaultLocale;
    private Property property;

    public Builder() {}

    public Builder setExternalId(String externalId) {
      this.externalId = externalId;
      return this;
    }

    public Builder setConfirmationNumber(String confirmationNumber) {
      this.confirmationNumber = confirmationNumber;
      return this;
    }

    public Builder setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder setLastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder setAverageDailyRate(String averageDailyRate) {
      this.averageDailyRate = averageDailyRate;
      return this;
    }

    public Builder setCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public Builder setRoomCode(String roomCode) {
      this.roomCode = roomCode;
      return this;
    }

    public Builder setRoomDescription(String roomDescription) {
      this.roomDescription = roomDescription;
      return this;
    }

    public Builder setStartOn(String startOn) {
      this.startOn = startOn;
      return this;
    }

    public Builder setEndOn(String endOn) {
      this.endOn = endOn;
      return this;
    }

    public Builder setLoyaltyNumber(String loyaltyNumber) {
      this.loyaltyNumber = loyaltyNumber;
      return this;
    }

    public Builder setDefaultLocale(String defaultLocale) {
      this.defaultLocale = defaultLocale;
      return this;
    }

    public Builder setProperty(Property property) {
      this.property = property;
      return this;
    }

    public Reservation build() {
      return new Reservation(externalId, confirmationNumber, firstName,
      lastName, email, averageDailyRate, currency,
      roomCode, roomDescription, startOn, endOn,
      loyaltyNumber, defaultLocale, property);
    }
  }
}
