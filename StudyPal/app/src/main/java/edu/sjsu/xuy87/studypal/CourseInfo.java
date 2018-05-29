package edu.sjsu.xuy87.studypal;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseInfo implements Parcelable {
    private final String fCourseId;
    private final String fTitle;

    public CourseInfo(String pCourseId, String pTitle) {
        fCourseId = pCourseId;
        fTitle = pTitle;
    }

    public String getCourseId() {
        return fCourseId;
    }

    public String getTitle() {
        return fTitle;
    }

    @Override
    public String toString() {
        return fTitle;
    }

    @Override
    public boolean equals(Object pO) {
        if (this == pO) return true;
        if (pO == null || getClass() != pO.getClass()) return false;

        CourseInfo that = (CourseInfo) pO;

        return fCourseId.equals(that.fCourseId);
    }

    @Override
    public int hashCode() {
        return fCourseId.hashCode();
    }

    private CourseInfo(Parcel in) {
        fCourseId = in.readString();
        fTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel in) {
            return new CourseInfo(in);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fCourseId);
        dest.writeString(fTitle);
    }
}
