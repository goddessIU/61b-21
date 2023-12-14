package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (args.length > 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.init();
                break;
            case "add":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                try {
                    Repository.add(args[1]);
                } catch (GitletException e) {
                    System.exit(0);
                }

                break;
            case "commit":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    return;
                } else if (args.length > 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.commit(args[1]);
                break;
            case "log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.log();
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.rm(args[1]);
                break;
            case "checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkout2(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkout3(args[1]);
                } else {
                    System.out.println("Incorrect operands.");
                    return;
                }
                break;
            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.status();
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.newBranch(args[1]);
                break;

            default:
                System.out.println("No command with that name exists.");
        }
    }
}
