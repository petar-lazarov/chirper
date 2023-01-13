package net.plazarov.chirper.data.service;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;
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
