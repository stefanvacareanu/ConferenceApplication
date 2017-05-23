package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import model.Author;
import model.Conference;
import model.File;
import model.Sections;
import repository.AuthorsRepository;
import services.AuthorService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Dragos on 5/8/2017.
 */
public class AuthorControl
{
    AuthorsRepository repo;
    final Main loginManager;
    @FXML private ComboBox<Conference> confCombo;
    @FXML private ComboBox<Sections> sesCombo;
    @FXML private ComboBox<Author> authorCombo;
    @FXML private Button confirmButton;
    @FXML private Button addButton;
    @FXML private Button uploadButton;
    @FXML private Label abstractLabel;
    @FXML private Label proposalLabel;
    @FXML private TextArea absText;
    @FXML private TableView fileTable;
    @FXML private TextField propText;
    @FXML private TextField keyText;
    @FXML private TextField topText;
    @FXML private TableColumn<File, String> titlu;
    @FXML private TableColumn<File, String> filedoc;

    private List<Author> authorSave;
    private AuthorService service;
    private ObservableList files;
    private List<File> lista = new ArrayList<File>();
    private ObservableList<Conference> conferences;

    public AuthorControl(AuthorsRepository repo,final Main loginManager)
    {
        this.repo = repo;
        this.loginManager = loginManager;

    }
    @SuppressWarnings("unchecked")
    public void initialize()
    {
        this.service = new AuthorService(this.repo);
        lista = service.getAllFiles();
        this.files = FXCollections.observableArrayList(lista);

        files.addListener(new ListChangeListener()
        {
            @Override
            public void onChanged(Change change)
            {

            }
        });
        this.conferences = FXCollections.observableArrayList(service.getAllConf());

        confCombo.setItems(conferences);

        confCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1)
            {
                ObservableList combox2 = FXCollections.observableArrayList((List) service.findByConfId(confCombo.getValue().getIdConference()));
                sesCombo.setItems(combox2);
                List<String> lst = service.returnDeadline(confCombo.getValue().getIdConference());
                abstractLabel.setText(lst.get(0));
                proposalLabel.setText(lst.get(1));

            }
        });

        fileTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
        {
            if (newSelection != null)
            {
                keyText.setText(((File) newSelection).getKeywords().toString());
                topText.setText(((File) newSelection).getTopic());
            }
        });


        fileTable.setItems(files);
    }

    @FXML
    public void setLogoutAction()
    {
        loginManager.logOut();
    }

    @FXML
    public void setUpload()
    {
        String prop = propText.getText();
        String key = keyText.getText();
        String top = topText.getText();
        String abs = absText.getText();
    }

    @FXML
    public void setAddAuthor()
    {

    }

    @FXML
    public void setConfirm()
    {

    }

    static void showMessage(Alert.AlertType type, String header, String text)
    {
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text)
    {
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
