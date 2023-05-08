package com.susanta.chat;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import de.hdodenhof.circleimageview.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class HomeActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private HashMap<String, Object> map = new HashMap<>();
	private String group_key = "";
	private String rkay = "";
	private HashMap<String, Object> nmp = new HashMap<>();
	private String rkaku = "";
	private String ckeyd = "";
	private double n = 0;
	private double exist = 0;
	private String creator = "";
	private HashMap<String, Object> own = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> lmp = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> mapgc = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> ownlmp = new ArrayList<>();
	private ArrayList<String> lstr = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear6;
	private LinearLayout linear12;
	private CircleImageView circleimageview1;
	private LinearLayout linear8;
	private ImageView imageview1;
	private Button logout;
	private ListView rlist;
	private LinearLayout linear7;
	private TextView textview3;
	private EditText rkey;
	private Button rbtn;
	
	private Intent in = new Intent();
	private DatabaseReference room = _firebase.getReference("room");
	private ChildEventListener _room_child_listener;
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
	
	private DatabaseReference group_chat = _firebase.getReference("group_chat");
	private ChildEventListener _group_chat_child_listener;
	private DatabaseReference roomown = _firebase.getReference("rown");
	private ChildEventListener _roomown_child_listener;
	private AlertDialog.Builder Alert;
	private SharedPreferences login;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear6 = findViewById(R.id.linear6);
		linear12 = findViewById(R.id.linear12);
		circleimageview1 = findViewById(R.id.circleimageview1);
		linear8 = findViewById(R.id.linear8);
		imageview1 = findViewById(R.id.imageview1);
		logout = findViewById(R.id.logout);
		rlist = findViewById(R.id.rlist);
		linear7 = findViewById(R.id.linear7);
		textview3 = findViewById(R.id.textview3);
		rkey = findViewById(R.id.rkey);
		rbtn = findViewById(R.id.rbtn);
		fauth = FirebaseAuth.getInstance();
		Alert = new AlertDialog.Builder(this);
		login = getSharedPreferences("login", Activity.MODE_PRIVATE);
		
		circleimageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				in.setClass(getApplicationContext(), ProfileActivity.class);
				startActivity(in);
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Alert.setTitle("Check Update");
				Alert.setMessage("check for update in our website. ");
				Alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						in.setAction(Intent.ACTION_VIEW);
						in.setData(Uri.parse("http://apk.imsusanta.ml/shat"));
						startActivity(in);
					}
				});
				Alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				Alert.create().show();
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				FirebaseAuth.getInstance().signOut();
				login.edit().remove("email").commit();
				login.edit().remove("password").commit();
				in.setClass(getApplicationContext(), MainActivity.class);
				startActivity(in);
				finish();
			}
		});
		
		rlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				in.putExtra("key", lmp.get((int)_position).get("rkey").toString());
				in.putExtra("rcreator", lmp.get((int)_position).get("rcreator").toString());
				in.setClass(getApplicationContext(), ChatActivity.class);
				startActivity(in);
			}
		});
		
		rlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (lmp.get((int)_position).get("rcreator").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
					Alert.setTitle("Delete Room");
					Alert.setMessage("worning alert for you after your click on deleted button. all messages inside this Room will be deleted. or hide this from list.");
					Alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							group_key = lmp.get((int)_position).get("rkey").toString();
							group_chat.removeEventListener(_group_chat_child_listener);
							ckeyd = "Room-".concat(group_key);
							group_chat = _firebase.getReference(ckeyd);
							group_chat.addChildEventListener(_group_chat_child_listener);
							group_chat.removeValue();
							room.child(lmp.get((int)_position).get("rkey").toString()).removeValue();
							roomown.child(lstr.get((int)(lstr.indexOf(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").concat(lmp.get((int)_position).get("rkey").toString()))))).removeValue();
							SketchwareUtil.showMessage(getApplicationContext(), "deleted all messages in this Room.");
						}
					});
					Alert.setNegativeButton("Do Nothing", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					Alert.setNeutralButton("Hide in List", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							room.child(lmp.get((int)_position).get("rkey").toString()).removeValue();
							SketchwareUtil.showMessage(getApplicationContext(), "enter key again to join again.");
						}
					});
					Alert.create().show();
				}
				else {
					room.child(lmp.get((int)_position).get("rkey").toString()).removeValue();
					SketchwareUtil.showMessage(getApplicationContext(), "you are not the creator of these Room.");
					SketchwareUtil.showMessage(getApplicationContext(), "the room is hide in you.\nmessages are still save on these room.");
					SketchwareUtil.showMessage(getApplicationContext(), "enter key again to join again.");
				}
				return true;
			}
		});
		
		rbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				rkay = rkey.getText().toString();
				exist = 0;
				n = 0;
				if (!rkey.getText().toString().equals("")) {
					for(int _repeat168 = 0; _repeat168 < (int)(ownlmp.size()); _repeat168++) {
						if (ownlmp.get((int)n).get("rkey").toString().equals(rkay)) {
							nmp = new HashMap<>();
							nmp.put("rkey", rkay);
							nmp.put("rcreator", ownlmp.get((int)n).get("rown").toString());
							room.child(rkay).updateChildren(nmp);
							nmp.clear();
							exist = 1;
						}
						n++;
					}
					if (exist == 0) {
						nmp = new HashMap<>();
						nmp.put("rkey", rkay);
						nmp.put("rcreator", FirebaseAuth.getInstance().getCurrentUser().getEmail());
						room.child(rkay).updateChildren(nmp);
						nmp.clear();
						own = new HashMap<>();
						own.put("rown", FirebaseAuth.getInstance().getCurrentUser().getEmail());
						own.put("rkey", rkay);
						roomown.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").concat(rkay)).updateChildren(own);
						own.clear();
					}
					rkey.setText("");
				}
				else {
					((EditText)rkey).setError("Please Enter Room Key");
				}
			}
		});
		
		_room_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				room.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						lmp = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								lmp.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						rlist.setAdapter(new RlistAdapter(lmp));
						((BaseAdapter)rlist.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				room.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						lmp = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								lmp.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						rlist.setAdapter(new RlistAdapter(lmp));
						((BaseAdapter)rlist.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				room.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						lmp = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								lmp.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						rlist.setAdapter(new RlistAdapter(lmp));
						((BaseAdapter)rlist.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), String.valueOf((long)(_errorCode)).concat(" \n ".concat(_errorMessage)));
			}
		};
		room.addChildEventListener(_room_child_listener);
		
		_group_chat_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				group_chat.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						mapgc = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								mapgc.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						creator = _childKey;
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
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
		group_chat.addChildEventListener(_group_chat_child_listener);
		
		_roomown_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				roomown.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						ownlmp = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								ownlmp.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						lstr.add(_childKey);
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
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
		roomown.addChildEventListener(_roomown_child_listener);
		
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
				
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
		room.removeEventListener(_room_child_listener);
		rkaku = "Room-".concat(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));
		room = _firebase.getReference(rkaku);
		room.addChildEventListener(_room_child_listener);
		rlist.setStackFromBottom(true);
		linear1.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFF212121,0xFF000000}));
		rbtn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)4, 0xFF795548, 0xFF004D40));
		rkey.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)3, 0xFF795548, 0xFF212121));
		logout.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)4, 0xFF795548, 0xFFB71C1C));
		imageview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)3, 0xFF795548, 0xFF1B5E20));
	}
	
	public class RlistAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public RlistAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.room, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView rkey = _view.findViewById(R.id.rkey);
			
			rkey.setText(_data.get((int)_position).get("rkey").toString());
			
			return _view;
		}
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