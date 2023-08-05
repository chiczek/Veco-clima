package pl.vecoclima.views.stronagłówna;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import pl.vecoclima.views.MainLayout;

@PageTitle("Strona główna")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class StronagłównaView extends Composite<VerticalLayout> {

    private VerticalLayout layoutColumn2 = new VerticalLayout();

    public StronagłównaView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        getContent().add(layoutColumn2);
    }
}
