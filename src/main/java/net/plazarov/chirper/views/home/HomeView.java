package net.plazarov.chirper.views.home;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Set;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.ChirpService;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.security.AuthenticatedUser;
import net.plazarov.chirper.views.LoggedLayout;
import net.plazarov.chirper.views.chirp.ChirpGrid;

@PageTitle("Home | Chirper")
@Route(value = "home", layout = LoggedLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    
    private final AuthenticatedUser authenticatedUser;
    private final ChirpService service;

    public HomeView(AuthenticatedUser authenticatedUser, ChirpService service, @Autowired UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.service = service;
		addClassName("home-view");
		
		H2 title = new H2("What's new");
        ChirpGrid grid = new ChirpGrid(
        		service.findAllByUsers(authenticatedUser.get().get().getFollowedUsers()),
        		authenticatedUser.get().get(),
        		userService,
        		service
        		);
        add(title, grid);
    }
}
