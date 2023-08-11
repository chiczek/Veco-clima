package pl.vecoclima.views.oferta;

import com.vaadin.flow.component.html.*;

public class OfertaViewCard extends ListItem {

    public OfertaViewCard(String title, String subtitles, String text, Image image) {
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");

       // Image image = new Image();
        image.setWidth("100%");
        image.setAlt(text);

        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(title);

        Span subtitle = new Span();
        subtitle.addClassNames("text-s", "text-secondary");
        subtitle.setText(subtitles);

        Paragraph description = new Paragraph(
                text);
        description.addClassName("my-m");

//        Span badge = new Span();
//        badge.getElement().setAttribute("theme", "badge");
//        badge.setText("Czytaj więcej");

        add(div, header, subtitle, description);
        //add(badge);

    }
}
