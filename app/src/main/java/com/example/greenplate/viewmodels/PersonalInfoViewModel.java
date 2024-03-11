package com.example.greenplate.viewmodels;
import android.text.TextUtils;
import androidx.lifecycle.ViewModel;
import com.example.greenplate.model.PersonalInfoModel;

public class PersonalInfoViewModel extends ViewModel {

    private PersonalInfoModel userInfo;

    public PersonalInfoViewModel() {
        userInfo = new PersonalInfoModel(0.0, 0.0, "");
    }

    public void setHeight(double height) {
        userInfo.setHeight(height);
    }

    public void setWeight(double weight) {
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
        if (newUserInfo == null || newUserInfo.getMealName() == null || newUserInfo.getHeight().isEmpty()) {
            return false;
        }
        return true;
    }

}
