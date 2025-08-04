package ShowBooking;

import ShowBooking.Types.PaymentStatus;

import java.util.UUID;

public class Payment {
    private Show show;
    private PaymentStatus status;
    private double totalPrice;
    private final String id;

    public Payment(Show show, PaymentStatus status, double totalPrice){
        this.show = show;
        this.status = status;
        this.totalPrice = totalPrice;
        this.id = UUID.randomUUID().toString();
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Show getShow() {
        return show;
    }

    public String getId() {
        return id;
    }
}
