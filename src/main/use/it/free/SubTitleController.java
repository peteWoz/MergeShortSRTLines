package main.use.it.free;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SubTitleController {

    private static String filename;
    public int initialNoOfLines, finalNumberOfLines;
    private int acceptableMergedDuration;
    private StringBuilder builder;

    public SubTitleController(String limit) {
        this.acceptableMergedDuration = Integer.parseInt(limit.trim());
    }

    public SubTitleController(int limit) {
        this.acceptableMergedDuration = limit;
    }

    public static void main(String[] args) throws InterruptedException {
        filename = "297 KSW 1_40_30 do 2_40.srt";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        SubTitleController subTitleController = new SubTitleController("10");
        System.out.println("Passed init");
        //subTitle.readFile("test_file.srt");
        System.out.println("Read file");
        //subTitle.testStuff();
        List<TextStructModel> result1 = subTitleController.mergeShortLines(subTitleController.readFile(filename));
        System.out.println("Merged Lines - first run completed \n");
        result1 = subTitleController.mergeShortLines(result1);
        System.out.println("Merged Lines - second run completed \n");
        result1 = subTitleController.mergeShortLines(result1);
        System.out.println("Merged Lines - third run completed \n");
        subTitleController.saveFile(result1);
    }

    public static void printUsage() {
        System.out.println("use:java MergeShortSRTLines [SubTitle filename] [line duration limit in seconds e.g. 10]]");
    }

    public void setAcceptableMergedDuration(int acceptableMergedDuration) {
        this.acceptableMergedDuration = acceptableMergedDuration;
    }

    public void saveFile(List<TextStructModel> result) {
        builder = TextStructModel.buildTextStructListIntoStringBuilder(result);
        String newFilename = filename.substring(0, filename.length() - 4) + "_processed.srt";
        try {
            Files.write(Paths.get(newFilename), builder.toString().getBytes());
            //System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File has been saved as: " + newFilename);
    }

    public void saveFileTo(String newFilename, List<TextStructModel> result) {
        builder = TextStructModel.buildTextStructListIntoStringBuilder(result);
        try {
            Files.write(Paths.get(newFilename), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File has been saved as: " + newFilename);
    }

    public List<TextStructModel> mergeShortLines(List<TextStructModel> items) {
        System.out.println("Original SRT file had " + items.size() + " lines.");
        int newIndex = 0;
        int j = 1;
        TextStructModel item;
        List<TextStructModel> result = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i += j) {
            //System.out.println (items.get(i).addSeconds(diff_seconds));
            item = items.get(i);
            if (shouldBeMerged(item, items.get(i + 1))) {
                result.add(TextStructModel.mergeTwo(item, items.get(i + 1), ++newIndex));
                j = 2;
            } else {
                item.index = "" + ++newIndex;
                result.add(items.get(i));
                j = 1;
            }
        }
        if (!items.get(items.size() - 1).mToTime.equals(result.get(result.size() - 1).mToTime)) {
            items.get(items.size() - 1).index = "" + ++newIndex;
            result.add(items.get(items.size() - 1));
        }
        System.out.println("New SRT file has: " + (newIndex) + " lines.");
        finalNumberOfLines = newIndex;
        return result;
    }

    private boolean shouldBeMerged(TextStructModel first, TextStructModel second) {
        return (first.getDuration() + second.getDuration()) <= acceptableMergedDuration;
    }

    public List<TextStructModel> readFile(String filename) {
        this.filename = filename;
        List<TextStructModel> items = new ArrayList();
        try {
            // Open the file that is the first
            FileInputStream fstream = new FileInputStream(filename);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";
            ArrayList<String> lines = new ArrayList();
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                //System.out.println (strLine);
                if (isEmpty(strLine) && lines.size() >= 3) {
                    //System.out.println("Empty line");
                    items.add(new TextStructModel(lines));
                    lines = new ArrayList();
                } else {
                    //System.out.println("Non-empty line");
                    lines.add(strLine);
                }
            }
            if (lines.size() > 1)
                items.add(new TextStructModel(lines));
            //Close the input stream
            br.close();
            in.close();
        } catch (Exception e) {//Catch exception if any
            e.printStackTrace();
        }
        initialNoOfLines = items.size();
        return items;
    }

    private boolean isEmpty(String s) {
        if (s == null) return true;
        if (s.trim().isEmpty()) return true;
        return false;
    }
}