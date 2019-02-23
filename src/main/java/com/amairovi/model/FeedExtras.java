package com.amairovi.model;

import java.io.Serializable;
import java.util.Objects;

public class FeedExtras implements Serializable {
    private long surveyPeriodInMs;
    private String filename;
    private int amountOfElementsAtOnce;

    public long getSurveyPeriod() {
        return surveyPeriodInMs;
    }

    public void setSurveyPeriod(long surveyPeriodInMs) {
        this.surveyPeriodInMs = surveyPeriodInMs;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getAmountOfElementsAtOnce() {
        return amountOfElementsAtOnce;
    }

    public void setAmountOfElementsAtOnce(int amountOfElementsAtOnce) {
        this.amountOfElementsAtOnce = amountOfElementsAtOnce;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedExtras that = (FeedExtras) o;
        return amountOfElementsAtOnce == that.amountOfElementsAtOnce &&
                Objects.equals(surveyPeriodInMs, that.surveyPeriodInMs) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyPeriodInMs, filename, amountOfElementsAtOnce);
    }
}
