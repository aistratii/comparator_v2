package imagesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import imagesearch.image.CustomImageType;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Main2ImageSearchSimple {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<Integer, Set<String>> db = new TreeMap<>();
    private static final Set<Pair<String, String>> clones =new HashSet<>();

    public static final int ACCEPTABLE_RGB_CHANNEL_DIF = 5;
    public static final int ACCEPTABLE_PERCENTAGE_OF_MATCHING = 5;
    public static final int PERSIST_ITERATION_INTERVAL = 100;


    public static long version = 0l;

    public static void main(String args[]) {
        System.out.println(LocalDateTime.now());
        //insert into db
        //String directory = "C:\\Users\\Alex\\Downloads";
        //String directory = "C:\\Users\\Alex\\Desktop\\imgs";
        String directory = "C:\\Users\\Alex\\Desktop\\nsfw tmp";
        //String directory = "E:\\nsfw";
        List<String> files = getFileNames(directory);
        fillDb(files);

        //read db file
        /*try {
            FileReader fileReader = new FileReader(new File("result/db_serialized_22.db"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Map<Integer, Set<String>> fromDbFile = objectMapper.reader().forType(Map.class).readValue(new File("result/db_serialized_22.db"));
                    //bufferedReader.readLine());
            System.out.println(fromDbFile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println(LocalDateTime.now());
    }

    private static void persistInfo(long version) {
        //writeObject(db.toString(), "result/db_readable.db".replace(".", "_" + version +"."));
        writeObject(db, "result/db_serialized.db".replace(".", "_" + version +"."));
        //writeObject(clones.toString(), "result/clones_readable.db".replace(".", "_" + version +"."));
        writeObject(clones, "result/clones_serialized.db".replace(".", "_" + version +"."));
    }

    private static void writeObject(Object object, String fileName) {
        try {
            FileWriter fos = new FileWriter(new File(fileName));
            BufferedWriter oos = new BufferedWriter(fos);
            oos.write(objectMapper.writer().writeValueAsString(object));
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void fillDb(List<String> files) {
        for (String fileName : files) {
            try {
                if (version % PERSIST_ITERATION_INTERVAL == 0){
                    persistInfo(version / PERSIST_ITERATION_INTERVAL);
                }
                version++;

                CustomBufferedImage image = CustomBufferedImage.CustomBufferedImageReader.readImage(fileName);
                Pair<Boolean, String> flagAndImage = isContainedInDb(image);

                if (!flagAndImage.getLeft()) {
                    if (db.get(image.averageRgb()) == null)
                        db.put(image.averageRgb(), new HashSet<>(asList(fileName)));
                    else
                        db.get(image.averageRgb()).add(fileName);
                } else {
                    clones.add(Pair.of(fileName, flagAndImage.getRight()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        persistInfo(version++);
    }

    private static Pair<Boolean, String> isContainedInDb(CustomBufferedImage image) {
        if (db.get(image.averageRgb()) == null)
            return Pair.of(false, null);
        else {
            Set<String> fileNames = db.get(image.averageRgb());

            return fileNames.parallelStream()
                    .map(fileName -> Pair.of(fileName, CustomBufferedImage.CustomBufferedImageReader.readImage(fileName)))
                    .filter(pair -> image.compareTo(pair.getRight()))
                    .findFirst()
                    .map(pair -> Pair.of(true, pair.getLeft()))
                    .orElse(Pair.of(false, null));
        }
    }

    private static List<String> getFileNames(String directory) {
        Pattern acceptedFormats[] = {Pattern.compile("^.*(.jpg)$"), Pattern.compile("^.*(.jpeg)$"), Pattern.compile("^.*(.png)$")};


        return stream(new File(directory).listFiles())
                .filter(file -> stream(acceptedFormats)
                        .filter(pattern -> pattern.matcher(file.getAbsolutePath()).matches())
                        .findAny()
                        .isPresent())
                .map(file -> file.getAbsolutePath())
                .collect(toList());
    }
}
