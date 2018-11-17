package osp.leobert.android.reportprinter.notation;

/**
 * <p><b>Package:</b> osp.leobert.android.reportprinter.notation </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Bug </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/7.
 */
public @interface Bug {
    State state() default State.TODO;

    String desc();
}
