package com.example.greenplate.viewmodels;
import android.text.TextUtils;
import androidx.lifecycle.ViewModel;
import com.example.greenplate.model.PersonalInfoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalInfoViewModel extends ViewModel {

    public PersonalInfoModel userInfo;

    public PersonalInfoViewModel() {
        userInfo = new PersonalInfoModel("0", "0", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userRef = database.getReference().child("Users").child(currentUser.getUid());
        }
    }

    public void setHeight(String height) {
        userInfo.setHeight(height);
    }

    public void setWeight(String weight) {
        userInfo.setWeight(weight);
    }

    public void setGender(String gender) {
        userInfo.setGender(gender);
    }

    public void setPersonalInfoModel(PersonalInfoModel newUserInfo) {
        if (this.isValidInput(newUserInfo)) {
            userInfo.setHeight(newUserInfo.getHeight());
            userInfo.setHeight(newUserInfo.getHeight());
            userInfo.setGender(newUserInfo.getGender());
        }
    }
    public boolean isValidInput(PersonalInfoModel newUserInfo) {
        return newUserInfo != null &&
                !TextUtils.isEmpty(newUserInfo.getGender()) &&
                !TextUtils.isEmpty(newUserInfo.getHeight()) &&
                !TextUtils.isEmpty(newUserInfo.getWeight());
    }

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = db.getReference().child("Users");
    public void saveUserInfo() {
        // Check if height, weight, and gender are not empty
        if (userInfo.getGender() != null && userInfo.getWeight() != null && userInfo.getHeight() != null) {

            // Convert height and weight to double
            double heightValue = Double.parseDouble(userInfo.getHeight());
            double weightValue = Double.parseDouble(userInfo.getWeight());

            // Save user information to Firebase database
            userRef.child("Height").setValue(heightValue);
            userRef.child("Weight").setValue(weightValue);
            userRef.child("Gender").setValue(userInfo.getGender());
        }
    }

}
