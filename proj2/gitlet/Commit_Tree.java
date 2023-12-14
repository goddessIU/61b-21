package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Commit_Tree implements Serializable {
    // commit shacode -> commit
    private  TreeMap<String, Commit> commitMap;
    // header pointer
    private  String header;
    // branch name -> commit ref
    private  TreeMap<String, Commit> branchMap;
    private String curBranch;

    public Commit_Tree(Commit commit) {
        commitMap = new TreeMap<>();
        branchMap = new TreeMap<>();
        header = commit.getCommitId();
        this.newBranch("master", commit);
        curBranch = "master";
    }

    public  Commit getCommitById(String sha) {
        return commitMap.get(sha);
    }

    public  Commit getCommitByPartialId(String sha) {
        int num = 0;
        Commit ret = null;
        for (String code : commitMap.keySet()) {
            if (code.startsWith(sha)) {
                if (num >= 1) {
                    return ret;
                }
                num++;
                ret = commitMap.get(code);
            }
        }
        return ret;
    }

    public  void addCommit(Commit commit) {
        commitMap.put(commit.getCommitId(), commit);
        header = commit.getCommitId();
    }

    public String getCurBranch() { return curBranch; }

    public Commit getHeader() {
        return commitMap.get(header);
    }

    public boolean isExistBranch(String branchName) {
        return branchMap.containsKey(branchName);
    }

    public void newBranch(String branchName, Commit commit) {
        branchMap.put(branchName, commit);
    }

    public void printBranches() {
        for (String name : branchMap.keySet()) {
            if (name.equals(curBranch)) {
                System.out.print("*");
            }
            System.out.println(name);
        }
        System.out.println();
    }
}
