package com.poch.asynctask.pdf417.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkbarcode.BarcodeType;
import com.microblink.recognizers.blinkbarcode.bardecoder.BarDecoderRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.bardecoder.BarDecoderScanResult;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.blinkbarcode.usdl.USDLRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.usdl.USDLScanResult;
import com.microblink.recognizers.blinkbarcode.zxing.ZXingRecognizerSettings;
import com.microblink.recognizers.blinkbarcode.zxing.ZXingScanResult;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.barcode.BarcodeDetailedData;
import com.microblink.util.Log;
import com.microblink.view.recognition.RecognizerView;
import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.models.Bitacora;
import com.poch.asynctask.pdf417.models.Persona;
import com.poch.asynctask.pdf417.models.Visita;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;


public class MainActivity extends ActionBarActivity {



    // demo license key for package com.microblink.barcode
    // obtain your licence key at http://microblink.com/login or
    // contact us at http://help.microblink.com
    private static final String LICENSE_KEY = "Y4EK2LGT-5IFAIYGP-Y7VVNE25-7JODFELW-73OV6F37-PPFISAJM-2IIGFGPD-AZQN5FIV";

    private static final int MY_REQUEST_CODE = 1337;

    private static final String TAG = "Pdf417MobiDemo";


    private SharedPreferences sp ;


    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getInstance(this);
        sp = getSharedPreferences("userData", Context.MODE_PRIVATE);

        if(   sp.contains("rut")  && !TextUtils.isEmpty(sp.getString("rut",""))   ){

            Intent intent = new Intent(MainActivity.this, ListaBitacora.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * Builds string which contains information about application version and library version.
     * @return String which contains information about application version and library version.
     */
    private String buildVersionString() {
        String nativeVersionString = RecognizerView.getNativeLibraryVersionString();
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = pInfo.versionName;
            int appVersionCode = pInfo.versionCode;

            StringBuilder infoStr = new StringBuilder();
            infoStr.append("Application version: ");
            infoStr.append(appVersion);
            infoStr.append(", build ");
            infoStr.append(appVersionCode);
            infoStr.append("\nLibrary version: ");
            infoStr.append(nativeVersionString);
            return infoStr.toString();
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public void btnScan_click(View v) {

        // Intent for ScanActivity
        Intent intent = new Intent(this, Pdf417ScanActivity.class);

        // If you want sound to be played after the scanning process ends,
        // put here the resource ID of your sound file. (optional)
        intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        // In order for scanning to work, you must enter a valid licence key. Without licence key,
        // scanning will not work. Licence key is bound the the package name of your app, so when
        // obtaining your licence key from Microblink make sure you give us the correct package name
        // of your app. You can obtain your licence key at http://microblink.com/login or contact us
        // at http://help.microblink.com.
        // Licence key also defines which recognizers are enabled and which are not. Since the licence
        // key validation is performed on image processing thread in native code, all enabled recognizers
        // that are disallowed by licence key will be turned off without any error and information
        // about turning them off will be logged to ADB logcat.
        intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);
        // If you want to open front facing camera, uncomment the following line.
        // Note that front facing cameras do not have autofocus support, so it will not
        // be possible to scan denser and smaller codes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable) CameraType.CAMERA_FRONTFACE);

        // You need to define array of recognizer settings. There are 4 types of recognizers available
        // in PDF417.mobi SDK.

        // Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
        Pdf417RecognizerSettings pdf417RecognizerSettings = new Pdf417RecognizerSettings();
        // Set this to true to scan barcodes which don't have quiet zone (white area) around it
        // Use only if necessary because it drastically slows down the recognition process
        pdf417RecognizerSettings.setNullQuietZoneAllowed(true);
        // Set this to true to scan even barcode not compliant with standards
        // For example, malformed PDF417 barcodes which were incorrectly encoded
        // Use only if necessary because it slows down the recognition process
//        pdf417RecognizerSettings.setUncertainScanning(true);

        // BarDecoderRecognizerSettings define settings for scanning 1D barcodes with algorithms
        // implemented by Microblink team.
        BarDecoderRecognizerSettings oneDimensionalRecognizerSettings = new BarDecoderRecognizerSettings();
        // set this to true to enable scanning of Code 39 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode39(true);
        // set this to true to enable scanning of Code 128 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode128(true);
        // set this to true to use heavier algorithms for scanning 1D barcodes
        // those algorithms are slower, but can scan lower resolution barcodes
//        oneDimensionalRecognizerSettings.setTryHarder(true);

        // USDLRecognizerSettings define settings for scanning US Driver's Licence barcodes
        // options available in that settings are similar to those in Pdf417RecognizerSettings
        // if license key does not allow scanning of US Driver's License, this settings will
        // be thrown out from settings array and error will be logged to logcat.
        USDLRecognizerSettings usdlRecognizerSettings = new USDLRecognizerSettings();

        // ZXingRecognizerSettings define settings for scanning barcodes with ZXing library
        // We use modified version of ZXing library to support scanning of barcodes for which
        // we still haven't implemented our own algorithms.
        ZXingRecognizerSettings zXingRecognizerSettings = new ZXingRecognizerSettings();
        // set this to true to enable scanning of QR codes
        zXingRecognizerSettings.setScanQRCode(true);
        zXingRecognizerSettings.setScanITFCode(true);

        // finally, when you have defined settings for each recognizer you want to use,
        // you should put them into array held by global settings object

        RecognitionSettings recognitionSettings = new RecognitionSettings();
        // add settings objects to recognizer settings array
        // Pdf417Recognizer, BarDecoderRecognizer, USDLRecognizer and ZXingRecognizer
        //  will be used in the recognition process
        recognitionSettings.setRecognizerSettingsArray(
                new RecognizerSettings[]{pdf417RecognizerSettings, oneDimensionalRecognizerSettings,
                        usdlRecognizerSettings, zXingRecognizerSettings});

        // additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process

        // set this to true to enable returning of multiple scan results from single camera frame
        // default is false, which means that as soon as first barcode is found (no matter which type)
        // its contents will be returned.
        recognitionSettings.setAllowMultipleScanResultsOnSingleImage(true);

        // finally send that settings object over intent to scan activity
        // use Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS to set recognizer settings
        intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS, recognitionSettings);

        // if you do not want the dialog to be shown when scanning completes, add following extra
        // to intent
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);

        // if you want to enable pinch to zoom gesture, add following extra to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM, true);

        // if you want Pdf417ScanActivity to display rectangle where camera is focusing,
        // add following extra to intent
        //intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE, true);


        // disable showing of dialog after scan
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);

