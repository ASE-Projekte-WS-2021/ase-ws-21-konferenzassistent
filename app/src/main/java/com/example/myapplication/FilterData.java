package com.example.myapplication;

import java.util.List;

public class FilterData {

    private boolean shouldFilter;
    private List<String> peopleList;
    private int minCount, maxCount;
    private String location;
    private Long dateStart, dateEnd;

    public FilterData(boolean shouldFilter) {
        this.shouldFilter = shouldFilter;
        this.peopleList = null;
        this.dateStart = null;
        this.dateEnd = null;
    }

    public boolean isShouldFilter() {
        return shouldFilter;
    }

    public void setShouldFilter(boolean shouldFilter) {
        this.shouldFilter = shouldFilter;
    }

    public List<String> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<String> peopleList) {
        this.peopleList = peopleList;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return "FilterData{" +
                "shouldFilter=" + shouldFilter +
                ", peopleList=" + peopleList +
                ", minCount=" + minCount +
                ", maxCount=" + maxCount +
                ", location='" + location + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }
}
