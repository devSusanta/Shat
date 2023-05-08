package com.susanta.chat;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> usermap = new HashMap<>();
	private String userdb = "";
	private double n = 0;
	
	private ArrayList<HashMap<String, Object>> userdata = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout linear10;
	private EditText noinputtxt;
	private EditText attherat;
	private TextView textview6;
	private LinearLayout linear12;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear11;
	private LinearLayout linear7;
	private EditText ename;
	private EditText eemail;
	private EditText epassword;
	private Button button1;
	private TextView fpassword;
	
	private Intent in = new Intent();
	private FirebaseAuth fauth;
	private OnCompleteListener<AuthResult> _fauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
	private OnCompleteListener<Void> _fauth_reset_password_listener;
	private OnCompleteListener<Void> fauth_updateEmailListener;
	private OnCompleteListener<Void> fauth_updatePasswordListener;
	private OnCompleteListener<Void> fauth_emailVerificationSentListener;
	private OnCompleteListener<Void> fauth_deleteUserListener;
	private OnCompleteListener<Void> fauth_updateProfileListener;
	private OnCompleteListener<AuthResult> fauth_phoneAuthListener;
	private OnCompleteListener<AuthResult> fauth_googleSignInListener;
	
	private DatabaseReference user = _firebase.getReference("user");
	private ChildEventListener _user_child_listener;
	private SharedPreferences userdblocal;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear3 = findViewById(R.id.linear3);
		linear10 = findViewById(R.id.linear10);
		noinputtxt = findViewById(R.id.noinputtxt);
		attherat = findViewById(R.id.attherat);
		textview6 = findViewById(R.id.textview6);
		linear12 = findViewById(R.id.linear12);
		linear4 = findViewById(R.id.linear4);
		linear5 = findViewById(R.id.linear5);
		linear11 = findViewById(R.id.linear11);
		linear7 = findViewById(R.id.linear7);
		ename = findViewById(R.id.ename);
		eemail = findViewById(R.id.eemail);
		epassword = findViewById(R.id.epassword);
		button1 = findViewById(R.id.button1);
		fpassword = findViewById(R.id.fpassword);
		fauth = FirebaseAuth.getInstance();
		userdblocal = getSharedPreferences("userdb", Activity.MODE_PRIVATE);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (eemail.getText().toString().equals("")) {
					((EditText)eemail).setError("Please Enter Email");
				}
				else {
					if (epassword.getText().toString().equals("")) {
						((EditText)epassword).setError("Please Enter Password");
					}
					else {
						fauth.signInWithEmailAndPassword(eemail.getText().toString(), epassword.getText().toString()).addOnCompleteListener(MainActivity.this, _fauth_sign_in_listener);
					}
				}
			}
		});
		
		fpassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (eemail.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please Enter Email");
					((EditText)eemail).setError("Enter Email l");
				}
				else {
					fauth.sendPasswordResetEmail(eemail.getText().toString()).addOnCompleteListener(_fauth_reset_password_listener);
				}
			}
		});
		
		_user_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		user.addChildEventListener(_user_child_listener);
		
		fauth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		fauth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_fauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					usermap = new HashMap<>();
					usermap.put("name", ename.getText().toString());
					usermap.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
					usermap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
					user.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "")).updateChildren(usermap);
					usermap.clear();
					_userdatalocal(ename.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());
					_gotohome();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
				}
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					user.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot _dataSnapshot) {
							userdata = new ArrayList<>();
							try {
								GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
								for (DataSnapshot _data : _dataSnapshot.getChildren()) {
									HashMap<String, Object> _map = _data.getValue(_ind);
									userdata.add(_map);
								}
							}
							catch (Exception _e) {
								_e.printStackTrace();
							}
							n = 0;
							for(int _repeat30 = 0; _repeat30 < (int)(userdata.size()); _repeat30++) {
								if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(userdata.get((int)n).get("email").toString())) {
									_userdatalocal(userdata.get((int)n).get("name").toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());
									_gotohome();
								}
								else {
									n++;
								}
							}
						}
						@Override
						public void onCancelled(DatabaseError _databaseError) {
						}
					});
				}
				else {
					if (_errorMessage.contains("no user")) {
						if (ename.getText().toString().equals("")) {
							((EditText)ename).setError("Please Enter Your Name.");
						}
						else {
							fauth.createUserWithEmailAndPassword(eemail.getText().toString(), epassword.getText().toString()).addOnCompleteListener(MainActivity.this, _fauth_create_user_listener);
						}
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
					}
				}
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				SketchwareUtil.showMessage(getApplicationContext(), "We have Sent a Password Reset E-Mail.\nPlease Check Your Email.");
			}
		};
	}
	
	private void initializeLogic() {
		linear1.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFF263238,0xFF000000}));
		_edittextstyle(ename);
		_edittextstyle(eemail);
		_edittextstyle(epassword);
		linear10.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)5, 0xFFE65100, 0xFF37474F));
		button1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)4, 0xFF795548, 0xFF2E7D32));
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			_gotohome();
		}
	}
	
	public void _gotohome() {
		in.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(in);
		finish();
	}
	
	
	public void _edittextstyle(final View _view) {
		_view.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)3, 0xFF212121, 0xFF212121));
	}
	
	
	public void _userdatalocal(final String _name, final String _email, final String _uid) {
		userdblocal.edit().putString("name", _name).commit();
		userdblocal.edit().putString("email", _email).commit();
		userdblocal.edit().putString("uid", _uid).commit();
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}