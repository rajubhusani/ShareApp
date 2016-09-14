package shareapp.vsshv.com.shareapp.datasets;

/**
 * Created by PC414506 on 12/09/16.
 */

public class GmailSet {

    private int _id;
    private String toMail;
    private String body;
    private String subject;
    private String scheduled;
    private int status;

    public int get_id() {
        return _id;
    }

    public String getToMail() {
        return toMail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
