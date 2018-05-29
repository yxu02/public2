package edu.sjsu.xuy87.studypal;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteInfo implements writeToParcel {
    private CourseInfo fCourseInfo;
    private String fTitle;
    private String fText;

    public NoteInfo(CourseInfo pCourseInfo, String pTitle, String pText) {
        fCourseInfo = pCourseInfo;
        fTitle = pTitle;
        fText = pText;
    }

    private NoteInfo(Parcel pSource) {
        fCourseInfo = pSource.readParcelable(CourseInfo.class.getClassLoader());
        fTitle = pSource.readString();
        fText = pSource.readString();
    }

    public CourseInfo getCourseInfo() {
        return fCourseInfo;
    }

    public String getTitle() {
        return fTitle;
    }

    public String getText() {
        return fText;
    }

    private void setCourseInfo(CourseInfo pCourseInfo) {
        fCourseInfo = pCourseInfo;
    }

    private void setTitle(String pTitle) {
        fTitle = pTitle;
    }

    private void setText(String pText) {
        fText = pText;
    }

    private String getCompareKey(){
        return fCourseInfo.getCourseId()+ "|" + fTitle + "|" + fText;
    }
    @Override
    public boolean equals(Object pO) {
        if (this == pO) return true;
        if (pO == null || getClass() != pO.getClass()) return false;

        NoteInfo noteInfo = (NoteInfo) pO;

        return getCompareKey().equals(noteInfo.getCompareKey());
    }

    @Override
    public int hashCode() {
        return fCourseInfo.hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeParcelable(fCourseInfo, 0);
        dest.writeString(fTitle);
        dest.writeString((fText));

    }

    public final static Parcelable.Creator<NoteInfo> CREATOR = new Parcelable.Creator<NoteInfo>() {
        @Override
        public NoteInfo createFromParcel(Parcel source) {
            return new NoteInfo(source);
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[0];
        }
    };
}
