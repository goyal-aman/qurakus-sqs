package org.acme.app.repo;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.app.entity.Course;

@ApplicationScoped
public class CourseRepo implements PanacheRepository<Course> {
}
