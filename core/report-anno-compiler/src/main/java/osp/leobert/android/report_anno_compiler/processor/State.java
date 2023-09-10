package osp.leobert.android.report_anno_compiler.processor;

/*
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> State </p>
 * <p><b>Description:</b> State </p>
 * Created by leobert on 2018/12/7.
 */
enum State {
    On("on"),Off("off");

    final String state;

    State(String state) {
        this.state = state;
    }

    public static State customValueOf(String mode) {
        if (On.state.equals(mode))
            return On;
        return Off;
    }
}