        // if you want to use camera fit aspect mode to letterbox the camera preview inside
        // available activity space instead of cropping camera frame (default), add following
        // extra to intent.
        // Camera Fit mode does not look as nice as Camera Fill mode on all devices, especially on
        // devices that have very different aspect ratios of screens and cameras. However, it allows
        // all camera frame pixels to be processed - this is useful when reading very large barcodes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_ASPECT_MODE, (Parcelable) CameraAspectMode.ASPECT_FIT);

        // Start Activity
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    /**
     * Checks whether data is URL and in case of URL data creates intent and starts activity.
     * @param data String to check.
     * @return If data is URL returns {@code true}, else returns {@code false}.
     */
    private boolean checkIfDataIsUrlAndCreateIntent(String data) {
        // if barcode contains URL, create intent for browser
        // else, contain intent for message
        boolean barcodeDataIsUrl;
        try {
            @SuppressWarnings("unused")
            URL url = new URL(data);
            barcodeDataIsUrl = true;
        } catch (MalformedURLException exc) {
            barcodeDataIsUrl = false;
        }

        if (barcodeDataIsUrl) {
            // create intent for browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data));
            startActivity(intent);
        }
        return barcodeDataIsUrl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        final SharedPreferences.Editor editor = sp.edit();
        RecognitionResults results = data.getParcelableExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULTS);
        // Get scan results array. If scan was successful, array will contain at least one element.
        // Multiple element may be in array if multiple scan results from single image were allowed in settings.
        BaseRecognitionResult[] resultArray = results.getRecognitionResults();

        final StringBuilder sb = new StringBuilder();

