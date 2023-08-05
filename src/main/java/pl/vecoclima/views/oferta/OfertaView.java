package pl.vecoclima.views.oferta;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.vecoclima.views.MainLayout;

@PageTitle("Oferta")
@Route(value = "oferta", layout = MainLayout.class)
@Uses(Icon.class)
public class OfertaView extends Composite<VerticalLayout> {

    public OfertaView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
