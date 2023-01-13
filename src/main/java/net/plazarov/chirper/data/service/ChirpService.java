package net.plazarov.chirper.data.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;

@Service
@Transactional
public class ChirpService extends AbstractService<Chirp> {
    protected ChirpRepository repository;

	public ChirpService(ChirpRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	public List<Chirp> findAll() {
		return repository.findAll();
	}
	
	public Set<Chirp> findAllByUsers(Set<User> users) {
		Set<Long> userIds = new HashSet<>();
		users.forEach(user -> {
			userIds.add(user.getId());
		});
		
		return repository.findAllByUsers(userIds);
	}
	
	public Set<Chirp> findAllByContentAndUsername(String term) {
		return repository.findAllByContentAndUsername(term);
	}
}
