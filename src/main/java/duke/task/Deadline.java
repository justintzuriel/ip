package duke.task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a task with info of by when it must be done
 */
public class Deadline extends Task {

    private final LocalDate date;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<LocalTime> time;

    /**
     * Initializes a newly created Deadline with a description, date, and whether it is done.
     *
     * @param desc description of task.
     * @param date date of task.
     * @param isDone whether task is done.
     */
    public Deadline(String desc, LocalDate date, boolean isDone) {
        super(desc, isDone);
        this.date = date;
        this.time = Optional.empty();
    }

    /**
     * Initializes a newly created Deadline with a description, date, time, and whether it is done.
     *
     * @param desc description of task.
     * @param date date of task.
     * @param time time of task.
     * @param isDone whether task is done.
     */
    public Deadline(String desc, LocalDate date, LocalTime time, boolean isDone) {
        super(desc, isDone);
        this.date = date;
        this.time = Optional.ofNullable(time);
    }

    /**
     * Sets this task as done.
     *
     * @return task set as done.
     */
    @Override
    public Task setAsDone() {

        boolean wasDone = this.isDone;
        Function<LocalTime, Deadline> toDeadline = localTime ->
                new Deadline(this.description, this.date, localTime, this.isDone);
        Supplier<Deadline> justDateDeadline = () -> new Deadline(this.description, this.date, this.isDone);

        Task doneTask = this.time.map(toDeadline).orElseGet(justDateDeadline);
        doneTask.isDone = true;
        assert this.isDone == wasDone;
        return doneTask;
    }

    /**
     * Formats the task to a string to write in a file.
     *
     * @return formatted task.
     */
    @Override
    public String formatTask() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmm");
        Function<LocalTime, String> convertToString = localTime -> " " + localTime.format(timeFormat);

        return ("D" + " | " + (isDone ? "V" : "X") + " | " + description + " | "
                + this.date.format(dateFormat)
                + this.time.map(convertToString)
                .orElse(""));
    }

    /**
     * Returns a string representation of the task.
     *
     * @return string representation of task.
     */
    @Override
    public String toString() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM d yyyy");
        Function<LocalTime, String> convertToString = time -> " " + time.toString();

        return "[D]" + super.toString() + " (by: "
                + this.date.format(dateFormat)
                + this.time.map(convertToString).orElse("") + ")";
    }
}
