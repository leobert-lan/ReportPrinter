package osp.leobert.android.reportprinter.spi;

/**
 * <p><b>Package:</b> osp.leobert.android.reportprinter.spi </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Result </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/11/17.
 */
public class Result {
    private final boolean handled;
    private final String reportContent;
    private final String reportFileNamePrefix;
    private final String fileExt;

    public boolean isHandled() {
        return handled;
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

    private Result(Builder builder) {
        handled = builder.handled;
        reportContent = builder.reportContent;
        reportFileNamePrefix = builder.reportFileNamePrefix;
        fileExt = builder.fileExt;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private boolean handled;
        private String reportContent;
        private String reportFileNamePrefix;
        private String fileExt = "txt";

        private Builder() {
        }

        public Builder handled(boolean val) {
            handled = val;
            return this;
        }

        public Builder reportContent(String val) {
            reportContent = val;
            return this;
        }

        public Builder reportFileNamePrefix(String val) {
            reportFileNamePrefix = val;
            return this;
        }

        public Builder fileExt(String val) {
            fileExt = val;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }
}
