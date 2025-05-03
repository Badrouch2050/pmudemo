package com.pmu.pmudemo.domains.mapper;

import com.pmu.pmudemo.domains.Partant;
import com.pmu.pmudemo.domains.dto.PartantDTO;

public class PartantMapper {

    public static Partant toPartant(PartantDTO partantDTO){
            Partant partant = new Partant();
            partant.setName(partantDTO.getName());
            partant.setNumero(partantDTO.getNumero());
            return partant;
    }

    public static PartantDTO toPartantDTO(Partant partant){
        return  new PartantDTO(partant.getNumero(),partant.getName());
    }


}
