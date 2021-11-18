package osp.leobert.android.kotlin_sample.diagram;

import osp.leobert.android.reporter.diagram.notation.ClassDiagram;
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram;
import osp.leobert.android.reporter.diagram.notation.Visible;

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> AAAB </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/23.
 */
@ClassDiagram(
        qualifier = "AAAB",
        fieldVisible = {Visible.Package, Visible.Public}
)
public @interface AAAB {
}

@GenerateClassDiagram
@AAAB
class SealedClz {
    private int a;

    int b;

    public int c;

    void m() {
        System.out.print(Enum.E1.getA());
    }
}
