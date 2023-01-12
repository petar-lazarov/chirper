package net.plazarov.chirper.views.welcome;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.plazarov.chirper.views.GuestLayout;
import net.plazarov.chirper.views.login.LoginView;
import net.plazarov.chirper.views.signup.SignUpView;

@PageTitle("Chirper")
@Route(value = "welcome", layout = GuestLayout.class)
@RouteAlias(value = "", layout = GuestLayout.class)
@AnonymousAllowed
public class WelcomeView extends VerticalLayout {

    public WelcomeView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        add(new H2("Welcome to Chirper"));
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
        
        MenuBar guestMenu = new MenuBar();
        
        Button loginButton = new Button("Log in");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button signinButton = new Button("Sign in");
        signinButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        guestMenu.addItem("Log in", e -> {
            guestMenu.getUI().ifPresent(ui -> {
            	ui.navigate(LoginView.class);
            });
        });
        guestMenu.addItem("Sign in", e -> {
        	guestMenu.getUI().ifPresent(ui -> {
        		ui.navigate(SignUpView.class);
        	});
        });

        add(guestMenu);
        
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
