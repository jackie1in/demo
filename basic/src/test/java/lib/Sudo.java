package lib;

public class Sudo {
    private int pid;

    public Sudo(int pid) {
        this.pid = pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }
}
