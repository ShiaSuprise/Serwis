package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class CustomerController {


    @FXML
    private StackPane StackPane;


    @FXML
    private TextField complaint_txt;

    @FXML
    private TextField bought_txt;

    @FXML
    private TextField title_txt;

    @FXML
    private Button btnNewCom;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnMyCom;

    @FXML
    private TextArea TextArea;

    @FXML
    private Button btnLogout;

    public CustomerController() {
    }

    @FXML
    void onClickLogout(ActionEvent event) {
        makeFadeOut();
    }

    @FXML
    void onClickClear(ActionEvent event) { // anuluj

        complaint_txt.clear();
        bought_txt.clear();
        title_txt.clear();


    }

    /**
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */


    @FXML
    void onClickMyCom(ActionEvent event) throws SQLException, ClassNotFoundException { // pokaż moje reklamacje

        TextArea.clear();
        CallableStatement callableStatement = null;
        try {

            Connection conn = new ConnectDB().connect();
            PreparedStatement stat = conn.prepareStatement("SELECT concat(co_id) AS ID, " + "concat(co_name) as tytul," + "concat(co_status) as status," + "concat(co_bought) as data_zakupu," + "concat(co_today) AS data_reklamacji \n" +
                    "from complaints;");

            ResultSet rs = stat.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            result = printResultSet(rs);

            TextArea.setText("");
            for (int i = 0; i < result.size(); i++) {
                String staryText = TextArea.getText();
                TextArea.setText(staryText + result.get(i));
            }


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     *
     * @param event
     */

    @FXML
    void onClickNewCom(ActionEvent event) { //wyslanie nowej reklamacji

        CallableStatement callableStatement = null;
        try {
            if (title_txt.getText().equals("") || complaint_txt.getText().equals("") || bought_txt.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Podaj wszystkie dane");

            } else {
                Connection conn = new ConnectDB().connect();
                callableStatement = conn.prepareCall("{CALL complaint (?, ?, ?)}");
                callableStatement.setString(1, title_txt.getText());
                callableStatement.setString(2, complaint_txt.getText());
                callableStatement.setString(3, bought_txt.getText());
                callableStatement.execute();
                JOptionPane.showMessageDialog(null, "Reklamacja wyslana!");
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }



    @FXML
    void initialize() {
        assert StackPane != null : "fx:id=\"StackPane\" was not injected: check your FXML file 'customer.fxml'.";
        assert complaint_txt != null : "fx:id=\"complaint_txt\" was not injected: check your FXML file 'customer.fxml'.";
        assert title_txt != null : "fx:id=\"title_txt\" was not injected: check your FXML file 'customer.fxml'.";
        assert bought_txt != null : "fx:id=\"bought_txt\" was not injected: check your FXML file 'customer.fxml'.";
        assert btnNewCom != null : "fx:id=\"btnNewCom\" was not injected: check your FXML file 'customer.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'customer.fxml'.";
        assert btnMyCom != null : "fx:id=\"btnMyCom\" was not injected: check your FXML file 'customer.fxml'.";
        assert TextArea != null : "fx:id=\"TextArea\" was not injected: check your FXML file 'customer.fxml'.";
        assert btnLogout != null : "fx:id=\"btnLogout\" was not injected: check your FXML file 'customer.fxml'.";
    }

    private void makeFadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(StackPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(event -> loadLogin());
        fadeTransition.play();
    }

    private void loadLogin() {

        try {
            Parent secondView;
            secondView = (StackPane) FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

            Scene secondScene = new Scene(secondView);
            Stage curStage = (Stage) StackPane.getScene().getWindow();
            curStage.setScene(secondScene);

        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */

    public ArrayList<String> printResultSet(ResultSet resultSet) throws
            SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData(); // metadane o zapytaniu
        int columnsNumber = rsmd.getColumnCount(); // liczba kolumn
        ArrayList<String> ex = new ArrayList<>();
        while (resultSet.next()) { // wyswietlenie nazw kolumn i wartosci w rzedach
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    ex.add(" \t ");

                String columnValue = resultSet.getString(i);
                ex.add(rsmd.getColumnName(i) + ": " + columnValue);

            }
            ex.add("\n");
        }
        System.out.println("");
        return ex;
    }
}
