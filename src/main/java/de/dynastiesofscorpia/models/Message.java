package de.dynastiesofscorpia.models;

import de.dynastiesofscorpia.repository.PlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

@Document
public class Message {
    @Id
    private ObjectId id;

    private ObjectId from;

    private ObjectId to;

    private long timestamp;

    private String message;

    private boolean unread;

    public Message() {
        this.id = new ObjectId();
        this.timestamp = System.currentTimeMillis();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Player getFrom() {
        return PlayerRepository.get().findById(from);
    }

    public void setFrom(Player from) {
        this.from = from.getId();
    }

    public Player getTo() {
        return PlayerRepository.get().findById(to);
    }

    public void setTo(Player to) {
        this.to = to.getId();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date(getTimestamp());

        return sdf.format(date);
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
