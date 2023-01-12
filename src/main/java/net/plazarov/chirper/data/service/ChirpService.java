package net.plazarov.chirper.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import net.plazarov.chirper.data.entity.Chirp;

@Service
public class ChirpService extends AbstractService<Chirp> {

	public ChirpService(ChirpRepository repository) {
		super(repository);
	}

}
