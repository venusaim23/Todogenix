//Creating a context

import React, { createContext, useState, useEffect } from "react";
import { auth } from "../firebase";
import firebase from "../firebase";

const PracticeContext = createContext();

const currentUserId = localStorage.getItem('userUid'); 

const ContextPractice = (props) => {
  // handle login submit
  const handleLoginSubmit = (eve) => {
    eve.preventDefault();
    const email = eve.target[0].value;
    const password = eve.target[1].value;

    auth
      .signInWithEmailAndPassword(email, password)
      .then((userCredential) => {
        const user = userCredential.user;
        localStorage.setItem("userUid", user.uid);
        window.location = `${window.location.origin}/home`;
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorCode, errorMessage);
      });
  };

  //   handle sign up submit
  const handleSignUpSubmit = (eve) => {
    eve.preventDefault();

    const name = eve.target[0].value;
    const email = eve.target[1].value;
    const password = eve.target[2].value;
    const location = eve.target[3].value;

    localStorage.setItem("name", name);
    localStorage.setItem("location", location);

    auth
      .createUserWithEmailAndPassword(email, password)
      .then((userCredential) => {
        const user = userCredential.user;
        localStorage.setItem("userUid", user.uid);
        window.location = `${window.location.origin}/home`;
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorCode, errorMessage);
      });
  };

  //   handle task upadte
  const handleUpdate = (currentUserId, taskId, completed) => {
    const todoRef = firebase
      .database()
      .ref(currentUserId)
      .child("Task")
      .child(taskId);
    todoRef.update({
      completed: !completed,
    });
  };

  //   handle task delete
  const handleDelete = (currentUserId, taskId) => {
      if(taskId){
        const todoRef = firebase
        .database()
        .ref(currentUserId)
        .child("Task")
        .child(taskId);
      todoRef.remove();
      }
    
  };

  //add a task to the database
  const addOrEdit = (title,type, date, time, duration) => {
      console.log("hilo",title,type,date,time,duration);
    const todoRef = firebase.database().ref(currentUserId).child("Task");
    let todo1 = {};

    if (date && time) {
      const todo = {
        title,
        completed: false,
        type: type,
        date,
        time,
        duration,
        timestamp: Date.now(),
      };
      todo1 = todo;
    } else {
      const todo = {
        title,
        completed: false,
        type: type,
      };
      todo1 = todo;
    }

    todoRef.push(todo1);
  };

  return (
    <div>
      {/* To pass some data you need a provider */}
      <PracticeContext.Provider
        value={{
          handleLoginSubmit,
          handleSignUpSubmit,
          handleUpdate,
          handleDelete,
          addOrEdit,
        }}
      >
        {props.children}
      </PracticeContext.Provider>
      {/* app jaha data chahiye waha jake import karo context ko */}
    </div>
  );
};

export default ContextPractice;
export { PracticeContext };
