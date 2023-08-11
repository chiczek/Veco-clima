package pl.vecoclima.views.zarządzanie;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import org.apache.poi.EncryptedDocumentException;
import pl.vecoclima.data.entity.Product;
import pl.vecoclima.views.MainLayout;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Zarządzanie")
@Route(value = "zarzadzanie", layout = MainLayout.class)
public class ZarządzanieView extends VerticalLayout {

    Product globalProduct;

    public ZarządzanieView() {
        setSpacing(false);
        setSizeFull();

        add(getProductManagementLayout());
    }

    private VerticalLayout getProductManagementLayout() {
        VerticalLayout vl = new VerticalLayout();
        HorizontalLayout topLayout = new HorizontalLayout();
        Grid<Product> grid = new Grid<>();
        grid.setWidthFull();
        vl.add(topLayout);
        vl.add(grid);


        Button addOrEdit = new Button("Add or edit", click -> {
            if(!grid.getSelectedItems().isEmpty()) {
                globalProduct = grid.getSelectedItems().iterator().next();
            }
            else{
                globalProduct = new Product();
            }
            showAddOrEditDialog();
        });
        addOrEdit.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        topLayout.add(addOrEdit);

        grid.addColumn(Product::getId).setHeader("Id");

        grid.addComponentColumn(product -> {
            Image img = new Image(product.getImageURL() == null ? "" : product.getImageURL(), "Image");
            img.setHeightFull();
            return img;
        }).setHeader("Zdjęcie");

        grid.addColumn(Product::getName).setHeader("Nazwa");

        grid.addComponentColumn(product -> {
            VaadinCKEditor editor = new VaadinCKEditorBuilder().with(builder->{
                builder.editorType= Constants.EditorType.BALLOON;
                builder.editorData=product.getDescriptionShort();
            }).createVaadinCKEditor();
            editor.setReadOnly(true);
            return editor;
        }).setHeader("Opis krótki");



        grid.addColumn(Product::getGrossPrice).setHeader("Cena brutto");
        grid.addColumn(Product::isAvailable).setHeader("Czy dostępny");
        grid.addColumn(Product::getAddedByName).setHeader("Dodany przez");
        grid.addColumn(Product::getAddedDateTime).setHeader("Data dodania");
        grid.setItems(Product.findAll());


        return vl;
    }

    private void showAddOrEditDialog() {


        Dialog dialog = new Dialog();
        dialog.setWidth("90%");
        dialog.setHeight("90%");
        dialog.open();
        dialog.setModal(true);
        dialog.setCloseOnOutsideClick(false);
        Button quit = new Button("", c -> dialog.close());
        quit.setIcon(VaadinIcon.CLOSE.create());
        HorizontalLayout top = new HorizontalLayout(quit);
        top.setJustifyContentMode(JustifyContentMode.END);
        dialog.add(top);
        VerticalLayout main = new VerticalLayout();

        TextField name = new TextField("Nazwa");
        name.setWidthFull();
        Div imageDiv = new Div();

        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload dropEnabledUpload = new Upload(memoryBuffer);
        dropEnabledUpload.setVisible(false);
        AtomicReference<Image> image = new AtomicReference<>();

        Label dropLabel = new Label("Images files only");
        dropEnabledUpload.setDropLabel(dropLabel);
        dropEnabledUpload.setWidth("100%");
        dropEnabledUpload.setMaxFileSize(300 * 1024 * 1024);
        dropEnabledUpload.setDropAllowed(true);
        dropEnabledUpload.addSucceededListener(event ->{
            File imageFile = getImageFile(memoryBuffer, event);
            globalProduct.setImageURL(imageFile.getPath());
            final StreamResource imageResource = new StreamResource("MyResourceName", () -> {
                try{
                    return new FileInputStream(imageFile);
                }
                catch(final FileNotFoundException e){
                    e.printStackTrace();
                    return null;
                }
            });
            image.set(new Image(imageResource, "No image found"));
            image.get().setWidth("200px");
            imageDiv.add(image.get());
            imageDiv.setWidth("100%");
        });

        dialog.add(imageDiv);

        Paragraph p1 = new Paragraph("Opis krótki:");

        VaadinCKEditor shortDescription = new VaadinCKEditorBuilder().with(builder->{
            builder.editorType= Constants.EditorType.CLASSIC;
            builder.editorData=globalProduct.getDescriptionLong();
        }).createVaadinCKEditor();
        shortDescription.setWidthFull();
        shortDescription.setHeight("300px");

       Paragraph p2 = new Paragraph("Opis długi");

        VaadinCKEditor longDescription = new VaadinCKEditorBuilder().with(builder->{
            builder.editorType= Constants.EditorType.CLASSIC;
            builder.editorData=globalProduct.getDescriptionLong();
        }).createVaadinCKEditor();
        longDescription.setWidthFull();
        longDescription.setHeight("300px");

        Checkbox isAvailable = new Checkbox("Is available");
        NumberField grossPrice = new NumberField("Cena brutto");

        if(globalProduct != null && globalProduct.getName() != null && globalProduct.getName().length() > 0){
            name.setValue(globalProduct.getName());
            shortDescription.setValue(globalProduct.getDescriptionShort());
            longDescription.setValue(globalProduct.getDescriptionLong());
            grossPrice.setValue(globalProduct.getGrossPrice().doubleValue());
            Image img = new Image(globalProduct.getImageURL() == null ? "" : globalProduct.getImageURL(), "Image");
            image.set(img);
        }

        Button save = new Button("Zapisz", click -> {

        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button add = new Button("Dodaj", click -> {

        });
        add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button cancel = new Button("Anuluj", click -> {
           dialog.close();
        });

        if(globalProduct == null){
            save.setVisible(false);
        }

        HorizontalLayout bottom = new HorizontalLayout(save, add, cancel);



        main.add(name, imageDiv, p1, shortDescription, p2, longDescription, isAvailable, grossPrice, bottom);
        dialog.add(main);






    }

    public static File getImageFile(MemoryBuffer memoryBuffer, SucceededEvent event) {
        InputStream fileData = memoryBuffer.getInputStream();
        String fileName = event.getFileName();

        try {
            int time = LocalDateTime.now().hashCode();
            String url = "src\\main\\resources\\META-INF\\resources\\html\\" + time + fileName;
            File targetFile = new File(url);
            java.nio.file.Files.copy(fileData,  targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return targetFile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
