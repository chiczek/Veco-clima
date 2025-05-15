package pl.vecoclima.views.kontakt;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import pl.vecoclima.other.SendMail;
import pl.vecoclima.views.MainLayout;

@PageTitle("Kontakt")
@Route(value = "kontakt", layout = MainLayout.class)
public class KontaktView extends VerticalLayout {

    TextField name = new TextField("Imię i nazwisko");
    EmailField email = new EmailField("Email");
    TextField phone = new TextField("Numer telefonu");

    TextArea message = new TextArea("Dodatkowe informacje");
    Checkbox consentForContact = new Checkbox("Wyrażam zgodę na przetwarzanie moich danych osobowych zgodnie z ustawą o ochronie danych osobowych w związku z realizacją zgłoszenia.");

    public KontaktView() {
        addClassNames("kontakt-view", "flex", "flex-col", "h-full");

        Main content = new Main();
        content.addClassNames("grid", "gap-xl", "items-start", "justify-center", "max-w-screen-md", "mx-auto", "pb-l",
                "px-l");

        H2 header = new H2("Skontaktuj się z nami");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        Paragraph note = new Paragraph("Zadzwoń, napisz, lub zgłoś prośbę o kontakt");
        note.addClassNames("mb-xl", "mt-0", "text-secondary");
        content.add(header, note);

        content.add(createAside());

        content.add(createCheckoutForm());

        add(content);
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames("flex", "flex-col", "flex-grow");



        checkoutForm.add(createPersonalDetailsSection());
        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private Section createPersonalDetailsSection() {
        Section personalDetails = new Section();
        personalDetails.addClassNames("flex", "flex-col", "mb-xl", "mt-m");


        H3 header = new H3("Wypełnij formularz, a skontaktujemy się z Tobą jeszcze tego samego dnia");
        header.addClassNames("mb-m", "mt-s", "text-2xl");


        name.setRequiredIndicatorVisible(true);
        name.setPattern("[\\p{L} \\-]+");
        name.addClassNames("mb-s");

        email.setRequiredIndicatorVisible(true);
        email.addClassNames("mb-s");

        phone.setRequiredIndicatorVisible(true);
        phone.setPattern("[\\d \\-\\+]+");
        phone.addClassNames("mb-s");

        message.addClassNames("mb-s");

        consentForContact.addClassNames("mt-s");
        consentForContact.setRequiredIndicatorVisible(true);

        personalDetails.add(header, name, email, phone, message, consentForContact);
        return personalDetails;
    }


    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames("flex", "items-center", "justify-between", "my-m");

        Button cancel = new Button("Wyczyść formularz");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(click ->{
            name.setValue("");
            email.setValue("");
            phone.setValue("");
            consentForContact.setValue(false);
        });

        Button send = new Button("Wyślij prośbę o kontakt", new Icon(VaadinIcon.MOBILE));
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        send.addClickListener(click ->{
            if(consentForContact.getValue() == true) {
                new SendMail().send(name.getValue(), email.getValue(), phone.getValue(), message.getValue());
            }
            else{
                Notification.show("Zaznacz zgodę na przetwarzanie danych, abyśmy mogli się z Tobą skontaktować", 2500, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        footer.add(cancel, send);
        return footer;
    }

    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "sticky");
        Header headerSection = new Header();
        headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");
        H3 header = new H3("Dane kontaktowe");
        header.addClassNames("m-0");
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

        ul.add(createListItem("Numer telefonu", "+48 792 798 879"));
        ul.add(createListItem("Adres email", "veco.clima@gmail.com"));
        ul.add(createListItem("Adres do korespondencji", "Halinów 05-074, ul. 3 Maja 15 G"));

        aside.add(headerSection, ul);
        return aside;
    }

    private ListItem createListItem(String primary, String secondary) {
        ListItem item = new ListItem();
        item.addClassNames("flex", "justify-between");

        Div subSection = new Div();
        subSection.addClassNames("flex", "flex-col");

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames("text-s text-secondary");
        subSection.add(secondarySpan);

        item.add(subSection);
        return item;
    }
}
