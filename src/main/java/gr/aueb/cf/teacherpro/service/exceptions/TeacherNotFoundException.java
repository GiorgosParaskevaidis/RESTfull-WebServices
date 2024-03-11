package gr.aueb.cf.teacherpro.service.exceptions;

import gr.aueb.cf.teacherpro.model.Teacher;

public class TeacherNotFoundException extends Exception {

    public TeacherNotFoundException(Teacher teacher) {
        super("Teacher with id " + teacher.getId() + " does not exist");
    }

    public TeacherNotFoundException(String s) {
        super(s);
    }
}
