import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainWindow extends JFrame{

    private JTextField textField6;
    private JTextArea questField;
    private JTextField textField3;
    private JTextField textField2;
    private JTextField textField1;
    private JTextField textField5;
    private JTextField textField4;
    private JRadioButton radioButton1; // 1 bis 6 in myPanel
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private JRadioButton radioButton6;
    private JRadioButton radioButton7; // 7 bis 12 in myPanel3
    private JRadioButton radioButton8;
    private JRadioButton radioButton9;
    private JRadioButton radioButton10;
    private JRadioButton radioButton11;
    private JRadioButton radioButton12;
    private JButton saveButton1;
    private JPanel myPanel; // Hinzufüge fenster
    private JTextField fileField1;
    private JLabel errorLabel; // Für das Hinzufüge fenster
    private JPanel myPanel2; // Hauptmenü
    private JButton hinzufuegenButton;
    private JButton bearbeitenButton;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JTextField textField13;
    private JPanel myPanel3; // Bearbeitungsfenster
    private JTextArea textField14;
    private JTextField textField15;
    private JButton nextButton;
    private JButton saveButton;
    private JButton suchenButton;
    private JButton loadButton;
    private JLabel errorLabel2; // Für das Bearbeitungsfenster
    private JLabel questionNum;
    private JButton zurueckZurAuswahlButton;
    private JButton zurueckZurAuswahlButton1;
    private JButton deleteButton;
    private JButton clearButton;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private final ReadWriteJson json = new ReadWriteJson();
    String dirPath = "./fragen/";

    public void launchGUI() {
        scanDir();
        setContentPane(myPanel2);
        setTitle("JsonEditor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        myPanel3.setVisible(false);
        myPanel2.setVisible(true);
        myPanel.setVisible(false);
        final boolean[] fileLoaded = {false};
        final int[] num = {0};

        // Neue Frage hinzufügen
        saveButton1.addActionListener(e -> {
            QuestionsAnswersWrapper questionsAnswersWrapper = new QuestionsAnswersWrapper();
            String file = (comboBox1.getSelectedIndex() != 0) ? Objects.requireNonNull(comboBox1.getSelectedItem()).toString() : fileField1.getText();
            json.setFileName(getFilename(file));
            boolean doubleQuestion = false;
            JsonNode questionNode;
            int questionLen;
            if (!json.fileExists()){
                try {
                    json.createEmptyFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try {
                questionNode = json.readJsonTree();
                questionLen = questionNode.get("questions").size();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            for (int i = 0; i < questionLen; i++) {
                if (questField.getText().strip().equals(questionNode.get("questions").get(String.valueOf(i)).toString().replace('"', ' ').strip())) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Diese Frage ist schon vorhanden");
                    doubleQuestion = true;
                    break;
                }
            }
            ArrayList<String> answersList = new ArrayList<>();
            // Alle möglichen Antworten werden hinzugefügt
            if (textField1.getText().length() > 0) {
                answersList.add(textField1.getText());
            }
            if (textField2.getText().length() > 0) {
                answersList.add(textField2.getText());
            }
            if (textField3.getText().length() > 0) {
                answersList.add(textField3.getText());
            }
            if (textField4.getText().length() > 0) {
                answersList.add(textField4.getText());
            }
            if (textField5.getText().length() > 0) {
                answersList.add(textField5.getText());
            }
            if (textField6.getText().length() > 0) {
                answersList.add(textField6.getText());
            }

            ArrayList<String> solutionsList = new ArrayList<>();
            // Alle möglichen Lösungen werden hinzugefügt
            if (textField1.getText().length() > 0 && radioButton1.isSelected()) {
                solutionsList.add(textField1.getText());
            }
            if (textField2.getText().length() > 0 && radioButton2.isSelected()) {
                solutionsList.add(textField2.getText());
            }
            if (textField3.getText().length() > 0 && radioButton3.isSelected()) {
                solutionsList.add(textField3.getText());
            }
            if (textField4.getText().length() > 0 && radioButton4.isSelected()) {
                solutionsList.add(textField4.getText());
            }
            if (textField5.getText().length() > 0 && radioButton5.isSelected()) {
                solutionsList.add(textField5.getText());
            }
            if (textField6.getText().length() > 0 && radioButton6.isSelected()) {
                solutionsList.add(textField6.getText());
            }

            if (doubleQuestion || questField.getText().strip().length() < 4 || answersList.size() < 1 || solutionsList.size() < 1 || fileField1.getText().strip().length() < 2) {
                if (questField.getText().strip().length() < 4) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Bitte eine Frage eingeben");
                }
                if (answersList.size() < 2) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Bitte mindestens 2 Antworten eingeben");
                }
                if (solutionsList.size() < 1) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Bitte mindestens 1 Antwort als richtig markieren");
                }
            } else {
                errorLabel.setVisible(false);
                errorLabel.setText("");
                questionsAnswersWrapper.setAnswers(answersList);
                questionsAnswersWrapper.setSolutions(solutionsList);
                questionsAnswersWrapper.setQuestions(questField.getText());

                try {
                    json.writeJson(questionsAnswersWrapper, questionLen, false);
                    setDefault();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        comboBox1.addActionListener(e -> {
            fileField1.setEnabled(comboBox1.getSelectedIndex() == 0);
        });
        // Wechselt zum Hinzufügen Modus
        hinzufuegenButton.addActionListener(e -> {
            setContentPane(myPanel);
            myPanel.setVisible(true);
            myPanel2.setVisible(false);
            myPanel3.setVisible(false);
        });
        // Wechselt zum Bearbeiten Modus
        bearbeitenButton.addActionListener(e -> {
            setContentPane(myPanel3);
            myPanel.setVisible(false);
            myPanel2.setVisible(false);
            myPanel3.setVisible(true);
        });
        // Lädt im Bearbeitungsmodus eine Datei mit der ersten Frage
        loadButton.addActionListener(e -> {
            try {
                loadQuestion(0);
                fileLoaded[0] = true;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Um beim Bearbeiten nach einer bestimmten Fragenummer zu suchen
        suchenButton.addActionListener(e -> {
            final int[] questionLen = {0};
            json.setFileName(getFilename(Objects.requireNonNull(comboBox2.getSelectedItem()).toString()));

            try {
                questionLen[0] = json.readObject("questions").size();
            } catch (NullPointerException | IOException ignored) {
            }
            if (fileLoaded[0]) {
                if (Integer.parseInt(textField15.getText()) < questionLen[0]) {
                    num[0] = Integer.parseInt(textField15.getText());
                    try {
                        loadQuestion(Integer.parseInt(textField15.getText()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }else {
                errorLabel2.setVisible(true);
                errorLabel2.setText("Bitte lade erst eine Datei");
            }
        });
        // Springt zur Nächsten Frage
        nextButton.addActionListener(e -> {
            final int[] questionLen = {0};
            json.setFileName(getFilename(Objects.requireNonNull(comboBox2.getSelectedItem()).toString()));
            try {
                questionLen[0] = json.readObject("questions").size();
            } catch (NullPointerException | IOException ignored) {}

            if (fileLoaded[0]) {
                if (num[0] + 1 < questionLen[0]) {
                    try {
                        num[0]++;
                        loadQuestion(num[0]);
                        errorLabel2.setVisible(false);
                        errorLabel2.setText("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    errorLabel2.setVisible(true);
                    errorLabel2.setText("Letzte Frage erreicht");
                }
            } else {
                errorLabel2.setVisible(true);
                errorLabel2.setText("Bitte lade erst eine Datei");
            }
        });
        // Der save button im Bearbeitungsmodus
        saveButton.addActionListener(e -> { // save a change to a question
            if (fileLoaded[0]) {
                errorLabel2.setText("");
                errorLabel2.setVisible(false);
                QuestionsAnswersWrapper questionsAnswersWrapper = new QuestionsAnswersWrapper();
                int idx = num[0];
                json.setFileName(getFilename(Objects.requireNonNull(comboBox2.getSelectedItem()).toString()));

                JsonNode questionNode;
                try {
                    questionNode = json.readJsonTree();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                ObjectNode question = (ObjectNode) questionNode.get("questions");

                int questionLen = question.size();
                boolean doubleQuestion = false;

                for (int i = 0; i < idx; i++) {
                    if (textField14.getText().strip().equals(questionNode.get("questions").get(String.valueOf(i)).toString().replace('"', ' ').strip())) {
                        errorLabel2.setVisible(true);
                        errorLabel2.setText("Diese Frage ist schon vorhanden");
                        doubleQuestion = true;
                        break;
                    }
                }

                for (int i = idx + 1; i < questionLen; i++) {
                    if (textField14.getText().strip().equals(questionNode.get("questions").get(String.valueOf(i)).toString().replace('"', ' ').strip())) {
                        errorLabel2.setVisible(true);
                        errorLabel2.setText("Diese Frage ist schon vorhanden");
                        doubleQuestion = true;
                        break;
                    }
                }

                ArrayList<String> answerList = new ArrayList<>();
                if (textField8.getText().length() > 0) {
                    answerList.add(textField8.getText().replace('"', ' ').strip());
                }
                if (textField9.getText().length() > 0) {
                    answerList.add(textField9.getText().replace('"', ' ').strip());
                }
                if (textField10.getText().length() > 0) {
                    answerList.add(textField10.getText().replace('"', ' ').strip());
                }
                if (textField11.getText().length() > 0) {
                    answerList.add(textField11.getText().replace('"', ' ').strip());
                }
                if (textField12.getText().length() > 0) {
                    answerList.add(textField12.getText().replace('"', ' ').strip());
                }
                if (textField13.getText().length() > 0) {
                    answerList.add(textField13.getText().replace('"', ' ').strip());
                }

                ArrayList<String> solutionList = new ArrayList<>();
                if (textField8.getText().length() > 0 && radioButton7.isSelected()) {
                    solutionList.add(textField8.getText().replace('"', ' ').strip());
                }
                if (textField9.getText().length() > 0 && radioButton8.isSelected()) {
                    solutionList.add(textField9.getText().replace('"', ' ').strip());
                }
                if (textField10.getText().length() > 0 && radioButton9.isSelected()) {
                    solutionList.add(textField10.getText().replace('"', ' ').strip());
                }
                if (textField11.getText().length() > 0 && radioButton10.isSelected()) {
                    solutionList.add(textField11.getText().replace('"', ' ').strip());
                }
                if (textField12.getText().length() > 0 && radioButton11.isSelected()) {
                    solutionList.add(textField12.getText().replace('"', ' ').strip());
                }
                if (textField13.getText().length() > 0 && radioButton12.isSelected()) {
                    solutionList.add(textField13.getText().replace('"', ' ').strip());
                }

                if (doubleQuestion || textField14.getText().strip().length() < 4 || answerList.size() < 1 || solutionList.size() < 1) {
                    if (textField14.getText().strip().length() < 4) {
                        errorLabel2.setVisible(true);
                        errorLabel2.setText("Bitte eine Frage eingeben");
                    }
                    if (answerList.size() < 2) {
                        errorLabel2.setVisible(true);
                        errorLabel2.setText("Bitte mindestens 2 Antworten eingeben");
                    }
                    if (solutionList.size() < 1) {
                        errorLabel2.setVisible(true);
                        errorLabel2.setText("Bitte mindestens 1 Antwort als richtig markieren");
                    }
                } else {
                    errorLabel2.setVisible(false);
                    errorLabel2.setText("");
                    questionsAnswersWrapper.setAnswers(answerList);
                    questionsAnswersWrapper.setSolutions(solutionList);
                    questionsAnswersWrapper.setQuestions(textField14.getText().replace('"', ' ').strip());

                    try {
                        json.writeJson(questionsAnswersWrapper, idx, true);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                errorLabel2.setVisible(true);
                errorLabel2.setText("Bitte erst eine Datei Laden");
            }
        });
        // Hinzufüge modus
        zurueckZurAuswahlButton.addActionListener(e -> {
            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            comboBox1.addItem("Neue Datei:");
            scanDir();
            myPanel.setVisible(false);
            myPanel2.setVisible(true);
            myPanel3.setVisible(false);
            setContentPane(myPanel2);
        });
        // Bearbeitungsmodus
        zurueckZurAuswahlButton1.addActionListener(e -> {
            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            comboBox1.addItem("Neue Datei:");
            scanDir();
            myPanel.setVisible(false);
            myPanel2.setVisible(true);
            myPanel3.setVisible(false);
            setContentPane(myPanel2);
        });
        // Für den Bearbeitungsmodus
        deleteButton.addActionListener(e -> {
            if (fileLoaded[0]) {
                int idx = num[0];
                String file = Objects.requireNonNull(comboBox2.getSelectedItem()).toString();
                json.setFileName(file);
                // Zum erstellen leerer Json nodes
                JsonNodeFactory factory = new JsonNodeFactory(false);
                // Zum Schreiben in Json
                ObjectMapper mapper = new ObjectMapper();
                // Formatierte ausgabe
                mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                // Leere Object node zum Schreiben in die Datei
                ObjectNode rootNode = factory.objectNode();

                JsonNode node;
                try {
                    node = json.readJsonTree();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // Aufteilen der node in question, answer und solution objekte
                ObjectNode question = (ObjectNode) node.get("questions");
                ObjectNode answers = (ObjectNode) node.get("answers");
                ObjectNode solutions = (ObjectNode) node.get("solutions");

                // Leere object nodes zum Zwischenspeichern
                ObjectNode answersNew = factory.objectNode();
                ObjectNode questNew = factory.objectNode();
                ObjectNode solNew = factory.objectNode();

                int questionLen = question.size();

                // Daten vom anfang bis zum zu löschenden Index werden gespeichert
                for (int i = 0; i < idx; i++) {
                    questNew.put(String.valueOf(i), question.get(String.valueOf(i)).toString().replace('"', ' ').strip());
                }

                for (int i = 0; i < idx; i++) {
                    answersNew.set(String.valueOf(i), answers.get(String.valueOf(i)));
                }
                for (int i = 0; i < idx; i++) {
                    solNew.set(String.valueOf(i), solutions.get(String.valueOf(i)));
                }

                // Daten vom gelöschten Index+1 bis zum Ende werden gespeichert
                for (int i = idx + 1; i < questionLen; i++) {
                    questNew.put(String.valueOf(i - 1), question.get(String.valueOf(i)).toString().replace('"', ' ').strip());
                }

                for (int i = idx + 1; i < questionLen; i++) {
                    answersNew.set(String.valueOf(i - 1), answers.get(String.valueOf(i)));
                }
                for (int i = idx + 1; i < questionLen; i++) {
                    solNew.set(String.valueOf(i - 1), solutions.get(String.valueOf(i)));
                }

                // Daten werden zum Speichern in die rootNode geschrieben
                rootNode.set("questions", questNew);
                rootNode.set("answers", answersNew);
                rootNode.set("solutions", solNew);

                try {
                    mapper.writeValue(new File(file), rootNode);
                    loadQuestion(idx);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        clearButton.addActionListener(e -> setDefault());
    }
    public void setDefault(){
        questField.setText("");
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");

        radioButton1.setSelected(false);
        radioButton2.setSelected(false);
        radioButton3.setSelected(false);
        radioButton4.setSelected(false);
        radioButton5.setSelected(false);
        radioButton6.setSelected(false);
    }

    public void scanDir(){
        File folder = new File(dirPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && listOfFile.toString().endsWith(".json")) {
                    comboBox1.addItem(listOfFile.getName());
                    comboBox2.addItem(listOfFile.getName());
                }
            }
        }
    }
    public String getFilename(String file){
        if (file.strip().length() > 2) {
            if (!file.endsWith(".json")) {
                file = dirPath + file.strip() + ".json";
            } else {
                file = dirPath + file.strip();
            }
        }else{
            errorLabel.setVisible(true);
            errorLabel.setText("Bitte einen Dateinamen eingeben");
        }
        return file;
    }
    public void loadQuestion(int n) throws IOException {
        try{
            errorLabel2.setVisible(false);
            errorLabel2.setText("");
            json.setFileName(getFilename(Objects.requireNonNull(comboBox2.getSelectedItem()).toString()));
            questionNum.setVisible(true);
            questionNum.setText("Frage Nummer: "+n);

            String question = json.readObject("questions", n).toString();
            ArrayList<String> antworten = json.nodeToArrayList(json.readObject("answers", n));

            switch (antworten.size()) {
                case 2 -> {
                    textField8.setText(antworten.get(0));
                    textField9.setText(antworten.get(1));
                    textField14.setText(question);
                    textField10.setText("");
                    textField11.setText("");
                    textField12.setText("");
                    textField13.setText("");
                }
                case 3 -> {
                    textField8.setText(antworten.get(0));
                    textField9.setText(antworten.get(1));
                    textField10.setText(antworten.get(2));
                    textField14.setText(question);
                    textField11.setText("");
                    textField12.setText("");
                    textField13.setText("");
                }
                case 4 -> {
                    textField8.setText(antworten.get(0));
                    textField9.setText(antworten.get(1));
                    textField10.setText(antworten.get(2));
                    textField11.setText(antworten.get(3));
                    textField14.setText(question);
                    textField12.setText("");
                    textField13.setText("");
                }
                case 5 -> {
                    textField8.setText(antworten.get(0));
                    textField9.setText(antworten.get(1));
                    textField10.setText(antworten.get(2));
                    textField11.setText(antworten.get(3));
                    textField14.setText(question);
                    textField12.setText(antworten.get(4));
                    textField13.setText("");
                }
                case 6 -> {
                    textField8.setText(antworten.get(0));
                    textField9.setText(antworten.get(1));
                    textField10.setText(antworten.get(2));
                    textField11.setText(antworten.get(3));
                    textField14.setText(question);
                    textField12.setText(antworten.get(4));
                    textField13.setText(antworten.get(5));
                }
            }

            ArrayList<String> solutions = json.nodeToArrayList(json.readObject("solutions", n));
            radioButton7.setSelected(solutions.contains(textField8.getText()));
            radioButton8.setSelected(solutions.contains(textField9.getText()));
            radioButton9.setSelected(solutions.contains(textField10.getText()));
            radioButton10.setSelected(solutions.contains(textField11.getText()));
            radioButton11.setSelected(solutions.contains(textField12.getText()));
            radioButton12.setSelected(solutions.contains(textField13.getText()));

        }catch (NullPointerException e){
            errorLabel2.setVisible(true);
            errorLabel2.setText("Bitte keine leere Datei laden");
        }catch (FileNotFoundException ee){
            errorLabel2.setVisible(true);
            errorLabel2.setText("Bitte eine existierende Datei laden");
        }
    }
}