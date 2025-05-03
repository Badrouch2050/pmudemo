package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Date date;
    private int numero ;
    private String name;
    private List<PartantDTO> partants = new ArrayList<>();
}
