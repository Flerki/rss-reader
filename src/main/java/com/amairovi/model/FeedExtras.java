package com.amairovi.model;

import java.io.Serializable;
import java.time.Period;
import java.util.Objects;

public class FeedExtras implements Serializable {
    private Period surveyPeriod;
    private String filename;
    private int amountOfElementsAtOnce;

    public FeedExtras() {
    }

    public FeedExtras(Period surveyPeriod, String filename, int amountOfElementsAtOnce) {
        this.surveyPeriod = surveyPeriod;
        this.filename = filename;
        this.amountOfElementsAtOnce = amountOfElementsAtOnce;
    }

    public Period getSurveyPeriod() {
        return surveyPeriod;
    }

    public void setSurveyPeriod(Period surveyPeriod) {
        this.surveyPeriod = surveyPeriod;
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
                Objects.equals(surveyPeriod, that.surveyPeriod) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyPeriod, filename, amountOfElementsAtOnce);
    }
}
