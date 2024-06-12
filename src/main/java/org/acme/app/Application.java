package org.acme.app;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.app.entity.Course;
import org.acme.app.handlers.LogExecutionTime;
import org.acme.app.service.course.CourseService;
import org.acme.app.service.sqs.SQSService;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import java.util.List;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Application {

    SQSService sqsService;
    CourseService courseService;

    @Inject
    public Application(SQSService SQSService, CourseService courseService){
        this.sqsService = SQSService;
        this.courseService = courseService;
    }

    @ConfigProperty(name = "greeting.message")
    String message;

    @GET
    @LogExecutionTime
    public Response allCourse() {
        List<Course> allCourses = courseService.allCourse();
        return Response.ok(allCourses).build();
    }

    @GET
    @Path("/{id}")
    @LogExecutionTime
    public Response getCourse(@PathParam("id") int id) {
        Course course = courseService.getById(id);
        return Response.ok(course).build();
    }
    @POST
    @LogExecutionTime
    public Response addCourse(Course course) {
        courseService.addCourse(course);
        return Response.ok(course).build();
    }

    @PUT
    @LogExecutionTime
    public Response updateCourse(Course course) {
        Course updatedCourse = courseService.updateCourse(course);
        if(updatedCourse==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedCourse).build();
    }

    @DELETE
    @Path("/{id}")
    @LogExecutionTime
    public Response deleteCourse(@PathParam("id") int id) {
        return Response.ok(courseService.deleteCourse(id)).build();
    }
}
