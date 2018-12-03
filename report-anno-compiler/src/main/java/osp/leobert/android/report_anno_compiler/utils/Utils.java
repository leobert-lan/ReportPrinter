package osp.leobert.android.report_anno_compiler.utils;

/**
 * <p><b>Package:</b> osp.leobert.android.issueprint.utils </p>
 * <p><b>Project:</b> IssuePrinter </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 13/04/2018.
 */
public class Utils {


    public static String firstCharUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static String genFileName(String type, String ext) {
        return firstCharUpperCase(type) + "Report." + ext;
    }

    public static String genReporterClzName(String type) {
        return firstCharUpperCase(type) + "Reporter";
    }

    public static String generateReportFilePath(String type, String ext) {
        return "./Reports/" + firstCharUpperCase(type) + "Report." + ext;
    }

}
