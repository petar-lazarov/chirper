package net.plazarov.chirper.views.profile;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
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
public class ProfileView extends Div implements HasUrlParameter<Long>, AfterNavigationObserver {
    private final AuthenticatedUser authenticatedUser;
	private User user;
	private UserService userService;
	
	public ProfileView(@Autowired UserService userService, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
		this.userService = userService;
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
	public void afterNavigation(AfterNavigationEvent event) {
		add(new ChirpGrid(user.getChirps()));
	}
}

