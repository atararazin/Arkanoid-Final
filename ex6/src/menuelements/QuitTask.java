package menuelements;

public class QuitTask implements Task<Void> {

    @Override
    public Void run() {
        System.exit(0);
        return null;
    }
}
