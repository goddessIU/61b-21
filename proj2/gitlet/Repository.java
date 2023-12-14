package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The blobs directory. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The staged file */
    public static final File STAGED_FILE = join(GITLET_DIR, "staged");
    /** The current commit */
//    public static Commit CUR_COMMIT;
    public static final File COMMIT_FILE = join(GITLET_DIR, "commit_file");


    /**
    * init command
    * init all things and make a first commit
    */

    public static void init() {
        if (GITLET_DIR.exists()) {
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        }

        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        try {
            STAGED_FILE.createNewFile();
            COMMIT_FILE.createNewFile();
        } catch (Exception e) {

        }

        // initalize stage file
        Stage stage = new Stage();
        Utils.writeObject(STAGED_FILE, stage);

        // initial cur_commit
        Commit commit = new Commit(null, "initial commit", null);
        makeCommit(commit);

        // something else to do.
    }

    /**
     * add command
     */
    public static void add(String fileName) {
        File file = Utils.join(CWD, fileName);
        if (!file.exists()) {
            throw new GitletException("File does not exist.");
        }


        String shaCode = Utils.sha1(Utils.readContentsAsString(file));
        Stage stage = Utils.readObject(STAGED_FILE, Stage.class);
        Commit cur_commit = readObject(COMMIT_FILE, Commit.class);
        if (cur_commit.isNotNeedToTrack(fileName, shaCode)) {
            stage.removeFile(fileName);
            Utils.writeObject(STAGED_FILE, stage);
            return;
        }

        if (stage.isNeedToStage(fileName, shaCode)) {
            File stagedFile = Utils.join(BLOBS_DIR, shaCode);
            if (!stagedFile.exists()) {
                try {
                    stagedFile.createNewFile();
                } catch (Exception e) {
                    System.out.println("haha");
                }
            }
            Utils.writeContents(stagedFile, Utils.readContentsAsString(file));
            stage.addFile(shaCode, fileName);
            Utils.writeObject(STAGED_FILE, stage);
        }
    }

    /**
     * commit command
     */
    public static void commit(String message) {
        Stage stage = Utils.readObject(STAGED_FILE, Stage.class);
        Commit cur_commit = readObject(COMMIT_FILE, Commit.class);
        Commit commit = new Commit(cur_commit, message, stage.getStagedFileMapper());
        // clear stage file and stage obj
        Utils.writeObject(STAGED_FILE, new Stage());

        // write commit to file, update cur_commit
        makeCommit(commit);
    }

    /**
     * make commit persisently
     */
    public static void makeCommit(Commit commit) {
        Utils.writeObject(COMMIT_FILE, commit);
    }

    /**
     * log command
     */
    public static void log() {
        Commit cur_commit = readObject(COMMIT_FILE, Commit.class);
        while (cur_commit != null) {
            System.out.println("===");
            System.out.println("commit " + cur_commit.getCommitId());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-0800"));
            String formattedDate = sdf.format(cur_commit.getTime());
            System.out.println("Date: " + formattedDate);
            System.out.println(cur_commit.getMessage());
            System.out.println();
            cur_commit = cur_commit.getParent();
        }
    }

    /**
     * rm command
     */
    public static void rm(String fileName) {
        Stage stage = Utils.readObject(STAGED_FILE, Stage.class);
        Commit cur_commit = readObject(COMMIT_FILE, Commit.class);
        boolean isStage = stage.isStagedFile(fileName);
        boolean isTracked = cur_commit.isTrackedFile(fileName);
        if (isStage) {
            stage.removeFile(fileName);
        }

        if (isTracked) {
            stage.stagedForCommitRm(fileName);
            File f = Utils.join(CWD, fileName);
            f.delete();
        }
        writeObject(STAGED_FILE, stage);

        if (!isStage && !isTracked) {
            System.out.println("No reason to remove the file.");
        }
    }

    // just for test convenently
    public static void testObjFile(String file) {
        DumpObj.main(file);
    }
}
