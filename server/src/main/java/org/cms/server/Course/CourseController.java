package org.cms.server.Course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CourseController {

    @RequestMapping("/courses")
    public List<Course> getAllCourses(){
        return Arrays.asList(
                new Course(1,"Spring Framework","Spring Framework Description"),
                new Course(2,"Maven ","Maven is a build tool"),
                new Course(3,"CICD  ","Jenkins is a CICD pipelining tool")

        );
    }
}
