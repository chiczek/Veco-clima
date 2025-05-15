package pl.vecoclima.views.sklep;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import pl.vecoclima.data.entity.*;
import pl.vecoclima.other.RestApiService;
import pl.vecoclima.views.MainLayout;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@PageTitle("Sklep")
@Route(value = "sklep", layout = MainLayout.class)
public class SklepView extends VerticalLayout {

    private ShoppingCart sessionCart;

    private OrderedList imageContainer;



    H5 totalNet = new H5();

    public SklepView() {
        sessionCart  = (ShoppingCart) VaadinSession.getCurrent().getAttribute("sessionCart");

        setSpacing(false);
        //setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //getStyle().set("text-align", "center");

        HorizontalLayout mainHorizontalLayout = new HorizontalLayout();
        VerticalLayout leftPane = new VerticalLayout();
        VerticalLayout rightPane = new VerticalLayout();

        mainHorizontalLayout.add(leftPane, rightPane);
        mainHorizontalLayout.setWidth("70%");

        HorizontalLayout topPanel = new HorizontalLayout();
        topPanel.setWidth("65%");
        topPanel.setJustifyContentMode(JustifyContentMode.END);
        Button showCart = new Button("", click -> {
            showCart();
        });
        showCart.setIcon(VaadinIcon.CART.create());
        topPanel.add(showCart);
        add(topPanel);
        add(mainHorizontalLayout);

        leftPane.add(new H4("Kategorie"));
        leftPane.setWidth("410px");
        Checkbox split = new Checkbox("Do domu - split");
        Checkbox multiSplit = new Checkbox("Do domu - multi split");
        leftPane.add(split, multiSplit);

        leftPane.add(new H4("Moc urządzenia"));
        Checkbox h25 = new Checkbox("2,5 KWh");
        Checkbox h35 = new Checkbox("3,5 KWh");
        Checkbox h50 = new Checkbox("5,0 KWh");
        Checkbox hmr = new Checkbox("Więcej niż 5,0 KWh");

        leftPane.add(h25, h35, h50, hmr);

        leftPane.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        rightPane.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Product.findAll().forEach(product -> {
            rightPane.add(createLine(product));
            rightPane.add(new Hr());
        });

        add(createFooter());
    }

    private Div createFooter() {
        Div div = new Div();
        div.addClassNames("blackBackground");
        div.setWidthFull();
        div.setHeight("400px");
        div.addClassNames("center");


        HorizontalLayout mainParts = new HorizontalLayout();
        VerticalLayout part1 = new VerticalLayout();
        VerticalLayout part2 = new VerticalLayout();
        VerticalLayout part3 = new VerticalLayout();
        mainParts.add(part1, part2, part3);
        part1.setWidth("25%");
        part2.setWidth("25%");
        part3.setWidth("25%");

        part1.add(new H4("Informacje dla klientów"));
        part1.add(new Hr());

        Button regulations = new Button("Regulamin", click -> {
            showWindowWithContent("regulamin");
        });
        regulations.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        part1.add(regulations);



        Button privacy = new Button("Polityka prywatności", click -> {
            showWindowWithContent("politykaprywatnosci");
        });
        privacy.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        part1.add(privacy);


        part2.add(new H4("Informacje dla klientów"));
        part2.add(new Hr());
        Button kontakt = new Button("Kontakt", click -> {
            showWindowWithContent("kontakt");


        });
        kontakt.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);

