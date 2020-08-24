package duke;

public class Task {

    protected String desc;
    protected boolean isDone;

    public Task(String desc) {
        this.desc = desc;
        this.isDone = false;
    }

    public Task(String desc, boolean isDone) {
        this.desc = desc;
        this.isDone = isDone;
    }

    public Task setDone() {
        Task doneTask = new Task(this.desc);
        doneTask.isDone = true;
        return doneTask;
    }

    public String formatTask() {
        return ("P | " + (isDone ? "V" : "X") + " | " + desc);
    }

    @Override
    public String toString() {
        return ("[" + (isDone ? "V" : "X") + "] " + desc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Task) {
            Task c = (Task) o;
            return this.desc.equals(c.desc);
        } else {
            return false;
        }
    }
}
