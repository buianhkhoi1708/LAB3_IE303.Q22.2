package com.khoi;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class StoreAppFX extends Application {

    private LeftPanel leftPanel;
    private List<ProductCard> cardList = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        List<Product> products = createSampleData();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffffff;");

        leftPanel = new LeftPanel(products.get(0));
        root.setLeft(leftPanel);

        TilePane productGrid = new TilePane();
        productGrid.setPadding(new Insets(20, 20, 80, 20));
        productGrid.setHgap(15);
        productGrid.setVgap(40);
        productGrid.setPrefColumns(4);
        productGrid.setPrefTileWidth(210);
        productGrid.setPrefTileHeight(220);
        productGrid.setStyle("-fx-background-color: #ffffff;");
        productGrid.setMinWidth(920);

        for (Product p : products) {

            ProductCard card = new ProductCard(p);

            cardList.add(card);
            productGrid.getChildren().add(card);

            card.setOnMouseClicked(e -> {

                for (ProductCard c : cardList) {
                    c.setSelected(false);
                }

                card.setSelected(true);
                leftPanel.updateProduct(p);
            });
        }

        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #ffffff;");
        scrollPane.setBorder(Border.EMPTY);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setCenter(scrollPane);

        cardList.get(0).setSelected(true);

        Scene scene = new Scene(root, 1280, 750);

        primaryStage.setTitle("Adidas Originals Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // PRODUCT MODEL 

    static class Product {

        final String name;
        final String brand;
        final String fullNote;
        final String cardNote;
        final String price;
        final Image image;

        public Product(
                String name,
                String brand,
                String fullNote,
                String cardNote,
                String price,
                String imagePath
        ) {

            this.name = name;
            this.brand = brand;
            this.fullNote = fullNote;
            this.cardNote = cardNote;
            this.price = price;

            this.image = new Image(
                    getClass().getResourceAsStream("/images/" + imagePath)
            );
        }
    }

    // LEFT PANEL 

    class LeftPanel extends VBox {

        private final ImageView imageView;
        private final Label titleLabel;
        private final Label priceLabel;
        private final Label brandLabel;
        private final Text noteText;

        public LeftPanel(Product product) {

            setPadding(new Insets(30, 20, 30, 30));
            setSpacing(8);
            setPrefWidth(350);

            setStyle("-fx-background-color: #ffffff;");

            imageView = new ImageView(product.image);

            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            VBox.setMargin(imageView, new Insets(20, 0, 10, 0));

            Region separator = new Region();

            separator.setPrefHeight(1);
            separator.setStyle("-fx-background-color: #dcdcdc;");

            VBox.setMargin(separator, new Insets(10, 0, 15, 0));

            titleLabel = new Label(product.name);

            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            titleLabel.setTextFill(Color.web("#333333"));
            titleLabel.setWrapText(true);

            priceLabel = new Label(product.price);

            priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            priceLabel.setTextFill(Color.web("#333333"));

            VBox.setMargin(priceLabel, new Insets(0, 0, 5, 0));

            brandLabel = new Label(product.brand);

            brandLabel.setFont(Font.font("Arial", 13));
            brandLabel.setTextFill(Color.web("#757575"));

            noteText = new Text(product.fullNote);

            noteText.setFont(Font.font("Arial", 12));
            noteText.setFill(Color.web("#9e9e9e"));
            noteText.setWrappingWidth(300);

            VBox.setMargin(noteText, new Insets(5, 0, 0, 0));

            getChildren().addAll(
                    imageView,
                    separator,
                    titleLabel,
                    priceLabel,
                    brandLabel,
                    noteText
            );
        }

        public void updateProduct(Product product) {

            titleLabel.setText(product.name);
            priceLabel.setText(product.price);
            brandLabel.setText(product.brand);
            noteText.setText(product.fullNote);

            imageView.setImage(product.image);

            FadeTransition fade =
                    new FadeTransition(Duration.millis(350), imageView);

            fade.setFromValue(0.4);
            fade.setToValue(1.0);

            fade.play();
        }
    }

    // PRODUCT CARD

    class ProductCard extends VBox {

        private static final String DEFAULT_STYLE =
                "-fx-background-color: #f3f3f3;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: transparent;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;";

        private static final String SELECTED_STYLE =
                "-fx-background-color: #ffffff;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #8cb4f8;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 8;";

        public ProductCard(Product product) {

            setPadding(new Insets(15, 12, 15, 12));
            setSpacing(5);
            setPrefWidth(210);

            setStyle(DEFAULT_STYLE);

            setCursor(Cursor.HAND);


            Label nameLabel = new Label(product.name);

            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            nameLabel.setTextFill(Color.web("#333333"));
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(190);

            Label noteLabel = new Label(product.cardNote);
            noteLabel.setFont(Font.font("Arial", 11));
            noteLabel.setTextFill(Color.web("#9e9e9e"));

            noteLabel.setWrapText(true);
            noteLabel.setMaxWidth(190);


            ImageView imgView = new ImageView(product.image);

            imgView.setFitWidth(170);
            imgView.setFitHeight(110);

            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);

            HBox imgContainer = new HBox(imgView);

            imgContainer.setAlignment(Pos.CENTER);
            imgContainer.setPadding(new Insets(10, 0, 10, 0));


            BorderPane bottomPane = new BorderPane();

            Label brandLabel = new Label(product.brand);

            brandLabel.setFont(Font.font("Arial", 12));
            brandLabel.setTextFill(Color.web("#757575"));

            Label priceLabel = new Label(product.price);

            priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            priceLabel.setTextFill(Color.web("#333333"));

            bottomPane.setLeft(brandLabel);
            bottomPane.setRight(priceLabel);

            BorderPane.setAlignment(brandLabel, Pos.BOTTOM_LEFT);
            BorderPane.setAlignment(priceLabel, Pos.BOTTOM_RIGHT);

            VBox.setMargin(bottomPane, new Insets(auto, 0, 0, 0));

            getChildren().addAll(
                    nameLabel,
                    noteLabel,
                    imgContainer,
                    bottomPane
            );

            VBox.setVgrow(imgContainer, Priority.ALWAYS);
        }

        public void setSelected(boolean selected) {

            if (selected) {
                setStyle(SELECTED_STYLE);
            } else {
                setStyle(DEFAULT_STYLE);
            }
        }

        private static final double auto = 5.0;
    }

    // SAMPLE DATA

    private List<Product> createSampleData() {

        List<Product> list = new ArrayList<>();

        String exclusionFull =
                "This product is excluded from all\n" +
                "promotional discounts and offers.";

        String exclusionShort =
                "This product is excluded fr...";

        list.add(new Product(
                "4DFWD PULSE SHOES",
                "Adidas",
                exclusionFull,
                exclusionShort,
                "$160.00",
                "img1.png"
        ));

        list.add(new Product(
                "FORUM MID SHOES",
                "Adidas",
                exclusionFull,
                exclusionShort,
                "$100.00",
                "img2.png"
        ));

        list.add(new Product(
                "SUPERNOVA SHOES",
                "Adidas",
                "NMD City Stock 2",
                "NMD City Stock 2",
                "$150.00",
                "img3.png"
        ));

        list.add(new Product(
                "NMD City Stock 2",
                "Adidas",
                "NMD City Stock 2",
                "NMD City Stock 2",
                "$160.00",
                "img4.png"
        ));

        list.add(new Product(
                "4DFWD PULSE SHOES",
                "Adidas",
                "NMD City Stock 2",
                "NMD City Stock 2",
                "$160.00",
                "img5.png"
        ));

        list.add(new Product(
                "FORUM MID SHOES",
                "Adidas",
                exclusionFull,
                exclusionShort,
                "$120.00",
                "img6.png"
        ));

        list.add(new Product(
                "4DFWD PULSE SHOES",
                "Adidas",
                exclusionFull,
                exclusionShort,
                "$160.00",
                "img1.png"
        ));

        list.add(new Product(
                "FORUM MID SHOES",
                "Adidas",
                exclusionFull,
                exclusionShort,
                "$100.00",
                "img2.png"
        ));

        return list;
    }
}