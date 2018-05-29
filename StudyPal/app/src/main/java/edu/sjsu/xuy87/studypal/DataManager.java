package edu.sjsu.xuy87.studypal;

import java.util.ArrayList;

public class DataManager {
    private static DataManager dm = null;
    ArrayList<NoteInfo> fNotes = new ArrayList<>();
    ArrayList<CourseInfo> fCourses = new ArrayList<>();

    private DataManager() {
    }

    public static DataManager getInstance(){
        if (dm == null){
            dm = new DataManager();
            dm.initiateNotes();
            dm.initiateCourses();
        }
        return dm;
    }

    public int createNewNote(){
        NoteInfo newNote = new NoteInfo(null,null,null);
        fNotes.add(newNote);
        return fNotes.size()-1;
    }

    public void removeNote(int index){
        fNotes.remove(index);
    }

    public ArrayList<NoteInfo> getNotes(CourseInfo course) {
        ArrayList<NoteInfo> notes = new ArrayList<>();
        for(NoteInfo note:fNotes) {
            if(course.equals(note.getCourseInfo()))
                notes.add(note);
        }
        return notes;
    }

    public int getNoteCount(CourseInfo course) {
        int count = 0;
        for(NoteInfo note:fNotes) {
            if(course.equals(note.getCourseInfo()))
                count++;
        }
        return count;
    }

    public int findNote (NoteInfo pNoteInfo){
        for (int i = 0; i < fNotes.size(); i++) {
            if(pNoteInfo.equals(fNotes.get(i)))
                return i;
        }
        return -1;
    }

    public CourseInfo getCourse (String id){
        for (CourseInfo course : fCourses) {
            if(id.equals(course.getCourseId()))
                return course;
        }
        return null;
    }

    public ArrayList<CourseInfo> getCourses() {
        return fCourses;
    }

    private void initiateCourses() {
        fCourses.add(initializeCourse1());
        fCourses.add(initializeCourse2());
        fCourses.add(initializeCourse3());
        fCourses.add(initializeCourse4());
        fCourses.add(initializeCourse5());
    }

    private CourseInfo initializeCourse5() {
        return new CourseInfo("CMPE283", "Virtualization Technologies");

    }

    private CourseInfo initializeCourse4() {
        return new CourseInfo("CMPE282", "Cloud Services");

    }

    private CourseInfo initializeCourse3() {
        return new CourseInfo("CMPE281", "Cloud Technologies");

    }

    private CourseInfo initializeCourse2() {
        return new CourseInfo("CMPE277", "Mobile Application Development");
    }

    private CourseInfo initializeCourse1() {
        return new CourseInfo("CMPE272", "Enterprise Software Platforms");
    }

    private void initiateNotes() {
        final DataManager dataManager = getInstance();

        CourseInfo course = dataManager.getCourse("CMPE272");
        fNotes.add(new NoteInfo(course, "Enterprise Software Platforms",
                "This course covers enterprise software, system and virtualized platforms"));

        course = dataManager.getCourse("CMPE277");
        fNotes.add(new NoteInfo(course, "Mobile Application Development",
                "This course covers architectures, technologies, and programming concepts for developing smartphone applications."));

        course = dataManager.getCourse("CMPE281");
        fNotes.add(new NoteInfo(course, "Cloud Technologies",
                "This course covers cloud computing concepts, evolution, architectures, infrastructures, opportunities, risks, enterprise adoption strategies, standards and polices."));

        course = dataManager.getCourse("CMPE282");
        fNotes.add(new NoteInfo(course, "Cloud Services",
                "This course covers cloud service architecture and layering, administrative issues, resiliency and security considerations."));

        course = dataManager.getCourse("CMPE283");
        fNotes.add(new NoteInfo(course, "Virtualization Technologies",
                "Virtualization concepts, components and infrastructure, hardware and software virtualization, virtualization machine life cycle management, virtualization services."));
    }
}
