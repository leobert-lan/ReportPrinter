package osp.leobert.android.report_anno_compiler.bean;

import java.util.Arrays;

import javax.lang.model.element.ElementKind;

import osp.leobert.android.reportprinter.notation.State;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.bean </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ChangeLogItem </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
public class ChangeLogItem {
    private String version;
    private String[] changes;
    private String simpleName;
    private ElementKind kind;

    public ChangeLogItem() {
    }

    public ChangeLogItem(String version, String[] changes, String simpleName, ElementKind kind) {
        this.version = version;
        this.changes = changes;
        this.simpleName = simpleName;
        this.kind = kind;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getChanges() {
        return changes;
    }

    public void setChanges(String[] changes) {
        this.changes = changes;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return version+ "\r\n"+kind.toString() + " :" + simpleName + "\r\n"
                + Arrays.toString(changes)  + "\n\n";
    }
}
