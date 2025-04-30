package app.entities;

import java.time.LocalDate;
import java.util.Date;

public class Quote {
    int quoteId;
    int validityPeriod;
    int price;
    Date dateCreated;
    boolean isAccepted;

    public Quote(int quoteId, int validityPeriod, int price, Date dateCreated, boolean isAccepted) {
        this.quoteId = quoteId;
        this.validityPeriod = validityPeriod;
        this.price = price;
        this.dateCreated = dateCreated;
        this.isAccepted = isAccepted;
    }

    public Quote(int validityPeriod, int price, Date dateCreated, boolean isAccepted) {
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

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
