package com.example.realreal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.realreal.Plant;
import com.example.realreal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class AddActivity extends AppCompatActivity {

  private static final int CAMERA_CODE = 10;
  private static final int GALLERY_CODE = 100;

  private String mCurrentPhotoPath;
  private String flag;
  private ImageView imageView;
  private EditText mName;
  private EditText mKind;
  private EditText mIntro;

  private TextView mAdoptionDate;

  //    private Switch mAlarm;
  private TextView mAlarmDate;
  private Spinner mPeriodSpinner;
  private TextView mAlarmTime;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseDatabase database;
  private FirebaseStorage storage;
  private String firebaseImagePath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);
    init();
  }

  private void init() {
    mFirebaseAuth = FirebaseAuth.getInstance();
    database = FirebaseDatabase.getInstance();
    storage = FirebaseStorage.getInstance();
    requestPermission();

    mIntro = findViewById(R.id.intro);
    imageView = findViewById(R.id.imageView);
    mName = findViewById(R.id.name);
    mKind = findViewById(R.id.kind);

    TextView mToolbar_title = findViewById(R.id.toolbar_title);
    Intent intent = getIntent();
    flag = intent.getStringExtra("FLAG");

    if ("U".equals(flag)) {
      mToolbar_title.setText("식물수정");
      mName.setText(intent.getStringExtra("name"));
      mKind.setText(intent.getStringExtra("kind"));
      mIntro.setText(intent.getStringExtra("intro"));
      Glide.with(imageView).load(intent.getStringExtra("imageUrl")).into(imageView);
    }

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징 하기 위해 필요
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(true);      // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
      actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_arrow_left_black_24);
    }

    Button button = findViewById(R.id.imgAddButton);
    button.setOnClickListener(view -> {
      final Context context = view.getContext();
      final String[] items = {"카메라로 찍기", "앨범에서 가져오기", "취소"};
      AlertDialog.Builder ab = new AlertDialog.Builder(AddActivity.this);
      ab.setTitle("사진 선택");
      // 목록 클릭시 설정
      ab.setItems(items, (dialog, index) -> {
        if (index == 0)
          chkCameraPermission(context);
        else if (index == 1)
          pickUpPicture();

        dialog.dismiss();
      });

      ab.show();
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_add, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_insert: {
        if ("I".equals(flag))
          insert();
        else
          update();
        break;
      }
    }
    return super.onOptionsItemSelected(item);
  }

  private void pickUpPicture() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
    intent.setType("image/*");
    startActivityForResult(intent, GALLERY_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String path;
    if (resultCode == RESULT_OK) {
      if (requestCode == CAMERA_CODE) {
        path = mCurrentPhotoPath;
        uploadCameraImageByFirebase(path, requestCode);
      } else if (requestCode == GALLERY_CODE) {
        path = getPath(data.getData());
        uploadGalleryImageByFirebase(path, data.getData(), requestCode);
      }
    }
  }

  private void chkCameraPermission(Context context) {
    boolean camera = (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    boolean write = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

    if (camera && write)
      takePicture();  // 사진찍는 인텐트 코드 넣기
    else
      Toast.makeText(AddActivity.this, "카메라 권한 및 쓰기 권한이 없습니다.", Toast.LENGTH_SHORT).show();
  }


  private void requestPermission() {
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ArrayList<String> listPermissionNeeded = new ArrayList<>();
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
        listPermissionNeeded.add(permission);   // 권한이 허가가 안됐을 경우 요청할 권한을 찾는 부분
    }
    if (!listPermissionNeeded.isEmpty())
      ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[]{}), 1);  // 권한 요청 하는 부분
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

    if (!storageDir.exists()) {
      if (!storageDir.mkdirs())
        Toast.makeText(this, "이미지를 저장할 폴더를 생성하지 못했습니다.", Toast.LENGTH_SHORT);
    }

    File image = File.createTempFile(
      imageFileName,
      ".jpg",
      storageDir
    );

    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private Uri getImageUri(Context context, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }

  private Bitmap resizeImage(String imgPath) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 2;
    Bitmap orgImage = BitmapFactory.decodeFile(imgPath, options);
    int requestWidth = 1024;
    int requestHeight = 1024;
    return Bitmap.createScaledBitmap(orgImage, requestWidth, requestHeight, true);
  }

  private void uploadCameraImageByFirebase(String path, final int requestCode) {
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://realreal-26ef8.appspot.com");
    Bitmap resizeBitmap = resizeImage(path);
    ExifInterface exif;
    try {
      exif = new ExifInterface(path);
      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
      resizeBitmap = rotateBitmap(resizeBitmap, orientation);
    } catch (Exception e) {
      Log.d("TAG", e.getMessage());
    }

    final Uri file;
    if (resizeBitmap != null)
      file = getImageUri(this, resizeBitmap);
    else
      file = null;

    ReentrantLock criticObj = new ReentrantLock();
    criticObj.lock();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
    String imageFileName = "JPEG_" + timeStamp;
    criticObj.unlock();

    firebaseImagePath = "plants/" + imageFileName;
    StorageReference riversRef = storageRef.child(firebaseImagePath);

    if (file != null) {
      UploadTask uploadTask = riversRef.putFile(file);

      uploadTask.addOnFailureListener(exception -> Toast.makeText(AddActivity.this, "사진 등록이 실패했습니다.", Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> {
        cameraImageView(file, requestCode);
        Toast.makeText(AddActivity.this, "사진 등록이 성공했습니다.", Toast.LENGTH_SHORT).show();
      });
    }
  }

  private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
    Matrix matrix = new Matrix();
    switch (orientation) {
      case ExifInterface.ORIENTATION_NORMAL:
        return bitmap;
      case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
        matrix.setScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_ROTATE_180:
        matrix.setRotate(180);
        break;
      case ExifInterface.ORIENTATION_FLIP_VERTICAL:
        matrix.setRotate(180);
        matrix.postScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_TRANSPOSE:
        matrix.setRotate(90);
        matrix.postScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_ROTATE_90:
        matrix.setRotate(90);
        break;
      case ExifInterface.ORIENTATION_TRANSVERSE:
        matrix.setRotate(-90);
        matrix.postScale(-1, 1);
        break;
      case ExifInterface.ORIENTATION_ROTATE_270:
        matrix.setRotate(-90);
        break;
      default:
        return bitmap;
    }
    try {
      Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
      bitmap.recycle();
      return bmRotated;
    } catch (OutOfMemoryError e) {
      Log.d("TAG", e.getMessage());
      return null;
    }
  }

  private void uploadGalleryImageByFirebase(String path, final Uri uri, final int requestCode) {
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://realreal-26ef8.appspot.com");

    Bitmap resizeBitmap = resizeImage(path);
    Uri file = getImageUri(this, resizeBitmap);

    ReentrantLock criticObj = new ReentrantLock();
    criticObj.lock();
    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp;
    criticObj.unlock();

    firebaseImagePath = "plants/" + imageFileName;
    StorageReference riversRef = storageRef.child(firebaseImagePath);
    UploadTask uploadTask = riversRef.putFile(file);

    uploadTask.addOnFailureListener(exception -> Toast.makeText(AddActivity.this, "사진 등록이 실패했습니다.", Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> {
      galleryImageView(uri, requestCode);
      Toast.makeText(AddActivity.this, "사진 등록이 성공했습니다.", Toast.LENGTH_SHORT).show();
    });
  }

  private String getPath(Uri uri) {
    String[] proj = {MediaStore.Images.Media.DATA};
    CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
    Cursor cursor = cursorLoader.loadInBackground();
    String path = "";
    if (cursor != null) {
      int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      path = cursor.getString(index);
    }
    return path;
  }

  private void cameraImageView(Uri uri, int requestCode) {
    ImageView imageview = findViewById(R.id.imageView);
    if (requestCode == CAMERA_CODE)
      Glide.with(imageView.getContext()).load(uri).into(imageview);
  }

  private void galleryImageView(Uri uri, int requestCode) {
    ImageView imageview = findViewById(R.id.imageView);
    if (requestCode == GALLERY_CODE)
      Glide.with(imageView.getContext()).load(uri).into(imageview);
  }

  private void takePicture() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    try {
      File photoFile = createImageFile();
      Uri photoUri = FileProvider.getUriForFile(this, "com.example.realreal.fileprovider", photoFile);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
      startActivityForResult(intent, CAMERA_CODE);
    } catch (IOException e) {
      e.getMessage();
    }
  }

  private void insert() {
    final String name = mName.getText().toString();
    final String kind = mKind.getText().toString();
    final String intro = mIntro.getText().toString();

    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
    final String uid;
    final String userId;
    if (firebaseUser != null) {
      uid = firebaseUser.getUid();
      userId = firebaseUser.getEmail();
    } else {
      uid = "";
      userId = "";
    }

    AlertDialog.Builder ab = new AlertDialog.Builder(AddActivity.this);
    ab.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());

    if (imageView.getDrawable() == null) {
      ab.setMessage("사진을 등록해 주세요.");
      ab.show();
      return;
    }

    if ("".equals(name)) {
      ab.setMessage("이름을 입력해주세요.");
      ab.show();
      return;
    }

    if ("".equals(kind)) {
      ab.setMessage("종류를 입력해주세요.");
      ab.show();
      return;
    }

    if ("".equals(intro)) {
      ab.setMessage("소개를 입력해주세요.");
      ab.show();
      return;
    }
    StorageReference storageRef = storage.getReferenceFromUrl("gs://realreal-26ef8.appspot.com");
    storageRef.child(firebaseImagePath).getDownloadUrl().addOnSuccessListener(uri -> {

      String imageUrl = uri.toString();
      String path = uri.getPath();
      String imageName = "";
      if (path != null) {
        String[] pathStrArr = uri.getPath().split("/");
        imageName = pathStrArr[pathStrArr.length - 1];
      }

      Plant plant = new Plant(name, kind, imageName, imageUrl, intro, uid, userId, "");
      database.getReference().child("plant").push().setValue(plant).addOnSuccessListener(aVoid -> {
        String msg = "등록이 완료됐습니다.";
        showDialogAfterWork(msg);
      }).addOnFailureListener(e -> Toast.makeText(AddActivity.this, "식물 등록이 실패했습니다.", Toast.LENGTH_SHORT).show());
    }).addOnFailureListener(exception -> Toast.makeText(AddActivity.this, "식물 등록이 실패했습니다.", Toast.LENGTH_SHORT).show());
  }

  private void showDialogAfterWork(String msg) {
    AlertDialog.Builder ab = new AlertDialog.Builder(AddActivity.this);
    ab.setPositiveButton("확인", (dialog, which) -> {
      dialog.dismiss();
      Intent intent = new Intent(AddActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    });
    ab.setMessage(msg);
    ab.show();
  }

  private void update() {
    final String name = mName.getText().toString();
    final String kind = mKind.getText().toString();
    final String intro = mIntro.getText().toString();

    AlertDialog.Builder ab = new AlertDialog.Builder(AddActivity.this);
    ab.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());

    if (imageView.getDrawable() == null) {
      ab.setMessage("사진을 등록해 주세요.");
      ab.show();
      return;
    }

    if ("".equals(name)) {
      ab.setMessage("이름을 입력해주세요.");
      ab.show();
      return;
    }

    if ("".equals(kind)) {
      ab.setMessage("종류를 입력해주세요.");
      ab.show();
      return;
    }

    if ("".equals(intro)) {
      ab.setMessage("소개를 입력해주세요.");
      ab.show();
      return;
    }
  }
}
