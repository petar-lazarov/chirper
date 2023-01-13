package net.plazarov.chirper.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import java.io.ByteArrayInputStream;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import net.plazarov.chirper.data.entity.Chirp;
import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.ChirpService;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.security.AuthenticatedUser;
import net.plazarov.chirper.views.home.HomeView;
import net.plazarov.chirper.views.search.SearchView;
import net.plazarov.chirper.views.explore.ExploreView;
import net.plazarov.chirper.views.welcome.WelcomeView;

public class LoggedLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

		public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                addClassNames(FontSize.LARGE, TextColor.SECONDARY);
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private AuthenticatedUser authenticatedUser;
    private final Dialog addChirpDialog;
    private final ChirpService chirpService;


    public LoggedLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, @Autowired ChirpService chirpService) {
        this.authenticatedUser = authenticatedUser;
    	addChirpDialog = createAddChirpDialog();
		this.chirpService = chirpService;
    	addToDrawer(addChirpDialog);
        addToDrawer(createAddChirpButton());
        addToDrawer(getTabs());
        addToNavbar(createHeaderContent());
    }
    
    private Dialog createAddChirpDialog() {
    	Dialog addChirpDialog = new Dialog(); 
    	addChirpDialog.setHeaderTitle("Add chirp");
    	BeanValidationBinder binder = new BeanValidationBinder<>(Chirp.class);

    	TextArea content = new TextArea();
    	binder.forField(content).asRequired().bind("content");
    	addChirpDialog.add(content);
    	
    	Button saveButton = new Button("Save", e -> {
    		try {
        		Chirp chirp = new Chirp();
        		chirp.setUser(authenticatedUser.get().get());
				binder.writeBean(chirp);
				chirpService.update(chirp);
				addChirpDialog.close();	
			} catch (ValidationException e1) {
				e1.printStackTrace();
			}
    	});
    	saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	
    	addChirpDialog.getFooter().add(saveButton);
		return addChirpDialog;

	}

	private Component createAddChirpButton() {
    	Button addChirpButton = new Button("Create Chirp", e -> {
    		addChirpDialog.open();
    	});
    	addChirpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		return addChirpButton;
	}

	private Tabs getTabs() {
    	Tabs tabs = new Tabs();
    	tabs.add(createTab("Home", HomeView.class));
    	tabs.add(createTab("Explore", ExploreView.class));
    	tabs.add(createTab("Search", SearchView.class));
    	tabs.setOrientation(Orientation.VERTICAL);
    	return tabs;
    }
    
    private Tab createTab(String name, Class<? extends Component> target) {
    	RouterLink link = new RouterLink(name, target);
    	return new Tab(link);
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("Chirper");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(new DrawerToggle(), appName);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            layout.add(getUserMenu(maybeUser.get()));
        }

        header.add(layout);
        return header;
    }
    
    private MenuBar getUserMenu(User user) {
        Avatar avatar = new Avatar(user.getAlias());
        StreamResource resource = new StreamResource("profile-pic",
                () -> new ByteArrayInputStream(user.getProfilePicture()));
        avatar.setImageResource(resource);
        avatar.setThemeName("xsmall");
        avatar.getElement().setAttribute("tabindex", "-1");

        MenuBar userMenu = new MenuBar();
        userMenu.setThemeName("tertiary-inline contrast");

        MenuItem userName = userMenu.addItem("");
        Div div = new Div();
        div.add(avatar);
        div.add(user.getAlias());
        div.add(new Icon("lumo", "dropdown"));
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        userName.add(div);
        userName.getSubMenu().addItem("Sign out", e -> {
            authenticatedUser.logout();
        });

        return userMenu;
    }

}
