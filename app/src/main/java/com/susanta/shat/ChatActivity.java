package com.susanta.chat;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.*;
import java.io.*;
import java.io.File;
import java.io.InputStream;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class ChatActivity extends AppCompatActivity {
	
	public final int REQ_CD_FP = 101;
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String group_key = "";
	private String chat_key = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String push_key = "";
	private String fpth = "";
	private String docc = "";
	private String id = "";
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	private ArrayList<String> fpath = new ArrayList<>();
	private ArrayList<String> keylist = new ArrayList<>();
	
	private LinearLayout linear2;
	private LinearLayout linear7;
	private LinearLayout linear3;
	private CircleImageView roomImage;
	private ListView clist;
	private LinearLayout option;
	private LinearLayout linear4;
	private ImageView imageview4;
	private LinearLayout menucon;
	private LinearLayout linear56;
	private LinearLayout sendcon;
	private ImageView imageview3;
	private EditText message;
	private ImageView btnsend;
	
	private DatabaseReference group_chat = _firebase.getReference("group_chat");
	private ChildEventListener _group_chat_child_listener;
	private Calendar clndr = Calendar.getInstance();
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
	
	private StorageReference fst = _firebase_storage.getReference("users");
	private OnCompleteListener<Uri> _fst_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _fst_download_success_listener;
	private OnSuccessListener _fst_delete_success_listener;
	private OnProgressListener _fst_upload_progress_listener;
	private OnProgressListener _fst_download_progress_listener;
	private OnFailureListener _fst_failure_listener;
	
	private Intent inte = new Intent();
	private SharedPreferences image;
	private SharedPreferences mess;
	private AlertDialog cdial;
	private AlertDialog.Builder dlog01;
	private SharedPreferences userdblocal;
	private Intent fp = new Intent(Intent.ACTION_GET_CONTENT);
	private SharedPreferences bool;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear2 = findViewById(R.id.linear2);
		linear7 = findViewById(R.id.linear7);
		linear3 = findViewById(R.id.linear3);
		roomImage = findViewById(R.id.roomImage);
		clist = findViewById(R.id.clist);
		option = findViewById(R.id.option);
		linear4 = findViewById(R.id.linear4);
		imageview4 = findViewById(R.id.imageview4);
		menucon = findViewById(R.id.menucon);
		linear56 = findViewById(R.id.linear56);
		sendcon = findViewById(R.id.sendcon);
		imageview3 = findViewById(R.id.imageview3);
		message = findViewById(R.id.message);
		btnsend = findViewById(R.id.btnsend);
		fauth = FirebaseAuth.getInstance();
		image = getSharedPreferences("image", Activity.MODE_PRIVATE);
		mess = getSharedPreferences("mess", Activity.MODE_PRIVATE);
		dlog01 = new AlertDialog.Builder(this);
		userdblocal = getSharedPreferences("userdb", Activity.MODE_PRIVATE);
		fp.setType("image/*");
		fp.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		bool = getSharedPreferences("bool", Activity.MODE_PRIVATE);
		
		clist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				
			}
		});
		
		clist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (listmap.get((int)_position).get("email").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
					_delete_message(_position);
				}
				else {
					if (listmap.get((int)_position).get("rcreator").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
						_delete_message(_position);
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Operation not Allowed");
					}
				}
				return true;
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_previewimage(docc);
			}
		});
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(fp, REQ_CD_FP);
			}
		});
		
		btnsend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!message.getText().toString().equals("")) {
					clndr = Calendar.getInstance();
					push_key = group_chat.push().getKey();
					map = new HashMap<>();
					if (!docc.equals("")) {
						map.put("docc", docc);
						option.setVisibility(View.GONE);
					}
					map.put("name", userdblocal.getString("name", ""));
					map.put("message", message.getText().toString());
					map.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
					map.put("rcreator", getIntent().getStringExtra("rcreator"));
					map.put("ckey", push_key);
					map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
					map.put("time", new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss aa").format(clndr.getTime()));
					group_chat.child(push_key).updateChildren(map);
					map.clear();
					message.setText("");
					docc = "";
				}
				else {
					((EditText)message).setError("input cannot be empty");
				}
			}
		});
		
		_group_chat_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				group_chat.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						listmap = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								listmap.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						keylist.add(_childKey);
						clist.setAdapter(new ClistAdapter(listmap));
						((BaseAdapter)clist.getAdapter()).notifyDataSetChanged();
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
				group_chat.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						listmap = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								listmap.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						keylist.add(_childKey);
						clist.setAdapter(new ClistAdapter(listmap));
						((BaseAdapter)clist.getAdapter()).notifyDataSetChanged();
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
				group_chat.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						listmap = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								listmap.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						keylist.add(_childKey);
						clist.setAdapter(new ClistAdapter(listmap));
						((BaseAdapter)clist.getAdapter()).notifyDataSetChanged();
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
				SketchwareUtil.showMessage(getApplicationContext(), String.valueOf((long)(_errorCode)).concat(_errorMessage));
			}
		};
		group_chat.addChildEventListener(_group_chat_child_listener);
		
		_fst_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				SketchwareUtil.showMessage(getApplicationContext(), "uploading...");
			}
		};
		
		_fst_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_fst_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				docc = _downloadUrl;
				Glide.with(getApplicationContext()).load(Uri.parse(_downloadUrl)).into(imageview4);
				option.setVisibility(View.VISIBLE);
			}
		};
		
		_fst_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_fst_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				SketchwareUtil.showMessage(getApplicationContext(), "Deleted");
			}
		};
		
		_fst_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), _message);
			}
		};
		
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
		if (!(FirebaseAuth.getInstance().getCurrentUser() != null)) {
			finish();
		}
		clist.setAdapter(new ClistAdapter(listmap));
		clist.setStackFromBottom(true);
		option.setVisibility(View.GONE);
		message.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)2, 0xFF795548, 0xFF0D47A1));
		sendcon.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF795548, 0xFF0D47A1));
		menucon.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)2, 0xFF795548, 0xFF0D47A1));
		group_key = getIntent().getStringExtra("key");
		group_chat.removeEventListener(_group_chat_child_listener);
		chat_key = "Room-".concat(group_key);
		group_chat = _firebase.getReference(chat_key);
		group_chat.addChildEventListener(_group_chat_child_listener);
		linear2.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFF000050,0xFF000000}));
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FP:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				fpth = _filePath.get((int)(0));
				fst.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().concat(Uri.parse(fpth).getLastPathSegment())).putFile(Uri.fromFile(new File(fpth))).addOnFailureListener(_fst_failure_listener).addOnProgressListener(_fst_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return fst.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().concat(Uri.parse(fpth).getLastPathSegment())).getDownloadUrl();
					}}).addOnCompleteListener(_fst_upload_success_listener);
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	public void _previewimage(final String _url) {
		image.edit().putString("image01", _url).commit();
		inte.setClass(getApplicationContext(), PreviewActivity.class);
		startActivity(inte);
	}
	
	
	public void _delete_message(final double _position) {
		dlog01.setTitle("Delete Message");
		dlog01.setMessage(listmap.get((int)_position).get("email").toString().concat("\n".concat(listmap.get((int)_position).get("message").toString().concat("\n".concat(listmap.get((int)_position).get("time").toString())))));
		dlog01.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				if (listmap.get((int)_position).containsKey("docc")) {
					_firebase_storage.getReferenceFromUrl(listmap.get((int)_position).get("docc").toString()).delete().addOnSuccessListener(_fst_delete_success_listener).addOnFailureListener(_fst_failure_listener);
				}
				clist.setAdapter(new ClistAdapter(listmap));
				((BaseAdapter)clist.getAdapter()).notifyDataSetChanged();
				group_chat.child(listmap.get((int)_position).get("ckey").toString()).removeValue();
				clist.smoothScrollToPosition((int)(_position));
			}
		});
		dlog01.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		dlog01.create().show();
	}
	
	public class ClistAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public ClistAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_view = _inflater.inflate(R.layout.cchat, null);
			}
			
			final LinearLayout main1 = _view.findViewById(R.id.main1);
			final LinearLayout mc1 = _view.findViewById(R.id.mc1);
			final LinearLayout wdoc1 = _view.findViewById(R.id.wdoc1);
			final LinearLayout docc = _view.findViewById(R.id.docc);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final TextView umessage = _view.findViewById(R.id.umessage);
			final TextView utime = _view.findViewById(R.id.utime);
			final de.hdodenhof.circleimageview.CircleImageView uimage = _view.findViewById(R.id.uimage);
			final TextView uemail = _view.findViewById(R.id.uemail);
			final ImageView image = _view.findViewById(R.id.image);
			
			if (_data.get((int)_position).containsKey("docc")) {
				docc.setVisibility(View.VISIBLE);
				if (bool.getString("autodown", "").equals("on")) {
					Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("docc").toString())).into(image);
				}
			}
			else {
				docc.setVisibility(View.GONE);
			}
			uemail.setText(_data.get((int)_position).get("name").toString());
			umessage.setText(_data.get((int)_position).get("message").toString());
			utime.setText(_data.get((int)_position).get("time").toString());
			main1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)5, (int)3, 0xFFFF5722, Color.TRANSPARENT));
			docc.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)5, (int)3, 0xFFFF5722, 0xFF263238));
			if (_data.get((int)_position).get("email").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
				main1.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				wdoc1.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				linear3.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				umessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				utime.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				uemail.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				wdoc1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)3, 0xFFECEFF1, 0xFF3F51B5));
				docc.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			}
			else {
				docc.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				main1.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				wdoc1.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				linear3.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				umessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				utime.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				uemail.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				wdoc1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)2, 0xFFECEFF1, 0xFF004D40));
			}
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_previewimage(_data.get((int)_position).get("docc").toString());
				}
			});
			
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