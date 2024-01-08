package step.learning.dto.models;

import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.FormParseResult;

import java.io.File;
import java.text.ParseException;
import java.util.*;

public class CharacterFormModel {

    // region fields
    private String name;
    private Integer cost;
    private String image;
    // endregion

    // region accessors
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = Integer.parseInt(cost);
    }

    public String getImage() {
        return image;
    }

    private void setImage(FormParseResult result) throws ParseException {
        Map<String, FileItem> files = result.getFiles();
        if(! files.containsKey("character-image") || files.get("character-image").getSize() == 0) {
            this.image = null;
            return;
        }
        FileItem item = files.get("character-image");
        // директорія завантаження файлів (./ - це директорія сервера (Tomcat))
        String targetDir = result.getRequest()
                .getServletContext() // контекст - "оточення" сервелету, з якого дізнаємось файлові шляхи
                .getRealPath("./upload/characters/");
        String submittedFilename = item.getName();
        // Визначити тип файлу (розширення) та перевірити на перелік дозволених
        String ext = submittedFilename.substring(submittedFilename.lastIndexOf('.'));

        String[] extentions = {".jpg", ".jpeg", ".png"};

        if (!Arrays.asList(extentions).contains(ext)) {
            this.image = null;
            return;
        }

        String savedFilename;
        File savedFile ;
        do {
            savedFilename = UUID.randomUUID().toString().substring(0,8) + ext;
            savedFile = new File(targetDir, savedFilename);
        } while (savedFile.exists());
        // Завантажуємо файл
        try {
            item.write(savedFile);
        } catch (Exception e) {
            throw new ParseException("File upload error: " + e.getMessage(), 0);
        }
        this.image = savedFilename;
    }
    // endregion

    public CharacterFormModel(FormParseResult result) throws ParseException {
        Map<String,String> fields = result.getFields();
        this.setName(fields.get("character-name"));
        this.setCost(fields.get("character-cost"));
        this.setImage(result);
    }

    public Map<String, String > getErrorMessages() {
        Map<String, String> result = new HashMap<>() ;

        if( this.name == null || "".equals( this.name ) ) {
            result.put("name", "Назва не може бути порожною");
        }

        if(this.cost < 1) {
            result.put("cost", "Вартість повинна бути більше 0");
        }

        if(this.image == null) {
            result.put("image", "Необхідно додати зображення");
        }

        return result;
    }
}
