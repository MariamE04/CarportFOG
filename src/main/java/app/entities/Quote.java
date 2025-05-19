    package app.entities;

    import java.time.LocalDate;
    import java.util.Date;

    public class Quote {
        private int quoteId;
        private LocalDate validityPeriod;
        private double price;
        private LocalDate dateCreated;
        private boolean isAccepted;
        private boolean isVisible;
        private  boolean isExpired;
        private int order_id;
        private boolean isPaid;



        public Quote(int quoteId, LocalDate validityPeriod, double price, LocalDate dateCreated, boolean isAccepted, boolean isVisible) {
            this.quoteId = quoteId;
            this.validityPeriod = validityPeriod;
            this.price = price;
            this.dateCreated = dateCreated;
            this.isAccepted = isAccepted;
            this.isVisible = isVisible;
        }
        public Quote(LocalDate validityPeriod, double price, int order_id){
            this.validityPeriod = validityPeriod;
            this.price = price;
            this.dateCreated = LocalDate.now();
            this.order_id = order_id;
            this.isAccepted = false;
            this.isVisible = true;
        }

        public Quote(int quoteId, boolean isVisible, boolean isAccepted) {
            this.quoteId = quoteId;
            this.isVisible = isVisible;
            this.isAccepted = isAccepted;
        }

        public boolean isExpired() {
            return dateCreated.plusDays(14).isBefore(LocalDate.now());
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
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

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
        }


        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
        public boolean isPaid() {
            return isPaid;
        }
    }
