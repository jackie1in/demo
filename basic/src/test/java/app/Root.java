package app;
import lib.Sudo;

public class Root {
    static Sudo chmod(Sudo sudo) {
        sudo = new Sudo(1);
        return sudo;
    }
    public static void main(String[] args) {
        Sudo sudo = new Sudo(0);
        System.out.print(sudo.getPid());
        Sudo sudo2 = chmod(sudo);
        System.out.print(sudo.getPid());
        System.out.print(sudo2.getPid());
        sudo = chmod(sudo2);
        System.out.print(sudo.getPid());
        System.out.print(sudo2.getPid());
    }
}