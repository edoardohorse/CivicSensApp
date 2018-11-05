package com.civic.gercs.civicsense;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.civic.gercs.civicsense.Sender.Report;
import com.civic.gercs.civicsense.Sender.Service;
import com.civic.gercs.civicsense.Sender.ServiceGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.civic.gercs.civicsense.MainActivity.hasPermissions;
import static com.civic.gercs.civicsense.MainActivity.managerReport;
import static com.civic.gercs.civicsense.SearchActivity.hideKeyboard;

//import static com.example.layout_segnalazione.UserUtils.RC_SIGN_IN;

public class UserReportActivity extends AppCompatActivity{


    CoordinatorLayout           coordinatorLayout;
    private CardView            mCardPhoto;
    private CardView            mCardDescription;
    private LayoutInflater      inflater;
    public  ArrayList<Photo>    photos = new ArrayList<>();
    private Photo               photoSelected;
    private ImageFull           imageFullFragment;
    public  Button              mButtonSendReport;
    private EditText            mEditTextDescription;
    private String              mCurrentPhotoPath;
    private Spinner             mSpinnerGrade;
    private Spinner             mSpinnerType;
    private boolean             withEmail = false;
    ViewPropertyTransition.Animator animationObject;
//    WebView webView;
    BottomSheetFragment         bottomSheetFragment;
    ProgressDialog              progressDialog;
    Boolean cardPhotoCompiled = false;
    Boolean cardDescriptionCompiled = false;

//    private User                user = new User();
    private Report              report = new Report();
    private String              city;
    private String              address;
    private String              grade;
    private String              description;
    private List<MultipartBody.Part> parts;
    private int                 typeReport;
    private String              email = "";

    private List<Report.TypeReport> listTypeReport;

    private ImageView mImageView;


    public static final int RC_PHOTO = 9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        inflater = LayoutInflater.from(this);

        report = (Report) getIntent().getSerializableExtra("report");
        city = report.getCity();

//        webView = (WebView) findViewById(R.id.web);
//        showCards();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        init();
    }

    private void init(){
        mImageView      = (ImageView)           findViewById(R.id.image);

        mButtonSendReport = (Button)              findViewById(R.id.button_report);

        mCardPhoto      = (CardView)            findViewById(R.id.cardview_photo);
        mCardDescription= (CardView)            findViewById(R.id.cardview_description);

        mEditTextDescription = (EditText)       findViewById(R.id.editext_description);

        photos = new ArrayList<>();

        bottomSheetFragment =                   new BottomSheetFragment();

        coordinatorLayout   =   (CoordinatorLayout) findViewById(R.id.coordinator);

        mSpinnerGrade = (Spinner) findViewById(R.id.spinner_grade);
        mSpinnerType = (Spinner) findViewById(R.id.spinner_type_report);

        progressDialog = new ProgressDialog(UserReportActivity.this);
        progressDialog.setMessage("Sto caricando....");
        progressDialog.setTitle("Upload report");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(300);
                fadeAnim.start();
            }
        };



        ImageView delete = (ImageView) findViewById(R.id.button_delete_image);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoto(photos.indexOf(photoSelected));
                photoSelected = null;
            }
        });

        mButtonSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareReportToSend();
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grade_report, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerGrade.setAdapter(adapter);

        listTypeReport = managerReport.getListTypeOfReport();

        String[] arr = new String[listTypeReport.size()];
        for (int i = 0; i < listTypeReport.size(); i++) {
            arr[i] = listTypeReport.get(i).getName();
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(adapter2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
            return;
        }
        switch (requestCode){
            case RC_PHOTO:{
                if(data != null && data.getData() != null){
                    populateLayoutPhoto(data);
                }
                else {
                    setPic();
                }
                break;
            }
        }
    }

    private void populateLayoutPhoto(Intent data){

        ClipData mClipData=data.getClipData();
        ArrayList<Uri> mArrayUri=new ArrayList<Uri>();
        for(int i=0;i<mClipData.getItemCount();i++) {
            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            photos.add(new Photo(uri));

         }
        populateImages();
    }

    private void populateImages(){
//        clearImages();
        LinearLayout horizontalScrollView = (LinearLayout) findViewById(R.id.layout_photos);
        View parent = (View) horizontalScrollView.getParent();

        final int width = parent.getWidth();
        final int height = parent.getHeight();

        for( Photo photo : photos ){
            if( photo.isAlreadyShown ){
                continue;
            }
            View cardPhoto = inflater.inflate(R.layout.card_view_layout, null);
            ImageView imageView = (ImageView) cardPhoto.findViewById(R.id.imageview_photo);
            horizontalScrollView.addView(cardPhoto);

//            horizontalScrollView.addView(cardPhoto);
            photo.setView(imageView);
            photo.isAlreadyShown = true;

            GlideApp.with(this)
                    .load(photo.uri)
//                    .placeholder(R.drawable.ic_loading_blink)
                    .override(width, height)
                    .fitCenter()
                    .skipMemoryCache(false)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .into(imageView);
        }

    }

    public void choosePhoto(View v){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


        Intent chooserIntent = Intent.createChooser(getIntent, "Seleziona l'immagine" );

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, RC_PHOTO);

        dismissBottomSheetDialogFragment();
   }

    public void takePhoto(View v){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.civic.gercs.civicsense.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, RC_PHOTO);
            }
        }
        dismissBottomSheetDialogFragment();
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void setPic() {


        Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
        Photo newPhoto = new Photo(uri);
        newPhoto.file = new File(mCurrentPhotoPath);
        photos.add(newPhoto);
        populateImages();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void showBottomSheetDialogFragment(View v) {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public void dismissBottomSheetDialogFragment(){
        bottomSheetFragment.dismiss();
    }

    private void openPhoto(Uri uri){
        hideKeyboard(this);
        for(Photo photo: photos){
            if(uri == photo.uri){
                photoSelected = photo;
                break;
            }
        }

        imageFullFragment = new ImageFull();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft
            .replace(R.id.container_image_full, imageFullFragment,uri.toString())
            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
            .addToBackStack(null)
            .commit();



    }

    public void closePhoto(View v){
        imageFullFragment.onDestroy();
    }

    public void prepareReportToSend(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);




        description = mEditTextDescription.getText().toString();
        if(photos.size() == 0){
            builder.setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_text_no_photo)
                    .setNeutralButton(R.string.text_button_neutral, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}})
                    .show();
            return;
        }

        if(description.isEmpty()){
            builder.setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_text_no_description)
                    .setNeutralButton(R.string.text_button_neutral, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}})
                    .show();
            return;
        }

        getAddress((float) report.getLocation().getLan(), (float) report.getLocation().getLng());


