package gr.aueb.cf.teacherpro.restcontroller;

import gr.aueb.cf.teacherpro.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.teacherpro.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherpro.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.teacherpro.dto.TeacherUpdateDTO;
import gr.aueb.cf.teacherpro.model.Teacher;
import gr.aueb.cf.teacherpro.service.ITeacherService;
import gr.aueb.cf.teacherpro.service.exceptions.TeacherNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.Objects;

@Path("/teachers")
public class TeacherRestController {

    @Inject
    private ITeacherService teacherService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeachersByLastname(@QueryParam("lastname") String lastname) {
        List<Teacher> teachers;

        try {
            teachers = teacherService.getTeachersByLastname(lastname);
            if (teachers.size() == 0) {
             return Response.status(Response.Status.BAD_REQUEST).entity("BAD REQUEST").build();
            }
            return Response.status(Response.Status.OK).entity(teachers).build();
        } catch (TeacherDAOException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneTeacher(@PathParam("id") Long id) {
        Teacher teacher;

        try {
            teacher = teacherService.getTeacherById(id);
            return Response.status(Response.Status.OK).entity(teacher).build();
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeacher(TeacherInsertDTO dto, @Context UriInfo uriInfo) {

        try {
            Teacher teacher = teacherService.insertTeacher(dto);
            if (teacher == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("BAD REQUEST").build();
            }

            TeacherReadOnlyDTO readOnlyDTO = new TeacherReadOnlyDTO(teacher.getId(), teacher.getSsn(), teacher.getFirstname(), teacher.getLastname());
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            return Response.status(Response.Status.CREATED)
                    .entity(readOnlyDTO)
                    .location(uriBuilder.path(Long.toString(readOnlyDTO.getId())).build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("id") Long id, TeacherUpdateDTO dto) {

        if (!Objects.equals(dto.getId(), id)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        try {
            Teacher teacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = new TeacherReadOnlyDTO(teacher.getId(), teacher.getSsn(),
                    teacher.getFirstname(), teacher.getLastname());
            return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTeacher(@PathParam("id") Long id) {
        Teacher teacher;

        try {
            teacher = teacherService.getTeacherById(id);
            teacherService.deleteTeacher(id);
            TeacherReadOnlyDTO readOnlyDTO = new TeacherReadOnlyDTO(teacher.getId(), teacher.getSsn(),
                    teacher.getFirstname(), teacher.getLastname());
            return Response.status(Response.Status.OK).entity(readOnlyDTO).build();
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
