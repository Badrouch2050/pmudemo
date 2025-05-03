package com.pmu.pmudemo.domains.mapper;

import com.pmu.pmudemo.domains.Course;
import com.pmu.pmudemo.domains.dto.CourseDTO;

import java.util.stream.Collectors;

public class CourseMapper {

    public static Course toCourse(CourseDTO courseDTO){
        Course course = new Course();
        course.setDate(courseDTO.getDate());
        course.setName(courseDTO.getName());
        course.setNumero(courseDTO.getNumero());
        // course.setPartants(courseDTO.getPartants().stream().map(PartantMapper::toPartant).collect(Collectors.toList()));
        return course;
    }

    public static CourseDTO toCourseDTO(Course course){
        CourseDTO coursedto= new CourseDTO();
        coursedto.setDate(course.getDate());
        coursedto.setName(course.getName());
        coursedto.setNumero(course.getNumero());
        coursedto.setPartants(course.getPartants().stream().map(PartantMapper::toPartantDTO).collect(Collectors.toList()));
        return coursedto;
    }



}