//        webView.loadUrl("about:blank");

        typeReport = 0;
        for (int i = 0; i < listTypeReport.size(); i++) {
            if(mSpinnerType.getSelectedItem() == listTypeReport.get(i).getName()){
                typeReport = listTypeReport.get(i).getId();
                break;
            }
        }

        parts = new ArrayList<>();
        for(int i=0; i < photos.size(); i++){
            MultipartBody.Part filePart = prepareFilePart("photos[]",photos.get(i));
            if(filePart != null) {
                parts.add(filePart);
            }
        }

        grade = "";

        switch (mSpinnerGrade.getSelectedItem().toString()){
            case "Basso": { grade = "LOW"; break;}
            case "Medio": { grade = "INTERMEDIATE"; break;}
            case "Alto": { grade = "HIGH"; break;}
        }

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams ls = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ls.setMargins(16,0,16,0);


        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Inserisci l'email");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setLayoutParams(ls);

        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_text_cdt_request)
                .setView(input)
                .setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No, grazie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        withEmail = false;
                        email = "";
                        sendRequest();
                    }
                })
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        withEmail = true;
                        email = input.getText().toString();
                        sendRequest();
                    }
                });

        final AlertDialog dialog  = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton((AlertDialog.BUTTON_POSITIVE)).setEnabled(false);
            }
        });
        dialog.show();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = input.getText().toString();
                if(isEmail(str)){
                    dialog.getButton((AlertDialog.BUTTON_POSITIVE)).setEnabled(true);
                }
                else{
                    dialog.getButton((AlertDialog.BUTTON_POSITIVE)).setEnabled(false);
                }

            }
        });

    }

    public void removePhoto(int index){

        final Photo photoToRemove = photos.get(index);



        photoToRemove.view.animate()
                .alphaBy(1f)
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        CardView cardView = (CardView) photoToRemove.view.getParent().getParent();
                        ViewGroup parentCardView = (ViewGroup) cardView.getParent();
                        parentCardView.removeAllViews();

                        photos.remove(photoToRemove);
                    }
                });

        closePhoto(null);
    }

    @NonNull
    private RequestBody createPartFromString(String tag) {
        return RequestBody.create(MediaType.parse("text/plain"), tag);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Photo photo) {
        try {
            File file;
            if(photo.file != null){
                file = photo.file;
            }
            else {
                file = new File(getRealPathFromURI(this, photo.uri));
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }catch(Exception e){
            Log.i(MainActivity.TAG,e.getMessage());
            return null;
        }

    }

    public class Photo{
        public Uri uri;
        public boolean isAlreadyShown = false;
        public File file = null;

        public void setView(ImageView view) {
            this.view = view;
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhoto(uri);
                }
            });
        }

        public ImageView view;

        Photo(Uri u){
            uri = u;
        }

        Photo(Uri u, ImageView v){
            uri = u;
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhoto(uri);
                }
            });
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri)  {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "getRealPathFromURI Exception : " + e.toString());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void getAddress(double lan, double lng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lan, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getThoroughfare()+", "+addresses.get(0).getFeatureName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnToMainActivity(){
        setResult(RESULT_OK);
        finish();
    }

    private void showCdt(final  Report.ResponseNewReport.Cdt cdt){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title)
                .setMessage("Ecco il codice di tracking: "+cdt.getCdt())
                .setNegativeButton(R.string.text_button_neutral, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) { dialog.dismiss();returnToMainActivity();}})
                .setNeutralButton(R.string.text_button_copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("cdt", cdt.getCdt());
                        try{
                            clipboard.setPrimaryClip(clip);
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Copiato", Toast.LENGTH_SHORT).show();
                        returnToMainActivity();
                    }
                })
                .show();
    }

    public static boolean isEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    private void sendRequest(){
        hideKeyboard(this);
        progressDialog.show();
        Service service = ServiceGenerator.createService();
        Call<Report.ResponseNewReport> call = service.newReport(
                city,
                description,
                address,
                report.getLocation().getLan(),
                report.getLocation().getLng(),
                typeReport,
                grade,
                email,
                parts);


        call.enqueue(new Callback<Report.ResponseNewReport>() {
            @Override
            public void onResponse(Call<Report.ResponseNewReport> call, Response<Report.ResponseNewReport> response) {
                if(response.isSuccessful()){
                    final Report.ResponseNewReport.Cdt cdt = response.body().getCdt();
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout,"Il report è stato inviato", Snackbar.LENGTH_SHORT).show();
                    showCdt(cdt);
                }
//                webView.loadData(response.body(), "text/html", null);
//                return;
            }

            @Override
            public void onFailure(Call<Report.ResponseNewReport> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.i(MainActivity.TAG,t.getLocalizedMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
}
