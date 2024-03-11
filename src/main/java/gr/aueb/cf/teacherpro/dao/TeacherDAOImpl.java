package gr.aueb.cf.teacherpro.dao;

import gr.aueb.cf.teacherpro.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.teacherpro.model.Teacher;
import gr.aueb.cf.teacherpro.service.util.DBUtil;
import jakarta.ws.rs.ext.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Provider
public class TeacherDAOImpl implements ITeacherDAO {
    @Override
    public Teacher insert(Teacher teacher) throws TeacherDAOException {
        String sql = "INSERT INTO TEACHERS (SSN, FIRSTNAME, LASTNAME) VALUES (?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String ssn = teacher.getSsn();
            String firstname = teacher.getFirstname();
            String lastname = teacher.getLastname();
            ps.setString(1, ssn);
            ps.setString(2, firstname);
            ps.setString(3, lastname);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            Long generatedId = 0L;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);
            }
            teacher.setId(generatedId);
            generatedKeys.close();
        } catch (SQLException e1) {
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher" + teacher + " insertion");
        }
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher) throws TeacherDAOException {
        String sql = "UPDATE TEACHERS SET SSN = ?, FIRSTNAME = ?, LASTNAME = ? WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            Long id = teacher.getId();
            String ssn = teacher.getSsn();
            String firstname = teacher.getFirstname();
            String lastname = teacher.getLastname();

            ps.setString(1, ssn);
            ps.setString(2, firstname);
            ps.setString(3, lastname);
            ps.setLong(4, id);

            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
            return teacher;
        } catch (SQLException e1) {
            //e1.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher" + teacher + " insertion");
        }
    }

    @Override
    public void delete(Long id) throws TeacherDAOException {
        String sql = "DELETE FROM TEACHERS WHERE ID = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            DBUtil.beginTransaction();
            ps.executeUpdate();
            DBUtil.commitTransaction();
        } catch (SQLException e) {
                //e.printStackTrace();
            DBUtil.rollbackTransaction();
            throw new TeacherDAOException("SQL Error in Teacher with id: " + id + " deletion");
        }
    }

    @Override
    public List<Teacher> getByLastname(String lastname) throws TeacherDAOException {
        String sql = "SELECT * FROM TEACHERS WHERE LASTNAME LIKE ?";
        List<Teacher> teachers = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ) {
            ResultSet rs;
            ps.setString(1, lastname + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Teacher teacher = new Teacher(rs.getLong("ID"),rs.getString("SSN"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"));
                teachers.add(teacher);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new TeacherDAOException("SQL Error in get teacher with lastname: " + lastname);
        }

        return teachers;
    }

    @Override
    public Teacher getById(Long id) throws TeacherDAOException {
        String sql = "SELECT * FROM TEACHERS WHERE ID = ?";
        Teacher teacher = null;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs;
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                teacher = new Teacher(rs.getLong("ID"),rs.getString("SSN"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return teacher;
    }
}
