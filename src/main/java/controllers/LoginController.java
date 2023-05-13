package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class LoginController {


    @FXML
    private StackPane FirstStack;

    @FXML
    private TextField username_txt;

    @FXML
    private PasswordField password_txt;

    @FXML
    private Button btnLogowanie;


    private String email;
    private String password;

    private int userId;

    /**
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */

    @FXML
    void onClickLogowanie(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

         email = username_txt.getText();
         password = password_txt.getText();


        if (email.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "Wpisz nazwe uzytkownika i haslo!");
        } else {

            Connection conn = new ConnectDB().connect();
            PreparedStatement stat = conn.prepareStatement("select * from customers where cu_email=? and cu_password=?");
            stat.setString(1, email);
            stat.setString(2, password);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
               if(email.equals("ola.boch@gmail.com") && password.equals("admin")){
                   makeFadeOutCompany();
               } else {
                   makeFadeOut();
               }

            } else {
                JOptionPane.showMessageDialog(null, "Sprobuj ponownie");
            }





        }
    }

    @FXML
    void initialize() {
        assert FirstStack != null : "fx:id=\"FirstStack\" was not injected: check your FXML file 'login.fxml'.";
        assert username_txt != null : "fx:id=\"username_txt\" was not injected: check your FXML file 'login.fxml'.";
        assert password_txt != null : "fx:id=\"password_txt\" was not injected: check your FXML file 'login.fxml'.";
        assert btnLogowanie != null : "fx:id=\"btnLogowanie\" was not injected: check your FXML file 'login.fxml'.";

    }

    private void makeFadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(FirstStack);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(event -> loadChooseCustomer());
        fadeTransition.play();
    }

    private void loadChooseCustomer(){

        try {
            Parent secondView;
            secondView = (StackPane) FXMLLoader.load(getClass().getResource("/fxml/customer.fxml"));

            Scene secondScene = new Scene(secondView);
            Stage curStage = (Stage) FirstStack.getScene().getWindow();
            curStage.setScene(secondScene);

        } catch (IOException e){
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void makeFadeOutCompany() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(FirstStack);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(event -> loadChoosePlCompany());
        fadeTransition.play();
    }

    private void loadChoosePlCompany(){

        try {
            Parent secondView;
            secondView = (StackPane) FXMLLoader.load(getClass().getResource("/fxml/company.fxml"));

            Scene secondScene = new Scene(secondView);
            Stage curStage = (Stage) FirstStack.getScene().getWindow();
            curStage.setScene(secondScene);

        } catch (IOException e){
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE,null,e);
        }
    }



}


