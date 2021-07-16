package stangenzirkel.notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private long id;
    private long time;
    private String header;
    private String body;

    public Note(long id, long time, String header, String body) {
        this.id = id;
        this.time = time;
        this.header = header;
        this.body = body;
    }

    public String getDate() {
        // return "test";
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        return format.format(new Date(time));
    }
    public String getHeader() {
        if (header.length() == 0) {
            return "Note №".concat(Integer.toString((int) id));
        } else return header;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        if (body.length() == 0) {
            return "this note empty";
        } else return body;
    }
}