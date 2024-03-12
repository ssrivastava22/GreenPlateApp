package com.example.greenplate.viewmodels;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.greenplate.model.User;
import com.example.greenplate.model.UserSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PersonalInfoViewModel extends ViewModel {

    public User userInfo;
    //public UserSingleton user1;

    public PersonalInfoViewModel() {
        //userInfo = new User("0", "0", "");
        //user1 = UserSingleton.getInstance();
        userInfo = User.getInstance();
        userInfo.setHeight("0");
        userInfo.setWeight("0");
        userInfo.setGender("");
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

    public void setPersonalInfoModel(User newUserInfo) {
        if (this.isValidInput(newUserInfo)) {
            userInfo.setHeight(newUserInfo.getHeight());
            userInfo.setWeight(newUserInfo.getWeight());
            userInfo.setGender(newUserInfo.getGender());
        }
    }
    public boolean isValidInput(User newUserInfo) {
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
            HashMap<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put("Height", userInfo.getHeight());
            userInfoMap.put("Weight", userInfo.getWeight());
            userInfoMap.put("Gender", userInfo.getGender());
            String curr = userInfo.getUsername();

            //userRef.child(curr).setValue(userInfoMap);
            //String username = userInfo.getUsername();
            String username = curr.replaceAll("\\.", "");
            username = username.replaceAll("@", "");
            Log.d("Username: ", username);
            //userRef.child("Username").setValue(userInfo.getUsername());
            userRef.child(curr).setValue(userInfoMap);
            //userRef.child("Height").setValue(heightValue);
            //userRef.child("Weight").setValue(weightValue);
            //userRef.child("Gender").setValue(userInfo.getGender());
        }
    }

}
