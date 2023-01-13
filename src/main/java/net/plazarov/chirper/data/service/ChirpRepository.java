package net.plazarov.chirper.data.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Long> {
	@Query( "select chirp from Chirp chirp where user_id in :ids" )
	public Set<Chirp> findAllByUsers(@Param("ids") Set<Long> userIds);
	
	@Query( "select c from Chirp c join c.user u where c.content like %:term% or u.username like %:term%")
	public Set<Chirp> findAllByContentAndUsername(@Param("term") String term);
}
