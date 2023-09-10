package osp.leobert.android.report_anno_compiler.processor;

import com.squareup.javapoet.JavaFile;

import java.io.File;
import java.io.IOException;

import javax.annotation.processing.Filer;

/*
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Writeable </p>
 * Created by leobert on 2018/12/3.
 */
public interface Writeable {
    boolean write(JavaFile javaFile);

    class DirectionWriter implements Writeable {
        static Writeable of(File direction) {
            return new DirectionWriter(direction);
        }

        final File file;

        DirectionWriter(File file) {
            this.file = file;
        }

        @Override
        public boolean write(JavaFile javaFile) {

            try {
                javaFile.writeTo(file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    class FilerWriter implements Writeable {
        public static Writeable of(Filer filer) {
            return new FilerWriter(filer);
        }

        final Filer filer;

        FilerWriter(Filer filer) {
            this.filer = filer;
        }

        @Override
        public boolean write(JavaFile javaFile) {
            try {
                javaFile.writeTo(filer);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
