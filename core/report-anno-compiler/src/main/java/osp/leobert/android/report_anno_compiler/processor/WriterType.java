package osp.leobert.android.report_anno_compiler.processor;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> WriterType </p>
 * <p><b>Description:</b> WriterType </p>
 * Created by leobert on 2018/12/7.
 */
enum WriterType {
    Filer(Consts.WRITER_FILER),Custom(Consts.WRITER_CUSTOM);
    final String type;

    WriterType(String type) {
        this.type = type;
    }

    public static WriterType customValueOf(String mode) {
        if (Custom.type.equals(mode))
            return Custom;
        return Filer;
    }
}
