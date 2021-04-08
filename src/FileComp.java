public class FileComp {
    public static void main(String[] args) {
        String path = null;
        String save = null;

        for (int i = 0; i < args.length; i++) {
            switch (i) {
                case 0:
                    path = args[i];
                case 1:
                    save = args[i];
            }
        }

        if (path != null) {
            Comparator myComp = new Comparator(args[0]);
            myComp.run();
            myComp.printDuplicates();

            if (save != null) {
                myComp.saveResultToFile(save);
            }
        }
    }
}
