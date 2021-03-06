package dzieniu.minesweeper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class GameSaver {

    private static final String GAME_STATE = "minesweeperSavedGameState.txt";

    public static String readFromFile(String filename, Context context) {

        String content = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                content = stringBuilder.toString();
            }
        }
        catch (IOException e) {}

        return content;
    }

    public static void writeToFile(String data,String filename,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void saveGameState(int height, int width, int mines, long time, int defuses, int minesLeft, Field[][] minefield, Context context){

        String save = height + "/" + width + "/" + mines + "/" + time + "/" + defuses + "/" + minesLeft + "/";
        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++) {
                if(!minefield[i][j].getContent().matches("x") && minefield[i][j].getClicked()==0){
                    save = save+"0";
                }else if(!minefield[i][j].getContent().matches("x") && minefield[i][j].getClicked()==1){
                    save = save+"1";
                }else if(minefield[i][j].getContent().matches("x") && minefield[i][j].getClicked()==0){
                    save = save+"2";
                }else if(minefield[i][j].getContent().matches("x") && minefield[i][j].getClicked()==2){
                    save = save+"3";
                }else if(!minefield[i][j].getContent().matches("x") && minefield[i][j].getClicked()==2){
                    save = save+"4";
                }
            }
        }
        writeToFile(save,GAME_STATE,context);
    }

    public static Map<String, Integer> readSave(Context context){

        int counter = 0;
        String save = readFromFile(GAME_STATE, context);
        String sHeight = "";
        String sWidth = "";
        String sMines = "";
        String sTime = "";
        String sDefuses = "";
        String minesLeft = "";

        Log.d("none", save);

        for (;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                sHeight = sHeight + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }
        for(;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                sWidth = sWidth + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }
        for(;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                sMines = sMines + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }
        for(;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                sTime = sTime + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }
        for(;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                sDefuses = sDefuses + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }
        for(;;) {
            if (!(save.charAt(counter)+"").matches("/")) {
                minesLeft = minesLeft + save.charAt(counter);
                counter++;
            } else {
                counter++;
                break;
            }
        }

        Map<String, Integer> savedData = new HashMap<>();
        savedData.put("height", Integer.parseInt(sHeight));
        savedData.put("width", Integer.parseInt(sWidth));
        savedData.put("mines", Integer.parseInt(sWidth));
        savedData.put("time", Integer.parseInt(sTime));
        savedData.put("defuses", Integer.parseInt(sDefuses));
        savedData.put("minesLeft", Integer.parseInt(minesLeft));
        savedData.put("counter", counter);

        return savedData;
    }
}
