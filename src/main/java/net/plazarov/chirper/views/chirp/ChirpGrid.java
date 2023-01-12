package net.plazarov.chirper.views.chirp;

import java.util.Set;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import net.plazarov.chirper.data.entity.Chirp;

public class ChirpGrid extends Div {
	private Set<Chirp> chirps;
	private Grid<Chirp> grid = new Grid<>();
	public ChirpGrid(Set<Chirp> chirps) {
		this.chirps = chirps;
		
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(chirp -> createCard(chirp));
        grid.setItems(chirps);
        add(grid);
	}
	
	private HorizontalLayout createCard(Chirp chirp) {
		HorizontalLayout card = new HorizontalLayout();
		return card;
	}

}
