package net.plazarov.chirper.views.chirp;

import java.util.Set;

import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject.Expandable;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.ChirpService;
import net.plazarov.chirper.data.service.UserRepository;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.views.profile.ProfileView;

public class ChirpGrid extends VerticalLayout {
	private Set<Chirp> chirps;
	private Grid<Chirp> grid = new Grid<>(Chirp.class, false);
	private final User authenticatedUser;
	private final UserService userService;
	private ChirpService chirpService;
	public ChirpGrid(Set<Chirp> chirps, User authenticatedUser, UserService userService) {
		this.authenticatedUser = authenticatedUser;
		this.chirps = chirps;
		this.userService = userService;
		addClassName("home-view");
        grid.setHeight("100%");
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(chirp -> createCard(chirp));
        grid.setItems(chirps);
        grid.setAllRowsVisible(true);
        add(grid);
        setPadding(true);
	}
	
	public void updateChirps(Set<Chirp> chirps) {
		this.chirps = chirps;
		grid.setItems(chirps);
	}
	
	private HorizontalLayout createCard(Chirp chirp) {
		User user = chirp.getUser();
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");
        
        Anchor name = new Anchor("profile/" + user.getId(), user.getAlias());
        name.getElement().setAttribute("router-ignore", "");
        Span date = new Span(chirp.getCreatedAt().toString());
        date.addClassName("date");
        header.add(name, date);

        Span post = new Span(chirp.getContent());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.HEART.create();
        likeIcon.addClickListener(e -> {
        	if(authenticatedUser.equals(chirp.getUser())) {
        		return;
        	}
        	
        	if(authenticatedUser.getLikedChirps().contains(chirp)) {
            	dislikeChirp(chirp);
        	} else {
            	likeChirp(chirp);        		
        	}
            UI.getCurrent().getPage().reload();
        });
        likeIcon.addClassName("icon");
        Span likes = new Span(Integer.toString(chirp.getLikedUsers().size()));
        likes.addClassName("likes");

        actions.add(likeIcon, likes);

        description.add(header, post, actions);
        card.add(description);
        
        return card;
	}

	private void dislikeChirp(Chirp chirp) {
		chirp.getLikedUsers().remove(authenticatedUser);
		authenticatedUser.getLikedChirps().remove(chirp);
		userService.update(authenticatedUser);
	}

	private void likeChirp(Chirp chirp) {
		authenticatedUser.getLikedChirps().add(chirp);
		chirp.getLikedUsers().add(authenticatedUser);
		userService.update(authenticatedUser);
	}

}
