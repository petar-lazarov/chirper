package net.plazarov.chirper.data.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
public class Chirp extends AbstractEntity {
	@Column(name="content")
	private String content;
	
	@ManyToOne
	private User user;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "likedChirps")
	private Set<User> likedUsers;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Set<User> getLikedUsers() {
		return likedUsers;
	}
	
	public void setLikedUsers(Set<User> likedUsers) {
		this.likedUsers = likedUsers;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
