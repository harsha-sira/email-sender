package models;

import Utils.Constants;

import java.io.Serializable;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class SendEmailAckMessage implements Serializable {

    private String requestId;
    private String status;

    // constructors
    public SendEmailAckMessage() {
        this.status = Constants.ERROR;
    }

    public SendEmailAckMessage(String requestId, String status) {
        this.requestId = requestId;
        this.status = status;
    }

    public SendEmailAckMessage(String requestId) {
        this.requestId = requestId;
        this.status = Constants.ERROR;
    }

    //getters and setters

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // override equals and hashcode for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendEmailAckMessage)) return false;

        SendEmailAckMessage that = (SendEmailAckMessage) o;

        if (!getRequestId().equals(that.getRequestId())) return false;
        return getStatus().equals(that.getStatus());

    }

    @Override
    public int hashCode() {
        int result = getRequestId().hashCode();
        result = 31 * result + getStatus().hashCode();
        return result;
    }

    //override toString()
    @Override
    public String toString() {
        return "SendEmailAckMessage{" +
                "requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
