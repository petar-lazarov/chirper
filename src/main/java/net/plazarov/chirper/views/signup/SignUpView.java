package net.plazarov.chirper.views.signup;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.plazarov.chirper.data.entity.User;
import net.plazarov.chirper.data.service.UserService;
import net.plazarov.chirper.security.AuthenticatedUser;

@PageTitle("Sign Up | Chirper")
@Route(value = "signup")
@AnonymousAllowed
public class SignUpView extends HorizontalLayout implements BeforeEnterObserver {

	private final AuthenticatedUser authenticatedUser;
	private final UserService userService;
    private PasswordField passwordConfirm;
	private Binder<User> binder;
	
	public SignUpView(AuthenticatedUser authenticatedUser, @Autowired UserService userService) {
		this.authenticatedUser = authenticatedUser;
		this.userService = userService;
		binder = new BeanValidationBinder<>(User.class);
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		add(createSignupForm());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            event.forwardTo("home");
        }
	}
	
	public FormLayout createSignupForm() {
		FormLayout form = new FormLayout();
		form.setMaxWidth("500px");
		H3 title = new H3("Sign up to Chirper");
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");
		passwordConfirm = new PasswordField("Confirm password");
		passwordConfirm.setRequiredIndicatorVisible(true);
		TextField alias = new TextField("Alias");
		EmailField email = new EmailField("Email");
		TextArea bio = new TextArea("Bio");
		Button submitButton = new Button("Submit", e -> {
			saveUser();
		});
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		form.setResponsiveSteps(new ResponsiveStep("0", 1));
		
		form.add(title, username, password, passwordConfirm, alias, email, bio, submitButton);
		binder.forField(username).asRequired().bind("username");
		binder.forField(password).asRequired().withValidator(this::passwordValidator).bind("hashedPassword");
		binder.forField(alias).asRequired().bind("alias");
		binder.forField(email).asRequired().bind("email");
		binder.forField(bio).bind("bio");
        passwordConfirm.addValueChangeListener(e -> {
        	binder.validate();
        });

		return form;
	}
	
    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {
        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }
        
        String pass2 = passwordConfirm.getValue();

        if (pass1 != null && pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }

	private void saveUser() {
		try {
			User user = new User();
			binder.writeBean(user);
			userService.register(user);
			UI.getCurrent().navigate("home");
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

}
