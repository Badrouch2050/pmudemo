package com.pmu.pmudemo.services.impl;

import com.pmu.pmudemo.domains.Course;
import com.pmu.pmudemo.domains.Partant;
import com.pmu.pmudemo.domains.dto.CourseDTO;
import com.pmu.pmudemo.domains.dto.PartantDTO;
import com.pmu.pmudemo.domains.mapper.CourseMapper;
import com.pmu.pmudemo.domains.mapper.PartantMapper;
import com.pmu.pmudemo.repositories.CourseRepository;
import com.pmu.pmudemo.repositories.PartantRepository;
import com.pmu.pmudemo.services.CourseService;
import com.pmu.pmudemo.services.PartantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseRepoImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final PartantRepository partantRepository;
    public CourseRepoImpl(CourseRepository courseRepository, PartantRepository partantRepository) {
        this.courseRepository = courseRepository;
        this.partantRepository = partantRepository;
    }

    @Override
    public void ajout(Course course, List<PartantDTO> partantDTOs) {
        List<Partant> partants =  partantDTOs.stream().map(x-> partantRepository.findByNumero(x.getNumero()).get()).toList();
        course.setPartants(partants);
        courseRepository.save(course);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(CourseMapper::toCourseDTO).collect(Collectors.toList());
    }
}
