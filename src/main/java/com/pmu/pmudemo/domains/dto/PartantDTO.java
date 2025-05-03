package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.Partant;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PartantDTO  {

    @Min(1)
    private int numero ;
    private String name;

}
