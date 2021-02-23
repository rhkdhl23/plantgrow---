package com.example.realreal;

import java.util.List;

public interface IFirebaseLoadDone {
  void onFirebaseLoadSuccess(List<Users> usersList);

  void onFirebaseLoadFailed(String message);
}
