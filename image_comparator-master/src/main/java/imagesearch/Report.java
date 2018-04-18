package imagesearch;

import imagesearch.image.CustomImageType;

import java.io.Serializable;

public class Report implements Serializable {
    private String report;

    public Report(String report) {
        this.report = report;
    }

    public Report(CustomImageType sourceImage, CustomImageType targetImage) {
        report = "[possibly match] source: "
                + sourceImage.getImageLocation()
                + ","
                + " target: " + targetImage.getImageLocation();
    }

    public String getReport() {
        return report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report1 = (Report) o;

        if (report != null ? !report.equals(report1.report) : report1.report != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return report != null ? report.hashCode() : 0;
    }
}
