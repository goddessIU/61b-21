package gitlet;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

public class Stage implements Serializable, Dumpable{
    /** record all staged_file
     * filename -> sha code */
    private TreeMap<String, String> stagedFileMapper;

    /**
     * add file code to sets
     */
    public Stage() {
        stagedFileMapper = new TreeMap<>();
    }
    public void addFile(String code, String fileName) {
        stagedFileMapper.put(fileName, code);
    }

    public String getShaCode(String fileName) {
        return stagedFileMapper.get(fileName);
    }

    /**
     *
     * @return file has existed
     */
    private boolean isExistFile(String fileName, String sha) {
        return stagedFileMapper.containsKey(fileName);
    }

    /**
     *  if the file is exist
     * @return file need overwrite
     */
    private boolean isDifferentFile(String fileName, String sha) {
        return stagedFileMapper.get(fileName).equals(sha);
    }

    /**
     *
     * @return whether need to stage the file
     */
    public boolean isNeedToStage(String fileName, String sha) {
        return (!isExistFile(fileName, sha)) || isDifferentFile(fileName, sha);
    }

    public TreeMap<String, String> getStagedFileMapper() {
        return this.stagedFileMapper;
    }

    public void dump() {
        System.out.println(stagedFileMapper);
    }


}
