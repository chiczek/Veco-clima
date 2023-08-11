package pl.vecoclima.views.stronagłówna;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import pl.vecoclima.other.SendMail;
import pl.vecoclima.views.MainLayout;

@PageTitle("Strona główna")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class StronaGlownaView extends Composite<VerticalLayout> {

    TextField name = new TextField("Imię i nazwisko");
    EmailField email = new EmailField("Email");
    TextField phone = new TextField("Numer telefonu");

    TextArea message = new TextArea("Dodatkowe informacje");
    Checkbox consentForContact = new Checkbox("Wyrażam zgodę na przetwarzanie moich danych osobowych zgodnie z ustawą o ochronie danych osobowych w związku z realizacją zgłoszenia.");


    //private TextField name;
    private Button sayHello;
    VerticalLayout mainVerticalLayout;
    HorizontalLayout mainHorizontalLayout;
    VerticalLayout leftLayout;
    VerticalLayout middleLayout;
    VerticalLayout rightLayout;

    public StronaGlownaView() {
        mainHorizontalLayout = new HorizontalLayout();
        mainVerticalLayout = new VerticalLayout();
        leftLayout = new VerticalLayout();
        middleLayout = new VerticalLayout();
        rightLayout = new VerticalLayout();
        mainHorizontalLayout.add(leftLayout, middleLayout, rightLayout);
        leftLayout.setWidth("45%");
        rightLayout.setWidth("5%");
        middleLayout.setWidth("50%");



        Image image = new Image("images/domowa.png", "Alt");
        image.setWidthFull();

        Image montazSiatka = new Image("images/montazSiatka.png", "Alt");
        montazSiatka.setWidthFull();

        leftLayout.add(montazSiatka);
        middleLayout.add(createMontazInfo());

        VaadinCKEditor classicEditor = new VaadinCKEditorBuilder().with(builder -> {
            builder.editorData = "<p>This is a classic editor sample.</p>";
            builder.editorType = Constants.EditorType.CLASSIC;
            builder.theme = Constants.ThemeType.DARK;
        }).createVaadinCKEditor();
        middleLayout.add(classicEditor);

        //rightLayout.add(b3);

        Section checkoutForm = new Section();
        checkoutForm.addClassNames("flex", "flex-col", "flex-grow");
        checkoutForm.add(createPersonalDetailsSection());
        checkoutForm.add(createFooter());

        middleLayout.add(checkoutForm);

        mainVerticalLayout.add(image);
        mainVerticalLayout.add(mainHorizontalLayout);
        mainVerticalLayout.setSizeUndefined();

        getContent().add(mainVerticalLayout);
        mainHorizontalLayout.setWidth("100%");
        getContent().setWidth("100%");



    }

    private Aside createMontazInfo() {
        Aside aside = new Aside();
        aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "sticky");
        Header headerSection = new Header();
        headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");
        H3 header = new H3("Montaż klimatyzacji");
        header.addClassNames("m-0");
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

        ul.add(createListItem("Obszar działania", "Nasza siedziba jest w Halinowie (powiat Miński), ale obszarem naszego działania jest cały powiat miński oraz Warszawa. " +
                "Zachęcamy do wysłania do nas zapytania o bezpłatną wycenę."));
        ul.add(createListItem("Adres email", "adam.kowieski@gmail.com"));
        ul.add(createListItem("Adres do korespondencji", "Halinów 05-074, ul. 3 Maja 15 G"));

        aside.add(headerSection, ul);
        return aside;
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
                if(!name.getValue().isEmpty() && !email.getValue().isEmpty() && !phone.getValue().isEmpty() && !email.isInvalid()) {
                    new SendMail().send(name.getValue(), email.getValue(), phone.getValue(), message.getValue());
                }
                else{
                    Notification.show("Sprawdź, czy wszystkie wymagane pola zostały prawidłowo uzupełnione", 2500, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else{
                Notification.show("Zaznacz zgodę na przetwarzanie danych, abyśmy mogli się z Tobą skontaktować", 2500, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        footer.add(cancel, send);
        return footer;
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

    private com.vaadin.flow.component.html.Section createPersonalDetailsSection() {
        com.vaadin.flow.component.html.Section personalDetails = new com.vaadin.flow.component.html.Section();
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

}

