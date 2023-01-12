package net.plazarov.chirper.data.service;

import java.util.Optional;
import net.plazarov.chirper.data.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User> {
	
	public UserService(UserRepository repository) {
		super(repository);
	}
	
	public User register(User user) {
		return repository.save(user);
	}
}
