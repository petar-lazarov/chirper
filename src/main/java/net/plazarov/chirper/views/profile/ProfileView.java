package net.plazarov.chirper.views.profile;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.security.AuthenticatedUser;
import net.plazarov.chirper.views.LoggedLayout;
import net.plazarov.chirper.views.chirp.ChirpGrid;

@PageTitle("Card List")
@PermitAll
@Route(value = "profile", layout = LoggedLayout.class)
public class ProfileView extends VerticalLayout implements HasUrlParameter<Long>, BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;
	private User user;
	private UserService userService;
	
	public ProfileView(@Autowired UserService userService, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
		this.userService = userService;
	}
	
	public TabSheet getTabs() {
		TabSheet tabs = new TabSheet();
		tabs.setWidthFull();
		Tab chirpsTab = new Tab(new Span("Chirps"), createBadge(user.getChirps().size()));
		tabs.add(chirpsTab, new ChirpGrid(user.getChirps(), authenticatedUser.get().get(), userService));
		Tab followersTab = new Tab(new Span("Followers"), createBadge(user.getFollowers().size()));
		tabs.add(followersTab, new UserGrid(user.getFollowers(), authenticatedUser.get().get(), userService));
		Tab folloingTab = new Tab(new Span("Following"), createBadge(user.getFollowedUsers().size()));
		tabs.add(folloingTab, new UserGrid(user.getFollowedUsers(), authenticatedUser.get().get(), userService));
		return tabs;
	}

	@Override
	public void setParameter(BeforeEvent event, Long id) {
		Optional<User> maybeUser = userService.get(id);
		if(maybeUser.isEmpty()) {
			UI.getCurrent().navigate("home");
			Notification.show("No such user!");
			return;
		}
		
		user = maybeUser.get();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		VerticalLayout layout = new VerticalLayout();
		layout.add(getHeader());
		layout.add(getTabs());
		add(layout);
	}

	private VerticalLayout getHeader() {
		VerticalLayout header = new VerticalLayout();
		HorizontalLayout userData = new HorizontalLayout();
		userData.setAlignItems(Alignment.CENTER);
		User loggedUser = authenticatedUser.get().get();
		Span name = new Span(user.getAlias() + " (@" + user.getUsername() + ")");
		name.addClassName("bold");
		Span createdAt = new Span(user.getCreatedAt().toString());
		userData.add(name, createdAt);

		if (!loggedUser.equals(user)) {
			Button followButton = new Button();

			if (user.getFollowers().contains(loggedUser)) {
				followButton.setText("Unfollow");
				followButton.addClickListener(e -> {
					user.getFollowers().remove(loggedUser);
					loggedUser.getFollowedUsers().remove(user);
					userService.update(user);
					UI.getCurrent().getPage().reload();
				});
			} else {
				followButton.setText("Follow");
				followButton.addClickListener(e -> {
					user.getFollowers().add(loggedUser);
					loggedUser.getFollowedUsers().add(user);
					userService.update(user);
					UI.getCurrent().getPage().reload();
				});
			}
			userData.add(followButton);
		}
		Span bio = new Span(user.getBio());
		header.add(userData, bio);
		return header;
	}
	
    private Span createBadge(int value) {
        Span badge = new Span(String.valueOf(value));
        badge.getElement().getThemeList().add("badge small contrast");
        badge.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        return badge;
    }

}

