package Heplers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.LinkedHashMap;

public class JsonReader {
    Gson gson;
    JsonObject jsonFile;

    /**
     * @param path : file path of json that need to be converted
     * @return the file in the format as LinkedHashMap
     */
    public LinkedHashMap convertJsonToHashMap(String path) {
        readJsonFile(path);
        return gson.fromJson(jsonFile, LinkedHashMap.class);
    }

    /**
     * @param path : json file path
     *             read json file and insert it in the jsonObject jsonFile
     */
    private void readJsonFile(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            gson = new Gson();
            jsonFile = gson.fromJson(bufferedReader, JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path: json file path
     * @return the json file in the format of json Object
     */
    public JsonObject getJsonObject(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            gson = new Gson();
            jsonFile = gson.fromJson(bufferedReader, JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonFile;
    }
}
