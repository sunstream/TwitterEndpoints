package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class PostGenerator implements CommonErrorMessages {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String SEPARATOR = ";";

    private List<Integer> tokensCountPerLine;
    private final String sourceFileName;
    private List<List<String>> tokens = null;

    private final Random random = new Random();

    private PostGenerator(String filename){
        sourceFileName = filename;
        readSourceFile();
    }
    public PostGenerator(){
        this(PropertiesReader.getPlainProperty("arbitrarypost.source"));
    }

    public String generateRandomPost(int charCount){
        return RandomStringUtils.randomAlphabetic(charCount);
    }

    public String generateArbitraryPost(){
        StringBuilder post = new StringBuilder();
        Iterator<List<String>> tokensIterator = tokens.iterator();
        for(Integer lineSizeInTokens : tokensCountPerLine){
            for (int j=0; j<lineSizeInTokens; j++){
                List<String> variants = tokensIterator.next();
                int index = variants.size() == 1 ? 0 : random.nextInt(variants.size());
                post.append(variants.get(index)).append(" ");
            }
            post.append(NEW_LINE);
        }
        String signature = getDateTimeSignature();
        post.append(signature);
        return post.toString();
    }

    private void readSourceFile(){
        Path filePath = Paths.get("src/test/resources/"+ sourceFileName);
        try {
            tokens = Files.lines(filePath)
                    .map(line-> Arrays.asList(line.split(SEPARATOR)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            fail(byException(e, "Failed to read from a file with tokens for arbitrary posts generating."));
        }
        readTokensCount();
    }

     private void readTokensCount(){
        List<String> tokensCountDescription = tokens.get(0);
        tokensCountPerLine = tokensCountDescription
                .stream()
                .map(entry -> Integer.parseInt(entry.trim()))
                .collect(Collectors.toList());
        tokens.remove(0);
    }

    private String getDateTimeSignature(){
        DateTime dt = DateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, yyyy 'at' HH:mm");
        return dt.toString(formatter);
    }




}
