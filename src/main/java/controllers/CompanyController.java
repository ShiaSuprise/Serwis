package controllers;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
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

public class CompanyController {


    @FXML
    private StackPane CompanyStack;

    @FXML
    private Button btnStats;

    @FXML
    private Button btnView;

    @FXML
    private Button btnChange;

    @FXML
    private Button btnAll;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea textArea2;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnID;

    @FXML
    private Button btnDay;

    @FXML
    private TextField Date_txt;

    @FXML
    private TextField IDZ_txt;

    @FXML
    private TextField IDS_txt;

    @FXML
    private TextField NewStatus_txt;

    /**
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    @FXML
    void onClickAll(ActionEvent event) throws SQLException, ClassNotFoundException { // pokaż moje rezerwacje

        textArea2.clear();
        CallableStatement callableStatement = null;
        try {

            Connection conn = new ConnectDB().connect();
            PreparedStatement stat = conn.prepareStatement("SELECT concat(co_id) AS ID, " + "concat(co_status) as status \n" +
                    "from complaints;");

            ResultSet rs = stat.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            result = printResultSet(rs);

            textArea2.setText("");
            for (int i = 0; i < result.size(); i++) {
                String staryText = textArea2.getText();
                textArea2.setText(staryText + result.get(i));
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
    void onClickChange(ActionEvent event) { //zmiana statusu reklamacji przez pracownika

        CallableStatement callableStatement = null;
        try {
            if (IDS_txt.getText().equals("") || NewStatus_txt.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Podaj wszystkie dane");

            } else {
                Connection conn = new ConnectDB().connect();
                callableStatement = conn.prepareCall("{CALL statusChange (?, ?)}");
                callableStatement.setString(1, IDS_txt.getText());
                callableStatement.setString(2, NewStatus_txt.getText());
                callableStatement.execute();
                JOptionPane.showMessageDialog(null, "Status zmieniony!");
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
    void onClickStats(ActionEvent event) { // statystyki

        textArea.clear();
        CallableStatement callableStatement = null;
        try {

            Connection conn = new ConnectDB().connect();
            PreparedStatement stat = conn.prepareStatement("SELECT concat(s_all) AS Wszystkie, " + "concat(s_send) AS Wyslane, \n" +
                            "concat(s_remade) AS Do_naprawy, " +  "concat(s_exchanged) AS Wymiana, " + "concat(s_rejected) as Odrzucone \n" +
                    "from stats;");

            ResultSet rs = stat.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            result = printResultSet(rs);

            textArea.setText("");
            for (int i = 0; i < result.size(); i++) {
                String staryText = textArea.getText();
                textArea.setText(staryText + result.get(i));
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
    void onClickDay(ActionEvent event) { // dzien do statystyk

        CallableStatement callableStatement = null;
        try {
            if (Date_txt.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Podaj wszystkie dane");

            } else {
                Connection conn = new ConnectDB().connect();
                callableStatement = conn.prepareCall("{CALL stats (?)}");
                callableStatement.setString(1, Date_txt.getText());
                callableStatement.execute();
                JOptionPane.showMessageDialog(null, "Data wybrana!");
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void onClickLogout(ActionEvent event) {
        makeFadeOut();
    }

    /**
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    @FXML
    void onClickID(ActionEvent event) throws SQLException, ClassNotFoundException { // wybiera id reklamacji w celu analizy przyczyny

        CallableStatement callableStatement = null;
        try {
            if (IDZ_txt.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Podaj wszystkie dane");

            } else {
                Connection conn = new ConnectDB().connect();
                callableStatement = conn.prepareCall("{CALL complaintView (?)}");
                callableStatement.setString(1, IDZ_txt.getText());
                callableStatement.execute();
                JOptionPane.showMessageDialog(null, "Reklamacja wybrana!");
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    @FXML
    void onClickView(ActionEvent event) throws SQLException, ClassNotFoundException { // pokazuje przyczyne reklamacji

        textArea.clear();
        CallableStatement callableStatement = null;
        try {

            Connection conn = new ConnectDB().connect();
            PreparedStatement stat = conn.prepareStatement("SELECT concat(coV_name) AS Nazwa," + "concat(coV_today) AS Data_reklamacji," + "concat(coV_text) AS Text " + "from complaintsView;");

            ResultSet rs = stat.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            result = printResultSet(rs);

            textArea.setText("");
            for (int i = 0; i < result.size(); i++) {
                String staryText = textArea.getText();
                textArea.setText(staryText + result.get(i));
            }


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    void initialize() {
        assert CompanyStack != null : "fx:id=\"CompanyStack\" was not injected: check your FXML file 'company.fxml'.";
        assert btnStats != null : "fx:id=\"btnStats\" was not injected: check your FXML file 'company.fxml'.";
        assert btnAll != null : "fx:id=\"btnAll\" was not injected: check your FXML file 'company.fxml'.";
        assert btnView != null : "fx:id=\"btnView\" was not injected: check your FXML file 'company.fxml'.";
        assert btnID != null : "fx:id=\"btnID\" was not injected: check your FXML file 'company.fxml'.";
        assert btnChange != null : "fx:id=\"btnChange\" was not injected: check your FXML file 'company.fxml'.";
        assert btnDay != null : "fx:id=\"btnDay\" was not injected: check your FXML file 'company.fxml'.";
        assert textArea != null : "fx:id=\"textArea\" was not injected: check your FXML file 'company.fxml'.";
        assert textArea2 != null : "fx:id=\"textArea2\" was not injected: check your FXML file 'company.fxml'.";
        assert btnLogout != null : "fx:id=\"btnLogout\" was not injected: check your FXML file 'company.fxml'.";
        assert Date_txt != null : "fx:id=\"Date_txt\" was not injected: check your FXML file 'company.fxml'.";
        assert IDZ_txt != null : "fx:id=\"IDZ_txt\" was not injected: check your FXML file 'company.fxml'.";
        assert IDS_txt != null : "fx:id=\"IDS_txt\" was not injected: check your FXML file 'company.fxml'.";
        assert NewStatus_txt != null : "fx:id=\"NewStatus_txt\" was not injected: check your FXML file 'company.fxml'.";

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


    private void makeFadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(CompanyStack);
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
            Stage curStage = (Stage) CompanyStack.getScene().getWindow();
            curStage.setScene(secondScene);

        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
