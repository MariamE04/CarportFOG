package app.entities;

import java.time.LocalDate;
import java.util.Date;

public class Quote {
    int quoteId;
    LocalDate validityPeriod;
    int price;
    LocalDate dateCreated;
    boolean isAccepted;

    public Quote(int quoteId, LocalDate validityPeriod, int price, LocalDate dateCreated, boolean isAccepted) {
        this.quoteId = quoteId;
        this.validityPeriod = validityPeriod;
        this.price = price;
        this.dateCreated = dateCreated;
        this.isAccepted = isAccepted;
    }

    public Quote(LocalDate validityPeriod, int price, LocalDate dateCreated, boolean isAccepted) {
        this.validityPeriod = validityPeriod;
        this.price = price;
        this.dateCreated = dateCreated;
        this.isAccepted = isAccepted;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public LocalDate getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(LocalDate validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
