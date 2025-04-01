import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;

import java.io.File;

public class FileEncryptionApp extends Application {
    private File selectedFile;
    private Label fileLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Encryption and Decryption Tool");

        // Title Label
        Label titleLabel = new Label("File Encryption & Decryption Tool");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        Label descriptionLabel = new Label("Securely encrypt and decrypt your files with AES encryption.");
        descriptionLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setTextFill(Color.LIGHTGRAY);

        Label descriptionLabe2 = new Label(
                "\nSupported File Types:\n\nText-Based Files → .txt, .csv, .json, .xml, .html, .log\n" + //
                        "\n" + //
                        "Document Files → .pdf, .docx, .xlsx, .pptx\n" + //
                        "\n" + //
                        "Image Files → .jpg, .png, .bmp, .gif\n" + //
                        "\n" + //
                        "Audio/Video Files → .mp3, .wav, .mp4, .mkv\n\n");
        descriptionLabe2.setFont(Font.font("Arial", 14));
        descriptionLabe2.setTextFill(Color.LIGHTGRAY);

        VBox topSection = new VBox(10, titleLabel, descriptionLabel, descriptionLabe2);
        topSection.setAlignment(Pos.CENTER);

        // File Selection Button
        Button selectFileButton = createStyledButton("📂 Select File");
        fileLabel = new Label("No file selected");
        fileLabel.setTextFill(Color.LIGHTGRAY);
        fileLabel.setFont(Font.font("Arial", 12));

        selectFileButton.setOnAction(e -> selectFile(primaryStage));

        // Encrypt & Decrypt Buttons
        Button encryptButton = createStyledButton("🔒 Encrypt File");
        Button decryptButton = createStyledButton("🔓 Decrypt File");

        encryptButton.setOnAction(e -> encryptFile());
        decryptButton.setOnAction(e -> decryptFile());

        HBox buttonBox = new HBox(20, encryptButton, decryptButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, topSection, selectFileButton, fileLabel, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 40px; -fx-background-color: #2C3E50;");

        Scene scene = new Scene(layout, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileLabel.setText("Selected: " + selectedFile.getName());
        } else {
            fileLabel.setText("No file selected");
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: #3498DB; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #2980B9; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #3498DB; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px;"));

        button.setEffect(new DropShadow());

        return button;
    }

    private void encryptFile() {
        if (selectedFile == null) {
            showAlert("Error", "Please select a file first!");
            return;
        }

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Enter Password");
        passwordDialog.setHeaderText("Enter a password for encryption:");
        passwordDialog.setContentText("Password:");

        passwordDialog.showAndWait().ifPresent(password -> {
            try {
                File encryptedFile = new File(selectedFile.getAbsolutePath() + ".enc");
                EncryptionUtil.encryptFile(selectedFile, encryptedFile, password);
                showAlert("Success", "File encrypted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Encryption failed: " + ex.getMessage());
            }
        });
    }

    private void decryptFile() {
        if (selectedFile == null) {
            showAlert("Error", "Please select a file first!");
            return;
        }

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Enter Password");
        passwordDialog.setHeaderText("Enter the password to decrypt:");
        passwordDialog.setContentText("Password:");

        passwordDialog.showAndWait().ifPresent(password -> {
            try {
                String outputFileName = selectedFile.getAbsolutePath().replace(".enc", "");
                // String decryptedFileName = outputFile.getAbsolutePath().replace("_decrypted",
                // "");
                File decryptedFile = new File(outputFileName);
                DecryptionUtil.decryptFile(selectedFile, decryptedFile, password);
                showAlert("Success", "File decrypted successfully!");
            } catch (Exception ex) {
                showAlert("Error", "Decryption failed: " + ex.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
