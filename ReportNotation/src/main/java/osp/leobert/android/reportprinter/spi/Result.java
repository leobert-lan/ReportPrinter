package osp.leobert.android.reportprinter.spi;



import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Package:</b> osp.leobert.android.reportprinter.spi </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Result </p>
 * Created by leobert on 2018/11/17.
 */
public class Result {

    public static class ResultFile {
        private final String reportContent;
        private final String reportFileNamePrefix;
        private final String fileExt;

        public ResultFile(String reportContent, String reportFileNamePrefix, String fileExt) {
            this.reportContent = reportContent;
            this.reportFileNamePrefix = reportFileNamePrefix;
            this.fileExt = fileExt;
        }

        public String getReportContent() {
            return reportContent;
        }

        public String getReportFileNamePrefix() {
            return reportFileNamePrefix;
        }

        public String getFileExt() {
            return fileExt;
        }
    }

    private final boolean handled;

    @Deprecated
    private final ResultFile compactFile;

    private final List<ResultFile> files;

    public ResultFile getCompactFile() {
        return compactFile;
    }

    public boolean isHandled() {
        return handled;
    }

    @Deprecated
    public String getReportContent() {
        if (compactFile!=null)
            return compactFile.reportContent;
        return null;
    }

    @Deprecated
    public String getReportFileNamePrefix() {
        if (compactFile!=null)
            return compactFile.reportFileNamePrefix;
        return null;
    }

    @Deprecated
    public String getFileExt() {
        if (compactFile!=null)
            return compactFile.fileExt;
        return null;
    }

    public List<ResultFile> getFiles() {
        return files;
    }

    private Result(Builder builder) {
        handled = builder.handled;
        compactFile = builder.compactBuilderHasSet ? new ResultFile(builder.compactBuilder.reportContent,
                builder.compactBuilder.reportFileNamePrefix,
                builder.compactBuilder.fileExt) : null;

        files = builder.files;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private boolean handled;

        @Deprecated
        private final ReportFileBuilder compactBuilder = new ReportFileBuilder(this);

        @Deprecated
        private boolean compactBuilderHasSet = false;

        private final List<ResultFile> files = new ArrayList<>();

        private Builder() {
        }

        public Builder handled(boolean val) {
            handled = val;
            return this;
        }

        public ReportFileBuilder fileBuilder() {
            return new ReportFileBuilder(this);
        }

        @Deprecated
        public Builder reportContent(String val) {
            compactBuilder.reportContent(val);
            compactBuilderHasSet = true;
            return this;
        }

        @Deprecated
        public Builder reportFileNamePrefix(String val) {
            compactBuilder.reportFileNamePrefix(val);
            compactBuilderHasSet = true;
            return this;
        }

        @Deprecated
        public Builder fileExt(String val) {
            compactBuilder.fileExt(val);
            compactBuilderHasSet = true;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    public static class ReportFileBuilder {
        private String reportContent;
        private String reportFileNamePrefix;
        private String fileExt = "txt";

        private final Builder builder;

        public ReportFileBuilder(Builder builder) {
            this.builder = builder;
        }

        public ReportFileBuilder reportContent(String val) {
            reportContent = val;
            return this;
        }

        public ReportFileBuilder reportFileNamePrefix(String val) {
            reportFileNamePrefix = val;
            return this;
        }

        public ReportFileBuilder fileExt(String val) {
            fileExt = val;
            return this;
        }

        public Builder build() {
            ResultFile file = new ResultFile(reportContent, reportFileNamePrefix, fileExt);
            builder.files.add(file);
            return builder;
        }

    }
}
