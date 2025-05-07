package com.pmu.pmudemo.Controller;



import com.pmu.pmudemo.domains.dto.CourseDTO;
import com.pmu.pmudemo.domains.dto.PartantDTO;
import com.pmu.pmudemo.domains.mapper.CourseMapper;
import com.pmu.pmudemo.domains.mapper.PartantMapper;
import com.pmu.pmudemo.services.CourseService;
import com.pmu.pmudemo.services.PartantService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class PMUController {

    private final PartantService partantService;
    private final CourseService courseService;


    public PMUController(PartantService partantService, CourseService courseService) {
        this.partantService = partantService;
        this.courseService = courseService;
    }


    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }


    @PostMapping("/addcourse")
    public void addCourse(@RequestBody CourseDTO course, HttpServletResponse response) {
        courseService.ajout(CourseMapper.toCourse(course), course.getPartants() );
    }

    @PostMapping("/addpartant")
    public void addPartant(@RequestBody PartantDTO partant, HttpServletResponse response) {
        partantService.ajout(PartantMapper.toPartant(partant));
    }

    @GetMapping("/courses")
    public List<CourseDTO> getAllCourse(HttpServletResponse response) {
        return courseService.getAllCourses();
    }


}
