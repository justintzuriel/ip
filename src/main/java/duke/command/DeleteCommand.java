package duke.command;

import duke.Task;
import duke.TaskList;
import duke.Ui;
import duke.Storage;
import duke.DukeException;

/**
 * Represents a delete command in the Duke program.
 */
public class DeleteCommand extends Command {

    private int idx;

    /**
     * Initializes a newly created delete command with a task index.
     *
     * @param idx index of the task to delete.
     */
    public DeleteCommand(int idx) {
        this.idx = idx;
    }

    /**
     * Executes the delete command.
     *
     * @param tasks TaskList of the program.
     * @param ui user interface of the program.
     * @param storage storage of the program.
     * @throws DukeException if the task doesn't exist.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        if (this.idx >= tasks.size()) {
            throw new DukeException("Oh dear! That task doesn't exist!");
        }
        Task rmTask = tasks.remove(this.idx);
        storage.save(tasks.getList());
        ui.onDelete(rmTask, tasks.size());
    }

    /**
     * Checks whether an object equals this delete command.
     *
     * @param obj object to be compared with this command.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof DeleteCommand) {
            DeleteCommand dc = (DeleteCommand) obj;
            return this.idx == dc.idx;
        } else {
            return false;
        }
    }
}
