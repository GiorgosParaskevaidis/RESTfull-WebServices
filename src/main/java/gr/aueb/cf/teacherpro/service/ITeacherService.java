package gr.aueb.cf.teacherpro.service;

import gr.aueb.cf.teacherpro.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.teacherpro.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherpro.dto.TeacherUpdateDTO;
import gr.aueb.cf.teacherpro.model.Teacher;
import gr.aueb.cf.teacherpro.service.exceptions.TeacherNotFoundException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO dto) throws TeacherDAOException;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws TeacherDAOException, TeacherNotFoundException;
    void deleteTeacher(Long id) throws TeacherDAOException, TeacherNotFoundException;
    List<Teacher> getTeachersByLastname(String lastname) throws TeacherDAOException;
    Teacher getTeacherById(Long id) throws TeacherDAOException, TeacherNotFoundException;
}
