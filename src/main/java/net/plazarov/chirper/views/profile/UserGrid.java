package net.plazarov.chirper.views.profile;

import java.util.Set;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.UserService;

public class UserGrid extends VerticalLayout {
	private Set<User> users;
	private Grid<User> grid = new Grid<>();
	private final User authenticatedUser;
	private final UserService userService;

	public UserGrid(Set<User> users, User authenticatedUser, UserService userService) {
		this.users = users;
		this.authenticatedUser = authenticatedUser;
		this.userService = userService;
		addClassName("home-view");
		if(users.size() == 0) {
        	Paragraph message = new Paragraph("No Users here.");
        	add(message);
		} else {
	        grid.setHeight("100%");
	        grid.setSelectionMode(SelectionMode.NONE);
	        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
	        grid.addComponentColumn(user -> createCard(user));
	        grid.setItems(users);
	        grid.setAllRowsVisible(true);
	        add(grid);
		}
        setPadding(true);

	}

	private VerticalLayout createCard(User user) {
		VerticalLayout card = new VerticalLayout();
        card.addClassName("card");
		HorizontalLayout userInfo = new HorizontalLayout();
        Anchor name = new Anchor("profile/" + user.getId(), user.getAlias());
        name.getElement().setAttribute("router-ignore", "");
        Span date = new Span(user.getCreatedAt().toString());
        date.addClassName("date");
        userInfo.add(name, date);
        Span bio = new Span(user.getBio());
        bio.addClassName("post");
        
        card.add(userInfo, bio);
        
        return card;
	}
}
