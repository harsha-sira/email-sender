package models;

import java.io.Serializable;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class SendEmailMessage implements Serializable {

    private String requestId;
    private String senderName;
    private String reciepientAddress;
    private String subject;
    private String message;

    //  constructors
    public SendEmailMessage(String requestId, String senderName, String reciepientAddress, String subject, String message) {
        this.requestId = requestId;
        this.senderName = senderName;
        this.reciepientAddress = reciepientAddress;
        this.subject = subject;
        this.message = message;
    }

    public SendEmailMessage() {
    }

    //   getters and setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReciepientAddress() {
        return reciepientAddress;
    }

    public void setReciepientAddress(String reciepientAddress) {
        this.reciepientAddress = reciepientAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // override equals and hashcode for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendEmailMessage)) return false;

        SendEmailMessage email = (SendEmailMessage) o;

        return getRequestId().equals(email.getRequestId());

    }

    @Override
    public int hashCode() {
        return getRequestId().hashCode();
    }
}
