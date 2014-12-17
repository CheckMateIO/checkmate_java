package com.checkmate.sdk.reservations;

/**
 * Represents a set of options for listing reservations.
 */
public class ReservationsOptions {
  private String propertyId;
  private String confirmationNumber;
  private Boolean excludeProperties;

  private ReservationsOptions(String propertyId, String confirmationNumber,
      Boolean excludeProperties) {
    this.propertyId = propertyId;
    this.confirmationNumber = confirmationNumber;
    this.excludeProperties = excludeProperties;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public String getConfirmationNumber() {
    return confirmationNumber;
  }

  public Boolean getExcludeProperties() {
    return excludeProperties;
  }

  public static class Builder {
    private String propertyId;
    private String confirmationNumber;
    private Boolean excludeProperties;

    public Builder() {}

    public Builder setPropertyId(String propertyId) {
      this.propertyId = propertyId;
      return this;
    }

    public Builder setConfirmationNumber(String confirmationNumber) {
      this.confirmationNumber = confirmationNumber;
      return this;
    }

    public Builder setExcludeProperties(Boolean excludeProperties) {
      this.excludeProperties = excludeProperties;
      return this;
    }

    public ReservationsOptions build() {
      return new ReservationsOptions(propertyId, confirmationNumber, excludeProperties);
    }
  }
}
