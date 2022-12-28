package osp.leobert.android.report_anno_compiler.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.lang.model.element.Modifier;

/**
 * <p><b>Package:</b> osp.leobert.android.issueprint.utils </p>
 * <p><b>Project:</b> IssuePrinter </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> Utils </p>
 * Created by leobert on 13/04/2018.
 */
/*public*/ class Utils {
    static String firstCharUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    static String genFileName(String type, String ext) {
        return firstCharUpperCase(type) + "Report." + ext;
    }

    static String genReporterClzName(String type) {
        return firstCharUpperCase(type) + "Reporter";
    }

    static String generateReportFilePath(String type, String ext) {
        return "./Reports/" + firstCharUpperCase(type) + "Report." + ext;
    }


    static boolean createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    static void writeStringToFile(String fileName, String content, boolean append) {
        BufferedWriter out = null;
        OutputStreamWriter osw = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName, append);
            osw = new OutputStreamWriter(fileOutputStream, "UTF-8");
            out = new BufferedWriter(osw);
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(osw);
            close(fileOutputStream);
        }
    }

    private static void close(java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String print_method_codes =
            "String path = folder + \"/\" + fileName;\n" +
                    "        java.io.BufferedWriter out = null;\n" +
                    "        java.io.OutputStreamWriter osw = null;\n" +
                    "        java.io.FileOutputStream fileOutputStream = null;\n" +
                    "        java.io.File file = new java.io.File(path);\n" +
                    "        if (!file.getParentFile().exists()) {\n" +
                    "            file.getParentFile().mkdirs();\n" +
                    "        }\n" +
                    "        try {\n" +
                    "            if (!file.exists())\n" +
                    "                file.createNewFile();\n" +
                    "            fileOutputStream = new java.io.FileOutputStream(path, false);\n" +
                    "            osw = new java.io.OutputStreamWriter(fileOutputStream, \"UTF-8\");\n" +
                    "            out = new java.io.BufferedWriter(osw);\n" +
                    "            out.write(content);\n" +
                    "        } catch (Exception e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        } finally {\n" +
                    "            try {\n" +
                    "                if (out != null)\n" +
                    "                    out.close();\n" +
                    "                if (osw != null)\n" +
                    "                    osw.close();\n" +
                    "                if (fileOutputStream != null)\n" +
                    "                    fileOutputStream.close();\n" +
                    "            } catch (java.io.IOException e) {\n" +
                    "                e.printStackTrace();\n" +
                    "            }\n" +
                    "        }";

    static void generatePrinterClass(String clzName, String fileName, String content, Writeable writeable) {

        MethodSpec printMethodSpec = MethodSpec.methodBuilder("print")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "folder")
                .addCode(print_method_codes)
                .build();

        FieldSpec fileNameFieldSpec = FieldSpec.builder(String.class, "fileName")
                .addModifiers(Modifier.STATIC)
                .initializer("$S", fileName)
                .build();

        FieldSpec contentFieldSpec = FieldSpec.builder(String.class, "content")
                .addModifiers(Modifier.STATIC)
                .initializer("$S", content)
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(clzName)
                .addModifiers(Modifier.PUBLIC)
                .addField(fileNameFieldSpec)
                .addField(contentFieldSpec)
                .addMethod(printMethodSpec)
                .build();


        JavaFile reportClass = JavaFile.builder("reporter"
                , typeSpec)
                .addFileComment("generate by reporter")
                .build();

        writeable.write(reportClass);

    }
}
