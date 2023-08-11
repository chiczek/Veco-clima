package pl.vecoclima.views.oferta;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.vecoclima.views.MainLayout;

@PageTitle("Oferta")
@Route(value = "oferta", layout = MainLayout.class)
@Uses(Icon.class)
public class OfertaView extends Composite<VerticalLayout> {

    private OrderedList imageContainer;

    public OfertaView() {
        constructUI();
        Image montazImage = new Image("images/montaz.jpg", "Alt");
        imageContainer.add(new OfertaViewCard(
                "Montaż klimatyzacji typu split",
                "Kompleksowa usługa",
                "Montażem klimatyzacji zajmujemy się od ponad 6 lat. Oferujemy montaż pojedynczych klimatyzacji a także systemów klimatyzacji. W swojej ofercie posiadamy klimatyzatory znanych marek w atrakcyjnych cenach. Skontaktuj się z nami aby pozać więcej szczegółów",
                montazImage));

        Image uprawnieniaImage = new Image("images/uprawnienia.jpg", "Alt");
        imageContainer.add(new OfertaViewCard(
                "Posiadamy uprawnienia i kwalifikacje",
                "Z nami masz pewność i gwarancję",
                "Każda firma instalująca urządzenia klimatyzacyjne powinna mieć wg polskiego prawa odpowiednie do tego uprawnienia. Każdy pracownik naszej firmy przeszedł szkolenie i uzyskał certyfikat F Gazy wydany przez Urząd Dozoru Technicznego. Nasza firma również dysponuje certyfikatem F Gazy dla przedsiębiorstw",
                uprawnieniaImage));

        Image montazystaImage = new Image("images/montazysta.jpg", "Alt");
        imageContainer.add(new OfertaViewCard(
                "Posiadamy niezbędny sprzęt",
                "Nadzędzia dla profesjonalistów",
                "Kim byłby fachowiec bez odpowiednich narzędzi? Zależy nam na bezpieczeństwie klientów i pracowników, posiadamy m.in. elektroniczne czujniki nieszczelności układu a także inne narzędzia sprawdzone przez Urząd Dozoru Technicznego ",
                montazystaImage));

//        Image montazImage = new Image("images/montaz.jpg", "Alt");
//        imageContainer.add(new OfertaViewCard(
//                "Montaż klimatyzacji typu split",
//                "Kompleksowa usługa",
//                "Montażem klimatyzacji zajmujemy się od 6 lat...",
//                montazImage));


//        imageContainer.add(new OfertaViewCard("Snow covered mountain",
//                "https://images.unsplash.com/photo-1512273222628-4daea6e55abb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
//        imageContainer.add(new OfertaViewCard("River between mountains",
//                "https://images.unsplash.com/photo-1536048810607-3dc7f86981cb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=375&q=80"));
//        imageContainer.add(new OfertaViewCard("Milky way on mountains",
//                "https://images.unsplash.com/photo-1515705576963-95cad62945b6?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=750&q=80"));
//        imageContainer.add(new OfertaViewCard("Mountain with fog",
//                "https://images.unsplash.com/photo-1513147122760-ad1d5bf68cdb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80"));
//        imageContainer.add(new OfertaViewCard("Mountain at night",
//                "https://images.unsplash.com/photo-1562832135-14a35d25edef?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=815&q=80"));

    }

    private void constructUI() {
        addClassNames("oferta-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Co możemy dla Ciebie zrobić:");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        Paragraph description = new Paragraph("Royalty free photos and pictures, courtesy of Unsplash");
        description.addClassNames("mb-xl", "mt-0", "text-secondary");
        headerContainer.add(header, description);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        container.add(header, sortBy);
        getContent().add(container, imageContainer);

    }
}
