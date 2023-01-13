package net.plazarov.chirper.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.plazarov.chirper.data.Role;

@Entity
@Table
public class User extends AbstractEntity {

	@Column(length = 60, nullable = false, unique = true)
	@NotBlank
    private String username;

    @JsonIgnore
    @Column
    private String hashedPassword;
	
	@Column(length = 255, nullable = false, unique = true)
	@Email
	@NotBlank
	private String email;
	
	@Column(length = 60, nullable = false)
	@NotBlank
    private String alias;
	
	@Column(length = 255)
	private String bio;
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Chirp.class, mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Chirp> chirps = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinTable(
			name = "user_likes",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "chirp_id", referencedColumnName = "id")
	)
	private Set<Chirp> likedChirps;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinTable(
			name = "user_followers",
			joinColumns = @JoinColumn(name = "followed_user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
	)
	private Set<User> followers;
	
	@ManyToMany(mappedBy = "followers", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<User> followedUsers;
	    
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    
    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }

    
    public String getHashedPassword() {
        return hashedPassword;
    }
    
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = new BCryptPasswordEncoder(10, new SecureRandom()).encode(hashedPassword);
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public byte[] getProfilePicture() {
    	return profilePicture;
    }
    
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Chirp> getChirps() {
    	return chirps;
    }
    
    public void setChirps(Set<Chirp> chirps) {
    	this.chirps = chirps;
    }
    
    public Set<Chirp> getLikedChirps() {
    	return likedChirps;
    }
    
    public void setLikedChirps(Set<Chirp> likedChirps) {
    	this.likedChirps = likedChirps;
    }
    
    public Set<User> getFollowers() {
    	return this.followers;
    }
    
    public void setFollowers(Set<User> followers) {
    	this.followers = followers;
    }
    
    public Set<User> getFollowedUsers() {
    	return this.followedUsers;
    }
    
    public void setFollwedUsers(Set<User> followedUsers) {
    	this.followedUsers = followedUsers;
    }
}
