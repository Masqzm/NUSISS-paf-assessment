package vttp.batch5.paf.movies.models.exception;

import java.util.Date;

public class SQLErrorLog {
    private String[] ids;
    private String error;
    private Date date;

    public SQLErrorLog(String[] ids, String error, Date date) {
        this.ids = ids;
        this.error = error;
        this.date = date;
    }
    public String[] getIds() {
        return ids;
    }
    public void setIds(String[] ids) {
        this.ids = ids;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
