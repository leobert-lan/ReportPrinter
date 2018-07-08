package osp.leobert.android.report_anno_compiler.bean;

import javax.lang.model.element.ElementKind;

import osp.leobert.android.reportprinter.notation.State;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.bean </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Issue </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
public class Issue {
    private State state;
    private String desc;
    private String simpleName;
    private ElementKind kind;

    public Issue() {
    }

    public Issue(State state, String desc, String simpleName, ElementKind kind) {
        this.state = state;
        this.desc = desc;
        this.simpleName = simpleName;
        this.kind = kind;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        return kind.toString() + " :" + simpleName + "\r\n"
                + desc + "\r\n" + state + "\n\n";
    }
}
