package www.tsdevcut.co.za.luladrivedemo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.Arrays;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;

public class Login extends AppCompatActivity {

    SharedPreferences sharedPre;
    EditText edtUserName;
    EditText edtPassword;

    String [] namingList ={"Themba Mbevu", "Clive Brikston","Guisara Masutha","Mbongeni Ngwevu"};
    String [] userList ={"thems", "clivovo","gees","mbos"};
    String [] idList ={"1001", "1002","1003","1004"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////////////////////////////////////////////////////////////////////////////////////
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
        }
        setContentView(R.layout.login);

        edtUserName         = (EditText) findViewById(R.id.editTextLoginUsername);
        edtPassword         = (EditText) findViewById(R.id.editTextLoginPassword);

    }

    public void btnDriverLoginAction(View view) {
        String uName = edtUserName.getText().toString().trim();
        String uPass = edtPassword.getText().toString().trim();
        if(uName.length() > 2 && uPass.length() > 2) {


            boolean userExits = false;
            int count = -1;
            switch(uName){
                case "thems":
                    userExits = true;
                    count = 0;
                    break;
                case "clivovo":
                    userExits = true;
                    count = 1;
                    break;
                case "gees":
                    userExits = true;
                    count = 2;
                    break;
                case "mbos":
                    userExits = true;
                    count = 3;
                    break;
                default: Toast.makeText(Login.this, "Eish - " + uName,Toast.LENGTH_LONG).show();;
                    break;
            }
            if(userExits){

                boolean sInPaas = Arrays.asList("1234").contains(uPass);
                if(sInPaas == true) {
                    InitializedbBase(count);
                    startActivity(new Intent(Login.this, MainScreen.class));
                }
            }
            else{
                Toast.makeText(Login.this, "Please Enter correct details",Toast.LENGTH_LONG).show();
            }
        }
        else {

            Toast.makeText(Login.this, "Please Enter User Details",Toast.LENGTH_LONG).show();
        }
    }
    private void InitializedbBase(int element) {
        String myName = namingList[element];
        String dId = idList[element] ;
        String userName = userList[element];


        sharedPre               = getSharedPreferences("LulaDriverApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = sharedPre.edit();
        ed.putString("DriverName",myName);
        ed.putString("DriverUserName",userName);
        ed.putString("DriverID",dId);
        ed.putString("DriverLatit", "-20.00");
        ed.putString("DriverLongit", "20.00");
        ed.putBoolean("DriverAwaitRequest", false);
        //Passenger
        ed.putString("PassUserID","1234");
        ed.putString("PLatit","0.00");
        ed.putString("PLongit", "0.7");
        //Other
        ed.putBoolean("OnAcceptButton", false);
        ed.putBoolean("BeginTrip", false);
        ed.commit();
    }

}
