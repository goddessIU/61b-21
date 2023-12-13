package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String commitId;
    /** mapper of fileNmae -> blob */
    private TreeMap<String, String> fileMapper;
    private Commit parent;
    /** timestamp */
    private Date time;

    public Commit(Commit parent, String message, TreeMap<String, String> stagedFileMapper) {
        this.message = message;
        this.parent = parent;
        this.time = new Date();
        if (parent == null) {
            fileMapper = new TreeMap<>();
            this.commitId = Utils.sha1(this.message, "", this.time.toString(), this.fileMapper.values().toString());
        } else {
            fileMapper = parent.getUpdatedMapper(stagedFileMapper);
            this.commitId = Utils.sha1(this.message, this.parent.getCommitId(), this.time.toString(), this.fileMapper.values().toString());
        }
    }

    /**
     * make the new map
     */
    private TreeMap<String, String> getUpdatedMapper(TreeMap<String, String> stagedFileMapper) {
        TreeMap<String, String> mapperClone = (TreeMap<String, String>) fileMapper.clone();
        for (String fileName : stagedFileMapper.keySet()) {
            if (mapperClone.containsKey(fileName)) {
                mapperClone.put(fileName, stagedFileMapper.get(fileName));
            }
        }
        return mapperClone;
    }

    private String getCommitId() {
        return  this.commitId;
    }
}
