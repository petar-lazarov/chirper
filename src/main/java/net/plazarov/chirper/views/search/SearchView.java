package net.plazarov.chirper.views.search;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.service.ChirpService;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.security.AuthenticatedUser;
import net.plazarov.chirper.views.LoggedLayout;
import net.plazarov.chirper.views.chirp.ChirpGrid;

@PageTitle("Home | Chirper")
@Route(value = "search", layout = LoggedLayout.class)
@PermitAll
public class SearchView extends VerticalLayout {
	private final AuthenticatedUser authenticatedUser;
	private TextField searchTerm;
	private Button searchButton;
	private ChirpGrid grid;
	private ChirpService service;
	
	public SearchView(AuthenticatedUser authenticatedUser, ChirpService service, UserService userService) {
		this.authenticatedUser = authenticatedUser;
		this.service = service;
		H2 title = new H2("Search");
		HorizontalLayout layout = new HorizontalLayout();
		searchTerm = new TextField();
		searchTerm.getElement().setAttribute("aria-label", "search");
		searchTerm.setPlaceholder("search by content or username");
		searchTerm.setClearButtonVisible(true);
		searchTerm.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchButton = new Button("Search", e -> updateChirpGrid());

		layout.add(searchTerm, searchButton);
		grid = new ChirpGrid(new HashSet<Chirp>(), authenticatedUser.get().get(), userService);
		add(title, layout, grid);
	}

	private void updateChirpGrid() {
		if(searchTerm.getValue().isEmpty()) {
			grid.updateChirps(new HashSet<Chirp>());
		} else {
			grid.updateChirps(service.findAllByContentAndUsername(searchTerm.getValue()));
		}
	}
}
