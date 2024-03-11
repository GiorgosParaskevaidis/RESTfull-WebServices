package gr.aueb.cf.teacherpro.dao;

import gr.aueb.cf.teacherpro.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.teacherpro.model.Teacher;

import java.util.List;

public interface ITeacherDAO {
    Teacher insert(Teacher teacher) throws TeacherDAOException;
    Teacher update(Teacher teacher) throws TeacherDAOException;
    void delete(Long id) throws TeacherDAOException;
    List<Teacher> getByLastname(String lastname) throws TeacherDAOException;
    Teacher getById(Long id) throws TeacherDAOException;
}
