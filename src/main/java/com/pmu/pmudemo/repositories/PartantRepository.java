package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.Partant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartantRepository  extends JpaRepository<Partant,Long> {
    Optional<Partant> findByNumero(int numero);
    @Query("SELECT MAX(e.numero) FROM Partant e")
    Integer maxNumroValue();
}
