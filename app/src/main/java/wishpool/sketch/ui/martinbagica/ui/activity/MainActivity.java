package wishpool.sketch.ui.martinbagica.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import wishpool.sketch.R;
import wishpool.sketch.ui.martinbagica.domain.manager.FileManager;
import wishpool.sketch.ui.martinbagica.domain.manager.PermissionManager;
import wishpool.sketch.ui.martinbagica.ui.component.DrawingView;
import wishpool.sketch.ui.martinbagica.ui.dialog.StrokeSelectorDialog;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_STROKE_WIDTH = 50;
    private int mCurrentBackgroundColor;
    private int mCurrentColor;
    private int mCurrentStroke;
    private DrawingView mDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        initDrawingView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                requestPermissionsAndSaveBitmap();
                break;
            case R.id.action_clear:
                new AlertDialog.Builder(this)
                        .setTitle("Clear canvas")
                        .setMessage("Are you sure you want to clear the canvas?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDrawingView.clearCanvas();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDrawingView() {
        mDrawingView = findViewById(R.id.main_drawing_view);
        mCurrentBackgroundColor = ContextCompat.getColor(this, android.R.color.white);
        mCurrentColor = ContextCompat.getColor(this, android.R.color.black);
        mCurrentStroke = 10;

        mDrawingView.setBackgroundColor(mCurrentBackgroundColor);
        mDrawingView.setPaintColor(mCurrentColor);
        mDrawingView.setPaintStrokeWidth(mCurrentStroke);


        setClicks();
    }

    private void setClicks() {


        findViewById(R.id.main_fill_iv).setOnClickListener(view -> {
            onBackgroundFillOptionClick();
        });

        findViewById(R.id.main_color_iv).setOnClickListener(view -> {
            onColorOptionClick();
        });

        findViewById(R.id.main_stroke_iv).setOnClickListener(view -> {
            onStrokeOptionClick();
        });

        findViewById(R.id.main_undo_iv).setOnClickListener(view -> {
            onUndoOptionClick();
        });

        findViewById(R.id.main_redo_iv).setOnClickListener(view -> {
            onRedoOptionClick();
        });


    }

    private void startFillBackgroundDialog() {
        int[] colors = getResources().getIntArray(R.array.palette);

		/*ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
				colors,
				mCurrentBackgroundColor,
				5,
				ColorPickerDialog.SIZE_SMALL);

		dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener()
		{

			@Override
			public void onColorSelected(int color)
			{
				mCurrentBackgroundColor = color;
				mDrawingView.setBackgroundColor(mCurrentBackgroundColor);
			}

		});

		dialog.show(getFragmentManager(), "ColorPickerDialog");*/
    }

    private void startColorPickerDialog() {
        int[] colors = getResources().getIntArray(R.array.palette);

		/*ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
				colors,
				mCurrentColor,
				5,
				ColorPickerDialog.SIZE_SMALL);

		dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener()
		{

			@Override
			public void onColorSelected(int color)
			{
				mCurrentColor = color;
				mDrawingView.setPaintColor(mCurrentColor);
			}

		});

		dialog.show(getFragmentManager(), "ColorPickerDialog");*/
    }

    private void startStrokeSelectorDialog() {
        StrokeSelectorDialog dialog = StrokeSelectorDialog.newInstance(mCurrentStroke, MAX_STROKE_WIDTH);

        dialog.setOnStrokeSelectedListener(stroke -> {
			mCurrentStroke = stroke;
			mDrawingView.setPaintStrokeWidth(mCurrentStroke);
		});

		dialog.show(getSupportFragmentManager(), "StrokeSelectorDialog");
    }

    private void startShareDialog(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Image"));
    }

    private void requestPermissionsAndSaveBitmap() {
        if (PermissionManager.checkWriteStoragePermissions(this)) {
            Uri uri = FileManager.saveBitmap(this, mDrawingView.getBitmap());
            startShareDialog(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Uri uri = FileManager.saveBitmap(this, mDrawingView.getBitmap());
                    startShareDialog(uri);
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void onBackgroundFillOptionClick() {
        startFillBackgroundDialog();
    }

    public void onColorOptionClick() {
        startColorPickerDialog();
    }

    public void onStrokeOptionClick() {
        startStrokeSelectorDialog();
    }

    public void onUndoOptionClick() {
        mDrawingView.undo();
    }

    public void onRedoOptionClick() {
        mDrawingView.redo();
    }
}
