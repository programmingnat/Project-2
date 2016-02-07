package lab.imaginenat.com.project2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AddNewBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_business);



        Button addButton = (Button)findViewById(R.id.addToDB_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Business b = new Business("Crestwood Pizza","21 Columbus Ave","NY","10707","Italian");
                BusinessManager manager=BusinessManager.getInstance(AddNewBusinessActivity.this);
                manager.addBusiness(b);
                finish();
            }

        });

        Button cancelButton = (Button)findViewById(R.id.cancel_Button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
