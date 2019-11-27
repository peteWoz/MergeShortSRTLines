package main.use.it.free;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TextStructModel {
    private static SimpleDateFormat mSdf = new SimpleDateFormat("HH:mm:ss,SSS");
    private static String newline = System.getProperty("line.separator");
    public ArrayList<String> mTextLines = null;
    public int mDiffSeconds = 0;
    public Date mFromTime = null;
    public Date mToTime = null;
    public String index;
    public String subtitleText;

    public TextStructModel() {
    }

    /**
     * Constructor that takes an ArrayList<String> where each item in that List is a part of a single SRT line
     * i.e. index, from --> to timing, and the actual text.
     *
     * @param textLines A single SRT line broken up into a List of Strings.
     */
    public TextStructModel(ArrayList<String> textLines) {
        mTextLines = textLines;
        //String indexIn = mTextLines.get(0).trim();
        index = (mTextLines.get(0)).toString().trim();
        //System.out.println("It is:" + indexIn + ". No of chars: " + indexIn.length());
        //index = Integer.parseInt(indexIn);
        subtitleText = String.join(" ", mTextLines.subList(2, mTextLines.size()));
        String[] splited = mTextLines.get(1).split(" ");
        try {
            mFromTime = mSdf.parse(splited[0]);
            mToTime = mSdf.parse(splited[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Merges two TextStructModels into one.
     *
     * @param first  One TextStructModel to merge
     * @param second The second TextStructModel to merge
     * @param index  The new index for the merged TextStructModel
     * @return
     */
    public static TextStructModel mergeTwo(TextStructModel first, TextStructModel second, int index) {
        //earlier first gives a minus value and positive otherwise
        int comparison = first.mFromTime.compareTo(second.mFromTime);
        TextStructModel earlier = first;
        TextStructModel later = second;
        if (comparison > 0) {
            earlier = second;
            later = first;
        }
        TextStructModel result = new TextStructModel();
        result.index = "" + index;
        result.mFromTime = earlier.mFromTime;
        result.mToTime = later.mToTime;
        result.subtitleText = earlier.subtitleText + " " + later.subtitleText;
        return result;
    }

    /**
     * Builds up a String representation of the supplied TextStructModel
     *
     * @param struct
     * @return
     */
    public static String buildTextStructIntoString(TextStructModel struct) {
        StringBuilder builder = new StringBuilder();
        builder.append(struct.index + newline);
        builder.append(mSdf.format(struct.mFromTime) + " --> " + mSdf.format(struct.mToTime) + newline);
        builder.append(struct.subtitleText + newline + newline);
        return builder.toString();
    }

    /**
     * Builds up a lengthy StringBuilder out of the list of passed TextStructModels so that it is ready
     * to be built into a String and saved to a file.
     *
     * @param list A List of TextStructModels i.e. individual SRT lines
     * @return StringBuilder containing the List of passed arguments appended into one large chunk.
     */
    public static StringBuilder buildTextStructListIntoStringBuilder(List<TextStructModel> list) {
        StringBuilder builder = new StringBuilder();
        for (TextStructModel struct : list) {
            builder.append(struct.index + newline);
            builder.append(mSdf.format(struct.mFromTime) + " --> " + mSdf.format(struct.mToTime) + newline);
            builder.append(struct.subtitleText + newline + newline);
        }
        return builder;
    }

    /**
     * Calculates the duration of a single SRT line.
     *
     * @return Duration of the TextStructModel
     */
    public double getDuration() {
        Calendar calEndTime = Calendar.getInstance();
        calEndTime.setTime(mToTime);
        Calendar calStartTime = Calendar.getInstance();
        calStartTime.setTime(mFromTime);
        //System.out.println(Math.round((double)Duration.between(calStartTime.toInstant(), calEndTime.toInstant()).toMillis()/1000));
        //return (int) Math.round((double)Duration.between(calStartTime.toInstant(), calEndTime.toInstant()).toMillis()/1000);
        return (double) Duration.between(calStartTime.toInstant(), calEndTime.toInstant()).toMillis() / 1000;
    }

    /**
     * Leftover from previous work. Don't rely on it.
     *
     * @param aDiffSeconds
     * @return
     */
    public String addSeconds(int aDiffSeconds) {
        mDiffSeconds = aDiffSeconds;
        String s = "";
        for (int i = 0; i < mTextLines.size(); i++) {
            if (i == 1) {
                s += mSdf.format(addTime(mFromTime)) + " --> " + mSdf.format(addTime(mToTime)) + newline;
                //System.out.println("Result of format is: " + mSdf.format(addTime(mFromTime)));
            } else {
                s += mTextLines.get(i) + newline;
            }
        }
        return s;
    }

    /**
     * Leftover code from previous work. Don't rely on it.
     *
     * @param d
     * @return
     */
    private Date addTime(Date d) {
        Calendar cal = Calendar.getInstance();
        //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTime(d);
        //System.out.println("Time b4 update is: " + cal.getTime());
        cal.add(Calendar.SECOND, mDiffSeconds);
        //System.out.println("Time after update is: " + cal.getTime());
        return cal.getTime();
    }

}
