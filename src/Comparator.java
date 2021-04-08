import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Comparator {
    private String rootPath;
    private HashMap<String, ArrayList<String>> register = new HashMap<>();

    public Comparator(String rootPath) {
        this.rootPath = rootPath;
    }

    public void run() {
        Path dir = Paths.get(rootPath);
        try {
            Files.walk(dir).forEach(path -> handleFile(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFile(File file) {
        if (file.isFile()) {
            String checksum = getMD5FromFile(file);
            ArrayList<String> tempList = new ArrayList<>();
            if (register.containsKey(checksum)) {
                tempList = register.get(checksum);
            }
            tempList.add(file.getAbsolutePath());
            register.put(checksum, tempList);
        }
    }

    private String getMD5FromFile(File file) {
        String md5 = null;

        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            while ((bytesCount = fileInputStream.read(byteArray)) != -1) {
                md5Digest.update(byteArray, 0, bytesCount);
            }
            fileInputStream.close();

            byte[] bytes = md5Digest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte myByte : bytes) {
                sb.append(Integer.toString((myByte & 0xff) + 0x100, 16).substring(1));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return md5;
    }

    public void printDuplicates() {
        register.forEach((checksum, list) -> {
            if (list.size() > 1) {
                list.forEach(foo -> System.out.println(foo));
                System.out.println("\n===========================\n");
            }
        });
    }

    public void saveResultToFile(String filepath) {
        try {
            FileWriter myWriter = new FileWriter(filepath);
            StringBuilder result = new StringBuilder();
            register.forEach((checksum, list) -> {
                if (list.size() > 1) {
                    list.forEach(foo -> {
                        result.append(foo);
                        result.append("\n");
                    });
                    result.append("\n===========================\n");
                }
            });
            myWriter.write(result.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
