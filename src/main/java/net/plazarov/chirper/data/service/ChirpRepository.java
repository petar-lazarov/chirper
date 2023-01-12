package net.plazarov.chirper.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.plazarov.chirper.data.entity.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Long>{

}
