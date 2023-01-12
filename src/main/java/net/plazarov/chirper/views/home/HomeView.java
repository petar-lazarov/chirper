package net.plazarov.chirper.views.home;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Set;

import javax.annotation.security.PermitAll;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.security.AuthenticatedUser;
import net.plazarov.chirper.views.LoggedLayout;
import net.plazarov.chirper.views.chirp.ChirpGrid;

@PageTitle("Home | Chirper")
@Route(value = "home", layout = LoggedLayout.class)
@PermitAll
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 1L;
    
    private final AuthenticatedUser authenticatedUser;

    public HomeView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
		addClassName("home-view");
    }

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
        	Set<Chirp> chirps = authenticatedUser.get().get().getChirps();
            event.forwardTo("home");
        }
	}
}
