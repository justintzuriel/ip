import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

public class Event extends Task {

    protected LocalDate date;
    protected Optional<LocalTime> time;

    public Event(String desc, boolean isDone, LocalDate date) {
        super(desc, isDone);
        this.date = date;
        this.time = Optional.empty();
    }

    public Event(String desc, boolean isDone, LocalDate date, LocalTime time) {
        super(desc, isDone);
        this.date = date;
        this.time = Optional.ofNullable(time);
    }

    @Override
    public Task setDone() {
        Task doneTask = this.time.map(localTime -> new Event(this.desc, this.isDone, this.date, localTime))
                .orElseGet(() -> new Event(this.desc, this.isDone, this.date));
        doneTask.isDone = true;
        return doneTask;
    }

    @Override
    public String formatTask() {
        return ("E" + " | " + (isDone ? "V" : "X") + " | " + desc + " | "
                + this.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + " "
                + this.time.map(localTime -> localTime.format(DateTimeFormatter.ofPattern("HHmm"))));
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: "
                + this.date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + " "
                + (this.time.isPresent() ? this.time.get() : "") + ")";
    }
}
