package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.Course;
import com.pmu.pmudemo.domains.dto.CourseDTO;
import com.pmu.pmudemo.domains.dto.PartantDTO;

import java.util.List;

public interface CourseService {
    void ajout(Course course, List<PartantDTO> partantDTOss);
    List<CourseDTO> getAllCourses();
}
