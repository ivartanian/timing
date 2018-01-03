package domain;

import javafx.beans.property.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"dateTime", "description"})
public class Event {
    private ObjectProperty<LocalDateTime> dateTime;
    private StringProperty description;
    private BooleanProperty done;

    public Event(LocalDateTime dateTime, String description, boolean done) {
        this.dateTime = new SimpleObjectProperty<>(dateTime);
        this.description = new SimpleStringProperty(description);
        this.done = new SimpleBooleanProperty(done);
    }

    public void setDone(boolean done) {
        this.done.set(done);
    }

    public LocalDateTime getDateTime() {
        return dateTime.get();
    }

    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime.set(dateTime);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean isDone() {
        return done.get();
    }

    public BooleanProperty doneProperty() {
        return done;
    }
}