        for(BaseRecognitionResult res : resultArray) {
            if(res instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer


                Pdf417ScanResult result = (Pdf417ScanResult) res;
                // getStringData getter will return the string version of barcode contents
                String barcodeData = result.getStringData();
                // isUncertain getter will tell you if scanned barcode contains some uncertainties
                boolean uncertainData = result.isUncertain();
                // getRawData getter will return the raw data information object of barcode contents
                BarcodeDetailedData rawData = result.getRawData();
                // BarcodeDetailedData contains information about barcode's binary layout, if you
                // are only interested in raw bytes, you can obtain them with getAllData getter
                byte[] rawDataBuffer = rawData.getAllData();

                // if data is URL, open the browser and stop processing result
                if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                    return;
                } else {
                    // add data to string builder
                    //sb.append("PDF417 scan data");
                    if (uncertainData) {
                        // sb.append("This scan data is uncertain!\n\n");
                    }
                    //  sb.append(" string data:\n");
                    sb.append(barcodeData);
                    if (rawData != null) {
                        sb.append("\n\n");
                    }
                }



                  /*
            * Guardamos en peferencias el nombre d ela persona
            * Como el nombre que se obtiene del carnet es solo el apellido damos al opcion de modifcar el nombre solo una vez
            * si ya existe el rut simplemente pasara sin rpeguntar por cambio nombre
            * */




                String[] splited = sb.toString().split("\\s");
                String nombre = "";
                String rut = "";

                for (int x=0;x<splited.length;x++){

                    if (x == 1) {
                        nombre =  splited[x].replaceAll("[^a-zA-Z0-9]", "");
                    }
                    if (x == 0) {
                        rut = splited[x].replaceAll("[^a-zA-Z0-9]", "");
                        editor.putString("rut",  rut.substring(0, 9) );
                        editor.apply();
                    }


                }

                nombre =nombre.split("CHL")[0];

                Persona per =realm.where(Persona.class)
                        .equalTo("rut", rut.substring(0, 9))
                        .findFirst();


                if( per == null ){

                    LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);


                    View promptView = layoutInflater.inflate(R.layout.form_nombre, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                    final EditText nombreActual = (EditText) promptView.findViewById(R.id.nombre);

                    final String rutCurrent = rut.substring(0, 9);

                    nombreActual.setText(nombre);

                    nombreActual.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    alertDialogBuilder.setView(promptView);
                    // setup a dialog window
                    alertDialogBuilder
                            .setTitle("Modificar Nombre")
                            .setCancelable(false)

                            .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // Guardamos en al abse de datos para no teenr que preguntar de nnuevo
                                    realm.beginTransaction();

                                    String uid = UUID.randomUUID().toString();
                                    Persona p = new Persona();
                                    p.setId(uid);
                                    p.setNombre(nombreActual.getText().toString());
                                    p.setRut(rutCurrent);
                                    realm.copyToRealmOrUpdate(p);
                                    realm.commitTransaction();
                                    realm.close();

                                    editor.putString("nombre", nombreActual.getText().toString());
                                    editor.apply();

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                    Intent intent = new Intent(MainActivity.this, ListaBitacora.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                    // create an alert dialog
                    AlertDialog alertD = alertDialogBuilder.create();
                    alertD.show();


                }else{

                    editor.putString("nombre", nombre);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, ListaBitacora.class);
                    startActivity(intent);
                    finish();
                }



            } else if(res instanceof BarDecoderScanResult) { // BarDecoder recognizer

                Toast.makeText(MainActivity.this, "BarDecoder!!! =)",
                        Toast.LENGTH_LONG).show();


            } else if(res instanceof ZXingScanResult) { // resultado si es un qr de carnet nuevo


                ZXingScanResult result= (ZXingScanResult) res;
                // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                BarcodeType type = result.getBarcodeType();

                String barcodeData = result.getStringData();


                try {
                    Map<String, String> map =  getQueryMap(barcodeData);


                    editor.putString("rut",  map.get("RUN") );
                    editor.apply();


                    Persona per =realm.where(Persona.class)
                            .equalTo("rut", map.get("RUN"))
                            .findFirst();


                    if( per == null ){

                        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);


                        View promptView = layoutInflater.inflate(R.layout.form_nombre, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                        final EditText nombreActual = (EditText) promptView.findViewById(R.id.nombre);

                        final String rutCurrent = map.get("RUN");

                        nombreActual.setText("");

                        nombreActual.requestFocus();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        alertDialogBuilder.setView(promptView);
                        // setup a dialog window
                        alertDialogBuilder
                                .setTitle("Modificar Nombre")
                                .setCancelable(false)

                                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // Guardamos en al abse de datos para no teenr que preguntar de nnuevo
                                        realm.beginTransaction();

                                        String uid = UUID.randomUUID().toString();
                                        Persona p = new Persona();
                                        p.setId(uid);
                                        p.setNombre(nombreActual.getText().toString());
                                        p.setRut(rutCurrent);
                                        realm.copyToRealmOrUpdate(p);
                                        realm.commitTransaction();
                                        realm.close();

                                        editor.putString("nombre", nombreActual.getText().toString());
                                        editor.apply();

                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                        Intent intent = new Intent(MainActivity.this, ListaBitacora.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                        // create an alert dialog
                        AlertDialog alertD = alertDialogBuilder.create();
                        alertD.show();


                    }else{

                        Intent intent = new Intent(MainActivity.this, ListaBitacora.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



            } else if(res instanceof USDLScanResult) { //   US Driver's Licence recognizer
                Toast.makeText(MainActivity.this, " US Driver!!! =)",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }



    private Map<String, String> getQueryMap(String query) throws UnsupportedEncodingException, Exception {
        String s = query;

        URL aURL = new URL(s);

        String[] params = aURL.getQuery().split("&");

        Map<String, String> map = new HashMap<String, String>();

        for (String param : params){

            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);

        }
        return map;
    }
}