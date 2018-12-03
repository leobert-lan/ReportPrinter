package osp.leobert.android.report_anno_compiler;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Mode </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/12/3.
 */
public enum Mode {
    MODE_FILE(Consts.MODE_FILE), MODE_CLASS(Consts.MODE_CLASS);

    final String mode;

    Mode(String mode) {
        this.mode = mode;
    }

    public static Mode customValueOf(String mode) {
        if (Consts.MODE_FILE.equals(mode))
            return MODE_FILE;
        return MODE_CLASS;
    }

}
