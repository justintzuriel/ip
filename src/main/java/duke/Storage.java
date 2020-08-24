package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() throws DukeException {
        List<Task> tasks = new ArrayList<>();
        try {
            int lastSlash = this.filePath.lastIndexOf('/');
            if (lastSlash != -1) {
                String folderDir = filePath.substring(0, lastSlash);
                Files.createDirectories(Paths.get("./" + folderDir));
            }
            File file = new File(this.filePath);
            if (!file.createNewFile()) {
                loadFromFile(tasks);
            }
        } catch (IOException ex) {
            throw new DukeException("Oh dear! " + ex.getMessage());
        }
        return tasks;
    }

    private void loadFromFile(List<Task> tasks) throws DukeException {
        try {
            File f = new File(this.filePath);
            Scanner s = new Scanner(f);
            int lineCounter = 0;
            while (s.hasNext()) {
                lineCounter++;
                String line = s.nextLine();
                String[] attr = line.split(" \\| ");
                formatCheck(attr, lineCounter);
                switch (attr[0]) {
                    case "T":
                        tasks.add(new Todo(attr[2], attr[1].equals("V")));
                        break;
                    case "D":
                        tasks.add(taskCreator("deadline", attr));
                        break;
                    default:
                        tasks.add(taskCreator("event", attr));
                        break;
                }
            }
        } catch (DateTimeParseException ex) {
            throw new DukeException("Oh dear! Bad date and time format.");
        } catch (FileNotFoundException ex) {
            throw new DukeException(ex.getMessage());
        }
    }

    private Task taskCreator(String type, String[] attr) {
        String meta = attr[3];
        if (meta.contains(" ")) {
            LocalDate date = LocalDate.parse(meta.substring(0, meta.indexOf(' ')),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            LocalTime time = LocalTime.parse(meta.substring(meta.indexOf(' ') + 1),
                    DateTimeFormatter.ofPattern("HHmm"));
            return type.equals("deadline")
                    ? new Deadline(attr[2], date, time, attr[1].equals("V"))
                    : new Event(attr[2], date, time, attr[1].equals("V"));
        } else {
            LocalDate date = LocalDate.parse(meta, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return type.equals("deadline")
                    ? new Deadline(attr[2], date, attr[1].equals("V"))
                    : new Event(attr[2], date, attr[1].equals("V"));
        }
    }

    private void formatCheck(String[] attr, int lineCounter) throws DukeException {
        if (attr.length < 3 || (!attr[1].equals("V") && !attr[1].equals("X"))
                || ((attr[0].equals("D") || attr[0].equals("E")) && attr.length != 4)
                || (!attr[0].equals("D") && !attr[0].equals("E")) && !attr[0].equals("T")) {
            throw new DukeException("Oh dear! Invalid task in line " + lineCounter + ".");
        }
    }

    public void save(List<Task> tasks) throws DukeException{
        StringBuilder taskData = new StringBuilder();
        for (Task ts : tasks) {
            taskData.append(ts.formatTask()).append("\n");
        }
        try {
            writeToFile(taskData.toString());
        } catch (IOException ex) {
            throw new DukeException("Oh dear! " + ex.getMessage());
        }
    }

    private void writeToFile(String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        fw.write(textToAdd);
        fw.close();
    }
}