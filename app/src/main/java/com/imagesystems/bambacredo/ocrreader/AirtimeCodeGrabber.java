package com.imagesystems.bambacredo.ocrreader;

import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP21 on 24/05/2017.
 */

public class AirtimeCodeGrabber {

    public AirtimeCodeGrabber(){

    }

    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        //mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        List<String> validCodes = new ArrayList<String>(); ;
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            String validCode = detectAirtimeCodes(item);
            Uri airtimeUri = ussdAirtimeUri(validCode);
        }
    }

    public String detectAirtimeCodes (TextBlock item) {
        if (item != null && item.getValue() != null) {
            List<? extends Text> textComponents = item.getComponents();
            for (Text currentTextBlock : textComponents) {
                List<? extends Text> pTextLineItems = currentTextBlock.getComponents();
                if (pTextLineItems.size() == 4) { //Come in fours
                    for (Text currentWord : pTextLineItems) {
                       // OcrGraphicWord graphic = new OcrGraphicWord(mGraphicOverlay, currentWord);
                    //    mGraphicOverlay.add(graphic);
                    }
                    if (validateAirtimeCodes(currentTextBlock)) {
                        return currentTextBlock.getValue().trim().replaceAll("\\s", "");
                    }
                }
            }
        }
        return null;
        //Discard everything not starting with int
        //String txt = pText.getValue().trim().replaceAll("\\s","");
//        List<? extends Text> textComponents = pText.getComponents();
//        for(Text currentTextLine : textComponents){
//            currentTextLine.getComponents()
//        }
//
//        Log.e("Processor", "XUM integer! " + pText.getValue());
//        try{
//            Integer.parseInt(txt);
//
//        }catch (NumberFormatException e) {
//            Log.e("Processor", "Not integer! " + pText.getValue());
//            return false;
//        }

//        String[] parts = pText.toString().split(" ");
//        for(int z = 0; z < parts.length; z++) {
//            try{
//                Integer.parseInt(parts[z]);
//            }catch (NumberFormatException e) {
//                Log.e("Processor", "Not integer! " + pText.getValue());
//            }
//        }
//        Log.i("Processor", "Text detected! " + pText.getValue());
    }

    public boolean validateAirtimeCodes (Text tParts) {
        //TODO: Check the codes intelligently
//        List<? extends Text> pTextLineItems = tParts.getComponents();
//        for (Text codePart: pTextLineItems) {//4 part code
//            //4 digit code
//            if(codePart.getValue().length() != 4){
//                return false;
//            }
//            String partialCode = codePart.getValue();
//            //Should be int
//            try{
//                Integer.parseInt(partialCode);
//            }catch (NumberFormatException e) {
//                Log.e("Processor", "Not integer! " + partialCode);
//                return false;
//            }
//        }
        String cCode = tParts.getValue().trim().replaceAll("\\s","");
        Log.e("Processor", "YAY integer! " + cCode);
        if(cCode.length() == 16){
            try{
                int intVal = Integer.parseInt(cCode);
                Log.e("Processor", "YAY integer! " + intVal);
            }catch (NumberFormatException e) {
                Log.e("Processor", "Not integer! " + cCode);
                return false;
            }
            return true;
        }
        return false;
    }

    public Uri ussdAirtimeUri(String validCode){
        //Send to safricom
        String uriString = "";
        validCode = "*141*"+validCode+"#";
        if(!validCode.startsWith("tel:"))
            uriString += "tel:";

        for(char c : validCode.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }
}
