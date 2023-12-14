package gitlet;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

public class Stage implements Serializable, Dumpable{
    /** record all staged_file
     * filename -> sha code */
    private TreeMap<String, String> stagedFileMapper;

    /**
     * deleted file mapper
     */
    private TreeSet<String> deletedFileSet;

    /**
     * add file code to sets
     */
    public Stage() {
        stagedFileMapper = new TreeMap<>();
        deletedFileSet = new TreeSet<>();
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

    public boolean isStagedFile(String fileName) {
        return stagedFileMapper.containsKey(fileName);
    }

    public void removeFile(String fileName) {
        stagedFileMapper.remove(fileName);
    }

    public void stagedForCommitRm(String fileName) {
        deletedFileSet.add(fileName);
    }

    public TreeSet<String> getDeletedFileSet() { return deletedFileSet; }

    public void printStagedFiles() {
        for (String fileName : stagedFileMapper.keySet()) {
            System.out.println(fileName);
        }
        System.out.println();
    }

    public void dump() {
        System.out.println(stagedFileMapper);
        System.out.println(deletedFileSet);
    }


}
