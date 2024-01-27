import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadWriteJson {
    String fileName;
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public JsonNode readJsonTree() throws IOException {
        return new ObjectMapper().readTree(new File(this.fileName));
    }

    public boolean fileExists(){
        return new File(fileName).exists();
    }

    public boolean createEmptyFile() throws IOException {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode question = factory.objectNode();
        ObjectNode answer = factory.objectNode();
        ObjectNode solution = factory.objectNode();
        ObjectNode rootNode = factory.objectNode();

        rootNode.set("questions", question);
        rootNode.set("answers", answer);
        rootNode.set("solutions", solution);
        boolean newFile = new File(fileName).createNewFile();
        try {
            mapper.writeValue(new File(fileName), rootNode);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return newFile;
    }

    public void writeJson(QuestionsAnswersWrapper jsonData, int idx, boolean editMode) throws Exception {
        boolean fileCreated = true;
        // Zum Erstellen leerer Json Objekte
        JsonNodeFactory factory = new JsonNodeFactory(false);
        // Zum Schreiben in Json Dateien
        ObjectMapper mapper = new ObjectMapper();
        // Formatiert die Datei auf mehrere Zeilen
        mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
        if (!fileExists()){
            fileCreated = createEmptyFile();
        }
        if (!fileCreated){
            throw new Exception("Fehler beim erstellen der Datei");
        }
            // Auslesen der Json
            ObjectNode node = (ObjectNode) readJsonTree();
            // Aufteilen in question, answer und solution Objekte
            ObjectNode question = (ObjectNode) readObject("questions");
            ObjectNode solutions = (ObjectNode) readObject("solutions");
            ObjectNode answers = (ObjectNode) readObject("answers");
            // 2 leere array nodes
            ArrayNode answ = factory.arrayNode();
            ArrayNode sol = factory.arrayNode();
            // Neue Daten müssen nur an das Ende hinzugefügt werden
            if (!editMode) {
                question.put(String.valueOf(idx), jsonData.getQuestions());
                for (String i : jsonData.getAnswers()) {
                    answ.add(i);
                }
                for (String i : jsonData.getSolutions()) {
                    sol.add(i);
                }
                // Neue Antworten und Lösungen werden mit fortlaufendem index hinzugefügt
                answers.set(String.valueOf(idx), answ);
                solutions.set(String.valueOf(idx), sol);
                // Die neuen Objekte wenden zur Object node hinzugefügt
                node.set("questions", question);
                node.set("answers", answers);
                node.set("solutions", solutions);
                mapper.writeValue(new File(fileName), node);
                // Mittig in die Datei muss eingefügt werden
            }else {
                // Leere nodes erstellen
                ObjectNode answersNew = factory.objectNode();
                ObjectNode questionsNew = factory.objectNode();
                ObjectNode solutionsNew = factory.objectNode();
                // Leere Object node zum Schreiben in die Datei
                ObjectNode rootNode = factory.objectNode();

                // Die Daten vom anfang bis zu dem Index der bearbeitet wird, werden zu den leeren object nodes hinzugefügt
                for (int i = 0; i < idx; i++) {questionsNew.put(String.valueOf(i), question.get(String.valueOf(i)).toString().replace('"', ' ').strip());}
                for (int i = 0; i < idx; i++) {answersNew.set(String.valueOf(i), answers.get(String.valueOf(i)));}
                for (int i = 0; i < idx; i++) {solutionsNew.set(String.valueOf(i), solutions.get(String.valueOf(i)));}

                int questionLen = question.size();

                // Die bearbeiteten Daten werden in leere array nodes hinzugefügt
                for (String i : jsonData.getSolutions()) {
                    sol.add(i);
                }

                for (String i : jsonData.getAnswers()) {
                    answ.add(i);
                }

                // bearbeitete Daten zu object nodes hinzufügen
                questionsNew.put(String.valueOf(idx), jsonData.getQuestions());
                answersNew.set(String.valueOf(idx), answ);
                solutionsNew.set(String.valueOf(idx), sol);

                // Die restlichen Daten vom bearbeiteten Index bis zum Ende werden hinzugefügt
                for (int i = idx + 1; i < questionLen; i++) {
                    questionsNew.put(String.valueOf(i), question.get(String.valueOf(i)).toString().replace('"', ' ').strip());
                }

                for (int i = idx + 1; i < questionLen; i++) {
                    answersNew.set(String.valueOf(i), answers.get(String.valueOf(i)));
                }
                for (int i = idx + 1; i < questionLen; i++) {
                    solutionsNew.set(String.valueOf(i), solutions.get(String.valueOf(i)));
                }

                // root node wird mit den zu schreibenden Daten befüllt
                rootNode.set("questions", questionsNew);
                rootNode.set("answers", answersNew);
                rootNode.set("solutions", solutionsNew);

                mapper.writeValue(new File(fileName), rootNode);
            }
    }
    public ArrayList<String> nodeToArrayList(JsonNode node){
        ArrayList<String> arrayList = new ArrayList<>();
        for (JsonNode i : node) {
            arrayList.add(i.toString().replace('"', ' ').strip());
        }
        return arrayList;
    }

    public JsonNode readObject(String obj) throws IOException {

        return readJsonTree().get(obj);
    }

    public JsonNode readObject(String obj, int n) throws IOException {

        return readJsonTree().get(obj).get(String.valueOf(n));
    }
}
