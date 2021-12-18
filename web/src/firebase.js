import firebase from "firebase";
import 'firebase/auth';
// import { getAuth, createUserWithEmailAndPassword } from "firebase/auth";




const firebaseConfig = {
  apiKey: "AIzaSyAy6wKL-4yr6ojPUldcJjZxb8FBFIpDDAQ",
  authDomain: "smart-todo-49872.firebaseapp.com",
  databaseURL: "https://smart-todo-49872-default-rtdb.firebaseio.com",
  projectId: "smart-todo-49872",
  storageBucket: "smart-todo-49872.appspot.com",
  messagingSenderId: "883466975764",
  appId: "1:883466975764:web:c9f73e4c40719fa2108501",
};

const app = firebase.initializeApp(firebaseConfig);
export const auth = app.auth();

export default firebase;