        Button oNas = new Button("O naszej firmie", click -> {
            showWindowWithContent("onaszejfirmie");
        });
        oNas.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);

        part2.add(kontakt, oNas);

        mainParts.addClassNames("padding30");
        div.add(mainParts);

        regulations.addClassName("whitebutton");

        return div;
    }

    private void showWindowWithContent(String content) {
        Dialog dialog = new Dialog();
        dialog.setWidth("90%");
        dialog.setHeight("95%");
        dialog.open();
        dialog.setModal(true);
        dialog.setCloseOnOutsideClick(false);

        File file = null;
        switch (content){
            case "regulamin" : {
                file = new File("src\\main\\resources\\META-INF\\resources\\html\\regulamin.html");
                break;
            }
            case "politykaprywatnosci" : {
                file = new File("src\\main\\resources\\META-INF\\resources\\html\\politykaprywatnosci.html");
                break;
            }
            case "onaszejfirmie" : {
                file = new File("src\\main\\resources\\META-INF\\resources\\html\\onaszejfirmie.html");
                break;
            }
            case "kontakt" : {
                file = new File("src\\main\\resources\\META-INF\\resources\\html\\kontakt.html");
                break;
            }
        }



        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.END);
        Button exit = new Button("", click -> dialog.close());
        exit.setIcon(VaadinIcon.CLOSE_SMALL.create());
        top.add(exit);

        StringBuilder sb = new StringBuilder();

        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach(line -> sb.append(line));
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        VaadinCKEditor editor = new VaadinCKEditorBuilder().with(builder->{
            builder.editorType= Constants.EditorType.BALLOON;
            builder.editorData=sb.toString();
        }).createVaadinCKEditor();
        editor.setReadOnly(true);

        editor.setHeight("500px");

        Button save = new Button("save", click -> {

            File file2 = null;
            switch (content){
                case "regulamin" : {
                    file2 = new File("src\\main\\resources\\META-INF\\resources\\html\\regulamin.html");
                    break;
                }
                case "politykaprywatnosci" : {
                    file2 = new File("src\\main\\resources\\META-INF\\resources\\html\\politykaprywatnosci.html");
                    break;
                }
                case "onaszejfirmie" : {
                    file2 = new File("src\\main\\resources\\META-INF\\resources\\html\\onaszejfirmie.html");
                    break;
                }
            }


            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file2));
                writer.write(editor.getValue());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        save.setVisible(false);
        editor.setHeight("90%");

        // vl.add(theAddon);
        dialog.add(top, editor, save); //temp

    }

    private HorizontalLayout createLine(Product product) {
        HorizontalLayout line = new HorizontalLayout();
        System.out.println(product.getImageURL());
        Image img = new Image(product.getImageURL() == null ? "" : product.getImageURL(), "Image");
        img.setHeightFull();
        line.add(img);

        VerticalLayout main = new VerticalLayout();

        main.add(new H4(product.getName()));
        main.add(new H6(product.getDescriptionShort()));
        line.addAndExpand(main);
        line.add(main);
        main.setSizeFull();

        H3 price  = new H3( new DecimalFormat("#.##").format(product.getGrossPrice()) + " zł");

        Button addToCart = new Button("Dodaj do koszyka", click ->{
            addToCart(product);
        });
        addToCart.setIcon(VaadinIcon.CART.create());
        addToCart.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addToCart.setWidthFull();

        Button details = new Button("Szczegóły", click ->{
            showProductDetails(product);

        });
        details.setIcon(VaadinIcon.INFO_CIRCLE.create());
        details.setWidthFull();

        VerticalLayout buts = new VerticalLayout();
        buts.add(price, addToCart, details);
        buts.setWidth("270px");
        buts.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        line.add(buts);


        line.setMaxWidth("1000px");
        line.setHeight("200px");
        line.add();
        return line;
    }

    private void showProductDetails(Product product) {
        Dialog d = new Dialog();
        d.setWidth("90%");
        d.setHeight("95%");
        d.open();
        d.setModal(true);
        d.setCloseOnOutsideClick(false);
        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.END);
        Button exit = new Button("", click -> d.close());
        exit.setIcon(VaadinIcon.CLOSE_SMALL.create());
        top.add(exit);
        d.add(top);

        Div div = new Div();
        div.setWidthFull();
        div.setHeight("90%");
        VaadinCKEditor purp = new VaadinCKEditorBuilder().with(builder->{
            builder.editorType= Constants.EditorType.BALLOON;
            builder.editorData=product.getDescriptionLong();

        }).createVaadinCKEditor();

        purp.setWidthFull();

        purp.setHeight("90%");

        div.add(purp);
        d.add(purp);

        d.add(new Button("Zapisz", click -> {
            product.setDescriptionLong(purp.getValue());
            product.update();
            d.close();
            Notification.show("Dane zostały zapisane").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }));
    }

    private void addToCart(Product product){
        if(sessionCart == null){
            VaadinSession.getCurrent().setAttribute("sessionCart", sessionCart);
            sessionCart = new ShoppingCart(null, LocalDateTime.now());

        }
        if(sessionCart.getShoppingCartLines().stream().filter(p -> p.getProduct().getId() == product.getId()).count() > 0){
            ShoppingCartLine scl = sessionCart.getShoppingCartLines().stream().filter(p -> p.getProduct().getId() == product.getId()).findAny().get();
            scl.setAmount(scl.getAmount() + 1);
        }
        else {
            sessionCart.add(product, 1);
        }
        ConfirmDialog cd = new ConfirmDialog();
        cd.setCancelable(true);
        cd.setText("Produkt ("+product.getName()+") został dodany do koszyka.");
        cd.setCancelText("Kontynuuj zakupy");
        cd.setConfirmText("Idź do koszyka");
        cd.addConfirmListener(c -> {
            showCart();
            cd.close();
        });
        cd.addCancelListener(c-> cd.close());
        cd.open();
    }

    private void showCart() {

        Dialog d = new Dialog();
        d.setWidth("90%");
        d.setHeight("90%");
        d.open();
        d.setCloseOnOutsideClick(false);

        Paragraph l1 = new Paragraph("Twój koszyk");
        ProgressBar pb1 = new ProgressBar();
        VerticalLayout v1 = new VerticalLayout(l1, pb1);
        pb1.setValue(1.0f);
        pb1.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);

        Paragraph l2 = new Paragraph("Podsumowanie i płatność");
        ProgressBar pb2 = new ProgressBar();
        VerticalLayout v2 = new VerticalLayout(l2, pb2);
        pb2.setValue(0.0f);
        pb2.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);

        Paragraph l3 = new Paragraph("Gotowe");
        ProgressBar pb3 = new ProgressBar();
        VerticalLayout v3 = new VerticalLayout(l3, pb3);
        pb3.setValue(0.0f);
        pb3.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);

        HorizontalLayout pbs = new HorizontalLayout(v1, v2, v3);
        pbs.setWidthFull();
        d.add(pbs);

        //------------------------------//

        VerticalLayout part1 = new VerticalLayout();
        VerticalLayout part2 = new VerticalLayout();
        VerticalLayout part3 = new VerticalLayout();
        part2.setVisible(false);
        part3.setVisible(false);


        part1.add(new H3("Zawartość Twojego koszyka"));

        Grid<ShoppingCartLine> grid = new Grid<>();
        grid.addComponentColumn(shoppingCartLine -> {
            Image img = new Image(shoppingCartLine.getProduct().getImageURL(), "alt");
            img.setWidth("150px");
            return img;
        });
        grid.addColumn(p -> p.getProduct().getName()).setHeader("Produkt/Usługa").setResizable(true);
        grid.addColumn(p -> p.getPriceGross() + " zł").setHeader("Cena brutto");
        grid.addComponentColumn(p -> {
            HorizontalLayout hl = new HorizontalLayout();
            Button minus = new Button("", click ->{
                p.setAmount(p.getAmount() - 1);
                grid.getDataProvider().refreshItem(p);
                setTotalNet();
            });
            minus.setIcon(VaadinIcon.MINUS.create());

            Button plus = new Button("", click -> {
                p.setAmount(p.getAmount() + 1);
                grid.getDataProvider().refreshItem(p);
                setTotalNet();
            });
            plus.setIcon(VaadinIcon.PLUS.create());

            hl.add(minus, new H6(""+p.getAmount()), plus);
            return hl;
        }).setHeader("Ilość").setAutoWidth(true);
        grid.addColumn(p -> (new DecimalFormat("### ###.##").format(p.getAmount() * p.getPriceGross().doubleValue())) + " zł").setAutoWidth(true).setHeader("Wartość brutto");
        grid.addComponentColumn(p -> {
            Button del = new Button("", click -> {

                p.getShoppingCart().getShoppingCartLines().remove(p);
                grid.setItems(sessionCart.getShoppingCartLines());

            });
            del.setIcon(VaadinIcon.CLOSE_SMALL.create());
            return del;
        }).setAutoWidth(true);
        grid.setItems(sessionCart.getShoppingCartLines());

        part1.add(grid);

        VerticalLayout summary = new VerticalLayout();
        summary.setWidthFull();
        summary.setDefaultHorizontalComponentAlignment(Alignment.END);
        summary.add(new H4("Podsumowanie:"));


        summary.add(totalNet);
        setTotalNet();
        part1.add(summary);
        Button nextStep = new Button("Płatność i dostawa", click -> {
            part1.setVisible(false);
            part2.setVisible(true);
            pb2.setValue(1.0f);

        });
        nextStep.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextStep.setIcon(VaadinIcon.ARROW_FORWARD.create());

        Button closeWindow = new Button("Zamknij koszyk", click -> d.close());
        part1.add(new HorizontalLayout(nextStep, closeWindow));

        //-------------------------------------//
        //PART 2
        FormLayout d1 = new FormLayout();
        FormLayout d2 = new FormLayout();
        FormLayout d3 = new FormLayout();
        d1.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        d2.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        d3.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        VerticalLayout vl1 = new VerticalLayout();
        VerticalLayout vl2 = new VerticalLayout();
        VerticalLayout vl3 = new VerticalLayout();



        H4 personalDataLabel = new H4("Dane zamawiającego");
        personalDataLabel.setWidthFull();
        VerticalLayout form = new VerticalLayout();
        TextField name = new TextField("Imię");
        name.setRequired(true);
        TextField surname = new TextField("Nazwisko");
        surname.setRequired(true);
        surname.setRequiredIndicatorVisible(true);
        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);

        H4 addressData = new H4("Date adresowe");
        TextField street = new TextField("Ulica");
        street.setRequired(true);
        street.setRequiredIndicatorVisible(true);
        TextField buildingNumber = new TextField("Numer budynku / mieszkania");
        buildingNumber.setRequired(true);
        buildingNumber.setRequiredIndicatorVisible(true);
        TextField postalCode = new TextField("Kod pocztowy");
        postalCode.setRequired(true);
        postalCode.setRequiredIndicatorVisible(true);
        TextField city = new TextField("Miejscowość");
        city.setRequired(true);
        city.setRequiredIndicatorVisible(true);
        TextField phone = new TextField("Numer telefonu");
        phone.setRequired(true);
        phone.setRequiredIndicatorVisible(true);

        TextArea details = new TextArea("Uwagi do zamówienia");
        details.setWidthFull();

        H4 paymentForm = new H4("Metoda płatności");
        RadioButtonGroup<String> paymentFormOptions = new RadioButtonGroup<>();
        paymentFormOptions.setRequired(true);
        paymentFormOptions.setRequiredIndicatorVisible(true);
        paymentFormOptions.setItems("Karta kredytowa", "Blik", "Szybki przelew", "Raty");

        Button previous = new Button("Wróć do koszyka", click -> {
            part2.setVisible(false);
            part1.setVisible(true);
            pb2.setValue(0.0f);
        });
        previous.setIcon(VaadinIcon.ARROW_BACKWARD.create());


        Button next = new Button("Przejdź do płatności", click -> {
            if(name.getValue().length() > 0 && surname.getValue().length() > 0 && email.getValue().length() > 6 && street.getValue().length() > 0
            && buildingNumber.getValue().length() > 0 && postalCode.getValue().length() == 6 && city.getValue().length() > 2 && paymentFormOptions.getValue() != null) {
                try {
                    Person client = new Person(email.getValue(), name.getValue(), surname.getValue(),
                            postalCode.getValue(), street.getValue(), buildingNumber.getValue(), city.getValue(), false, phone.getValue());
                    int idClient = client.insert();

                    client.setId(idClient);
                    System.out.println("id client " + idClient);

                    sessionCart.setCreatedBy(client);

                    int idCart = sessionCart.insert();
                    System.out.println("id cart " + idCart);
                    sessionCart.setId(idCart);
                    Order order = new Order(""+Order.findLastNumber() + 1, LocalDateTime.now(), paymentFormOptions.getValue(), "Złożone - nieopłacone", client, sessionCart, details.getValue());
                    new RestApiService().submit(sessionCart);
                    order.insert();
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            else{

                Notification.show("Wprowadzono niepełne dane formularza. Sprawdź poprawność danych i spróbuj ponownie.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        next.setIcon(VaadinIcon.ARROW_RIGHT.create());
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buts2 = new HorizontalLayout(previous, next);

        d1.add(name, surname, email);
        d2.add(street, buildingNumber, postalCode, city, phone);
        d3.add(paymentFormOptions);
        form.add(personalDataLabel, d1, addressData, d2, details, paymentForm, d3, buts2);
        part2.add(form);
        d.add(part1, part2, part3);

    }

    private void setTotalNet(){
        totalNet.setText("Wartość zakupów: " + new DecimalFormat("### ###.##").format(sessionCart.getShoppingCartLines().stream()
                .mapToDouble(e -> (e.getAmount() * e.getPriceGross().doubleValue()))
                .sum())+ " zł");
    }


}