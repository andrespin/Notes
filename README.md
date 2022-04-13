# Notes
Notes

EditText mEdit = (EditText) findViewById(R.id.yourid);
mEdit.setEnabled(false);
This is also a viable alternative :

EditText mEdit = (EditText) findViewById(R.id.yourid);
mEdit.setKeyListener(null);

android:inputType="none"



