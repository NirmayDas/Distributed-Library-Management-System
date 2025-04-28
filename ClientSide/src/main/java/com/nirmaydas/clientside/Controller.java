package com.nirmaydas.clientside;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {
    @FXML private TextField memberIdField;
    @FXML private TextField commandField;
    @FXML private Button loginButton;
    @FXML private TextField passwordField;

    @FXML private TextField newMemberIdField;
    @FXML private TextField newPasswordField;
    @FXML private Label createAccountMessage;
    @FXML private Button createAccountButton;
    @FXML private Button backToLoginButton;

    // @FXML private TextArea responseArea;
    @FXML private HBox checkedOutContainer;
    @FXML private Button sendButton;
    @FXML private Button quitButton;
    @FXML private Button viewCheckedOutButton;
    @FXML private Button listButton;
    @FXML private HBox bookContainer;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextArea feedbackArea;

    @FXML private TextField resetMemberIdField;
    @FXML private Label resetPasswordMessage;
    @FXML private Button resetPasswordButton;
    @FXML private Button forgotPasswordButton;

    private Stage reviewsPopupStage;
    private Map<String, Object> selectedBook;
    @FXML private Label reviewsTitleLabel;
    @FXML private TextArea reviewsArea;
    @FXML private TextField newReviewField;
    @FXML private Button submitReviewButton;
    @FXML private Button closeButton;
        

    private AudioClip wooshSound;
    private AudioClip buttonSound;
    private AudioClip errorSound;
    private String memberId;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;
    private Scene loginScene;
    private List<Map<String, Object>> bookList;

    public void initialize() {
        gson = new Gson();
        //audio initialization
        wooshSound = new AudioClip(getClass().getResource("sounds/woosh.mp3").toString());
        buttonSound = new AudioClip(getClass().getResource("sounds/button.wav").toString());
        errorSound = new AudioClip(getClass().getResource("sounds/error.wav").toString());
        if (sortComboBox != null) {
            ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Sort by Title",
                "Sort by Author",
                "Sort by ID"
            );
            sortComboBox.setItems(sortOptions);
            sortComboBox.setOnAction(event -> onSortSelected());
        }
        
        if (filterComboBox != null) {
            ObservableList<String> filterOptions = FXCollections.observableArrayList(
                "All Genres",
                "Fantasy",
                "Dystopian",
                "Classic",
                "Adventure",
                "Fiction",
                "Young Adult",
                "Humor",
                "Horror",
                "Science Fiction",
                "Drama" 
            );
            filterComboBox.setItems(filterOptions);
            filterComboBox.setValue("All Genres");
            filterComboBox.setOnAction(event -> onFilterSelected());
        }
    }

    //Login screen methods:
    @FXML
    private void handleLogin() {
        // buttonSound.play();
        memberId = memberIdField.getText().trim();
        String password = passwordField.getText().trim();
        if (memberId.isEmpty() || password.isEmpty()) {
            System.out.println("Member ID or password cannot be empty.");
            return;
        }

        try {
            Socket socket = new Socket("4.249.197.97", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Socket connected successfully");

            Command cmd = new Command("LOGIN", 0, memberId);
            cmd.setPassword(password);
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res == null || res.getResponse() == null || !res.getResponse().equals("Login successful")) {
                if (res != null) {
                    System.out.println("Login failed: " + res.getResponse());
                } else {
                    System.out.println("Login failed: Server disconnected");
                }
                return;
            }
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            loginScene = stage.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            loader.setController(this);
            System.out.println("Loading Main.fxml...");
            Scene mainScene = new Scene(loader.load());
            System.out.println("Main.fxml loaded, setting scene...");
            stage.setTitle("Library Client");
            stage.setScene(mainScene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            System.out.println("Scene set, stage size: " + stage.getWidth() + "x" + stage.getHeight());
            handleList();
            handleViewCheckedOut();
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showCreateAccount() {
        // buttonSound.play();
        try {
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            loginScene = stage.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
            loader.setController(this);
            Scene createAccountScene = new Scene(loader.load(), 300, 250);
            stage.setTitle("Create Account");
            stage.setScene(createAccountScene);
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error loading CreateAccount.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void showResetPassword() {
        // buttonSound.play();
        try {
            Stage stage = (Stage)forgotPasswordButton.getScene().getWindow();
            loginScene = stage.getScene();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResetPassword.fxml"));
            loader.setController(this);
            Scene resetPasswordScene = new Scene(loader.load(), 300, 250);
            stage.setTitle("Reset Password");
            stage.setScene(resetPasswordScene);
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error loading ResetPassword.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Create account screen methods:
    @FXML
    private void handleCreateAccount() {
        // buttonSound.play();
        String newMemberId = newMemberIdField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        if (newMemberId.isEmpty()) {
            createAccountMessage.setText("Member ID cannot be empty.");
            return;
        }

        try {
            Socket socket = new Socket("4.249.197.97", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Socket connected successfully for account creation");
        } catch (Exception e) {
            createAccountMessage.setText("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Command cmd = new Command("CREATE_ACCOUNT", 0, newMemberId);
        cmd.setPassword(newPassword);
        try {
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                createAccountMessage.setText("Server: " + res.getResponse());
                if (res.getResponse().equals("Account created successfully")) {
                    backToLogin();
                }
            } else {
                createAccountMessage.setText("Server disconnected");
            }
        } catch (Exception e) {
            createAccountMessage.setText("Error creating account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void backToLogin() {
        // buttonSound.play();
        Stage stage = (Stage) backToLoginButton.getScene().getWindow();
        stage.setScene(loginScene);
        stage.centerOnScreen();
    }

    //Reset password screen methods:
    @FXML
    private void handleResetPassword() {
        // buttonSound.play();
        String resetMemberId = resetMemberIdField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        if (resetMemberId.isEmpty() || newPassword.isEmpty()) {
            resetPasswordMessage.setText("Member ID and new password cannot be empty.");
            return;
        }

        try {
            Socket socket = new Socket("4.249.197.97", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Socket connected successfully for password reset");
        } catch (Exception e) {
            resetPasswordMessage.setText("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Command cmd = new Command("RESET_PASSWORD", 0, resetMemberId);
        cmd.setPassword(newPassword);
        try {
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                resetPasswordMessage.setText("Server: " + res.getResponse());
                if (res.getResponse().equals("Password reset successfully")) {
                    backToLogin();
                }
            } else {
                resetPasswordMessage.setText("Server disconnected");
            }
        } catch (Exception e) {
            resetPasswordMessage.setText("Error resetting password: " + e.getMessage());
            e.printStackTrace();
        }
    }   

    //Main Screen Methods:
    @FXML
    private void handleSend() {
        wooshSound.play();
        String command = commandField.getText().trim();
        if (command.isEmpty()) {
            feedbackArea.appendText("Please enter a command.\n");
            return;
        }

        Command cmd;
        if (command.toUpperCase().startsWith("BORROW ") || command.toUpperCase().startsWith("RETURN ")) {
            String[] parts = command.split(" ", 2);
            int itemId = Integer.parseInt(parts[1]);
            cmd = new Command(parts[0].toUpperCase(), itemId, memberId);
        } else {
            feedbackArea.appendText("Invalid command. Use BORROW <id> or RETURN <id>.\n");
            return;
        }

        try {
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                feedbackArea.appendText("Server: " + res.getResponse() + "\n");
                handleViewCheckedOut();
                handleList();
            } else {
                feedbackArea.appendText("Server disconnected\n");
            }
        } catch (Exception e) {
            feedbackArea.appendText("Error: " + e.getMessage() + "\n");
        }
        commandField.clear();        
    }

    @FXML
    private void onSortSelected() {
        String selectedOption = sortComboBox.getSelectionModel().getSelectedItem();
        if (selectedOption != null && bookList != null) {
            System.out.println("Sorting books by: " + selectedOption);
            if ("Sort by Title".equals(selectedOption)) {
                bookList.sort((a, b) ->
                    ((String) a.get("title")).compareTo((String) b.get("title"))
                );
            } else if ("Sort by Author".equals(selectedOption)) {
                bookList.sort((a, b) ->
                    ((String) a.get("author")).compareTo((String) b.get("author"))
                );
            } else if ("Sort by ID".equals(selectedOption)) {
                bookList.sort((a, b) -> {
                    Number idA = (Number) a.get("id");
                    Number idB = (Number) b.get("id");
                    return Integer.compare(idA.intValue(), idB.intValue()); 
                });
            }
            displayBooks();
        }
    }

    @FXML
    private void onFilterSelected() {
        String selectedTag = filterComboBox.getSelectionModel().getSelectedItem();
        if (selectedTag == null || bookList == null || bookList.isEmpty()) {
            System.out.println("No items to filter.\n");
            return;
        }
        List<Map<String, Object>> filteredBooks;
        if (selectedTag.equals("All Genres")) {
            filteredBooks = new ArrayList<>(bookList);
        } else {
            filteredBooks = bookList.stream()
                .filter(book -> {
                    List<String> tags = (List<String>) book.get("tags");
                    return tags != null && tags.contains(selectedTag);
                })
                .collect(Collectors.toList());
        }
        bookContainer.getChildren().clear();
        if (filteredBooks.isEmpty()) {
            System.out.println("No books found with tag: " + selectedTag + "\n");
        } else {
            List<Map<String, Object>> originalBookList = bookList;
            bookList = filteredBooks;
            displayBooks();
            bookList = originalBookList;
            System.out.println("Found " + filteredBooks.size() + " books with tag: " + selectedTag + "\n");
        }
    }

    @FXML
    private void handleList() {
        buttonSound.play();
        Command cmd = new Command("LIST");
        try {
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                System.out.println("Server: " + res.getResponse() + "\n");
            } else {
                System.out.println("Server disconnected\n");
                return;
            }
            if (res.getResponse() == null || res.getResponse().isEmpty()) {
                System.out.println("No items to display.\n");
                return;
            }
            bookContainer.getChildren().clear();
            if (res.getResponse().equals("[]")) {
                System.out.println("No items available.\n");
            } else {
                Type bookListType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                bookList = gson.fromJson(res.getResponse(), bookListType);
                searchField.clear(); //when refresh it will reset search
                filterComboBox.setValue("All Genres"); //when refreshed it will reset tag filter
                displayBooks(); 
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void displayBooks() {
        bookContainer.getChildren().clear();
        if (bookList == null || bookList.isEmpty()) {
            System.out.println("No items to display.\n");
            return;
        }
        for (Map<String, Object> book : bookList) {
            VBox bookPanel = new VBox(5);
            ImageView imageView;
            try {
                String imageUrl = (String) book.get("imageUrl");
                // System.out.println("Loading image from URL: " + imageUrl);
                Image image = new Image(imageUrl, 100, 150, true, true, true);
                imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                Button reviewsButton = new Button("Reviews");
                reviewsButton.setOnAction(event -> showReviews(book));
                bookPanel.getChildren().addAll(
                    imageView,
                    new Label("" + book.get("title")),
                    new Label("By: " + book.get("author")),
                    new Label("ID: " + book.get("id")),
                    reviewsButton
                );
                bookContainer.getChildren().add(bookPanel);
            } catch (Exception e) {
                System.out.println("Failed to load image: " + e.getMessage());
                imageView = new ImageView();
            }
        }
    }

    private void showReviews(Map<String, Object> book) {
        System.out.println("Showing reviews for book: " + book.get("title") + " (ID: " + book.get("id") + ")");
        selectedBook = book;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReviewsPopup.fxml"));
            loader.setController(this);
            VBox popupContent = loader.load();
            reviewsTitleLabel.setText("Reviews for " + book.get("title"));

            Command cmd = new Command("GET_REVIEWS");
            cmd.setItemId(((Number) book.get("id")).intValue());
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                Type reviewListType = new TypeToken<List<Map<String, String>>>(){}.getType();
                List<Map<String, String>> reviews = gson.fromJson(res.getResponse(), reviewListType);
                if (reviews.isEmpty()) {
                    reviewsArea.setText("No reviews available yet.");
                } else {
                    StringBuilder reviewsText = new StringBuilder();
                    for (Map<String, String> review : reviews) {
                        reviewsText.append(review.get("memberId")).append(": ").append(review.get("reviewText")).append("\n");
                    }
                    reviewsArea.setText(reviewsText.toString());
                }
            } else {
                reviewsArea.setText("Error: Unable to fetch reviews.");
            }

            reviewsPopupStage = new Stage();
            reviewsPopupStage.setTitle("Book Reviews");
            reviewsPopupStage.setScene(new Scene(popupContent));
            reviewsPopupStage.setResizable(false);
            reviewsPopupStage.show();
        } catch (Exception e) {
            System.out.println("Error loading ReviewsPopup.fxml or fetching reviews: " + e.getMessage());            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            displayBooks();
            return;
        }
        if (bookList == null || bookList.isEmpty()) {
            System.out.println("No items to search.\n");
            return;
        }
        List<Map<String, Object>> filteredBooks = bookList.stream()
            .filter(book -> {
                String title = ((String) book.get("title")).toLowerCase();
                String author = ((String) book.get("author")).toLowerCase();
                return title.contains(searchText) || author.contains(searchText);
            })
            .collect(Collectors.toList());
        bookContainer.getChildren().clear();
        if (filteredBooks.isEmpty()) {
            System.out.println("No books found matching: " + searchText + "\n");
        } else {
            bookList = filteredBooks;
            displayBooks();
            System.out.println("Found " + filteredBooks.size() + " books matching: " + searchText + "\n");
        }
    }

    @FXML
    private void submitReview() {
        System.out.println("Submit Review button clicked for book: " + (selectedBook != null ? selectedBook.get("title") : "unknown"));
        if (selectedBook == null || newReviewField.getText().trim().isEmpty()) {
            reviewsArea.setText("Error: Review text cannot be empty.");
            return;
        }
        try {
            Command cmd = new Command("SUBMIT_REVIEW");
            cmd.setItemId(((Number) selectedBook.get("id")).intValue());
            cmd.setMemberId(memberId);
            cmd.setCommandDetail("reviewText", newReviewField.getText().trim());
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                if (res.getResponse().equals("Review submitted successfully")) {
                    newReviewField.clear();
                    cmd = new Command("GET_REVIEWS");
                    cmd.setItemId(((Number) selectedBook.get("id")).intValue());
                    out.println(gson.toJson(cmd));
                    jsonResponse = in.readLine();
                    res = gson.fromJson(jsonResponse, Response.class);
                    if (res != null && res.getResponse() != null) {
                        Type reviewListType = new TypeToken<List<Map<String, String>>>(){}.getType();
                        List<Map<String, String>> reviews = gson.fromJson(res.getResponse(), reviewListType);
                        if (reviews.isEmpty()) {
                            reviewsArea.setText("No reviews available yet.");
                        } else {
                            StringBuilder reviewsText = new StringBuilder();
                            for (Map<String, String> review : reviews) {
                                reviewsText.append(review.get("memberId")).append(": ").append(review.get("reviewText")).append("\n");
                            }
                            reviewsArea.setText(reviewsText.toString());
                        }
                    } else {
                        reviewsArea.setText("Error: Unable to fetch reviews after submission.");
                    }
                } else {
                    reviewsArea.setText("Error: " + res.getResponse());
                }
            } else {
                reviewsArea.setText("Error: Server disconnected.");
            }
        } catch (Exception e) {
            reviewsArea.setText("Error submitting review: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void closePopup() {
        if (reviewsPopupStage != null) {
            reviewsPopupStage.close();
            reviewsPopupStage = null;
            selectedBook = null;
        }
    }

    private void handleViewCheckedOut() {
        try {
            Command cmd = new Command("GET_CHECKED_OUT");
            cmd.setItemId(0);
            cmd.setMemberId(memberId);
            out.println(gson.toJson(cmd));
            String jsonResponse = in.readLine();
            Response res = gson.fromJson(jsonResponse, Response.class);
            if (res != null && res.getResponse() != null) {
                checkedOutContainer.getChildren().clear();
                if (res.getResponse().equals("No items checked out")) {
                    System.out.println("Server: No items checked out");
                } else {
                    Type bookListType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                    List<Map<String, Object>> checkedOutBooks = gson.fromJson(res.getResponse(), bookListType);
                    for (Map<String, Object> book : checkedOutBooks) {
                        VBox bookPanel = new VBox(5);
                        ImageView imageView;
                        try {
                            String imageUrl = (String) book.get("imageUrl");
                            // System.out.println("Loading image from URL: " + imageUrl);
                            Image image = new Image(imageUrl, 100, 150, true, true, true);
                            imageView = new ImageView(image);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(150);
                            imageView.setPreserveRatio(true);
                            imageView.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                            bookPanel.getChildren().addAll(
                                imageView,
                                new Label("" + book.get("title")),
                                new Label("By: " + book.get("author")),
                                new Label("ID: " + book.get("id"))
                            );
                            checkedOutContainer.getChildren().add(bookPanel);
                        } catch (Exception e) {
                            System.out.println("Failed to load image: " + e.getMessage());
                            imageView = new ImageView();
                        }
                    }
                    System.out.println("Server: Displayed checked-out books");
                }
            } else {
                System.out.println("Server disconnected");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuit() {
        errorSound.play();
        Platform.exit();
    }
}