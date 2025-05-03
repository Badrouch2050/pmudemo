package com.pmu.pmudemo.services.impl;

import com.pmu.pmudemo.domains.Partant;
import com.pmu.pmudemo.repositories.PartantRepository;
import com.pmu.pmudemo.services.PartantService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PartantServiceImp implements PartantService {

    //  private static int incr_num = 0;
    private final PartantRepository partantRepository;

    public PartantServiceImp(PartantRepository partantRepository) {
        this.partantRepository = partantRepository;
    }

    @Override
    @Transactional
    public void ajout(Partant partant) {
        partant.setNumero(null == partantRepository.maxNumroValue()  ? 1 : partantRepository.maxNumroValue() + 1);
        partantRepository.save(partant);
    }

}
