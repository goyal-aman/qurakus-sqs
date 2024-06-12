package org.acme.app.service.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.app.entity.Course;
import org.acme.app.repo.CourseRepo;

import java.util.List;

@ApplicationScoped
public class CourseService {

    CourseRepo courseRepo;

    @Inject
    public CourseService(CourseRepo courseRepo){
        this.courseRepo = courseRepo;
    }

    public List<Course> allCourse() {
        return courseRepo.findAll().stream().toList();
    }

    @Transactional
    public void addCourse(Course course) {
        courseRepo.persist(course);
    }


    @Transactional
    public Course updateCourse(Course course) {

        int count = 0;

        Course existingCourse = courseRepo.findById((long) course.getId());

        if(existingCourse == null){
            return null;
        }

        if(course.getDescription()!=null){
            existingCourse.setDescription(course.getDescription());
        }

        if(course.getName()!=null){
            existingCourse.setName(course.getName());
        }

        existingCourse.persist();
        return existingCourse;

    }

    @Transactional
    public boolean deleteCourse(int id) {
        return courseRepo.deleteById((long)id);
    }

    public Course getById(int id) {
        return courseRepo.findById((long) id);
    }
}
