import "./App.css";
import { useState, useEffect, useContext } from "react";
import { Route, Routes } from "react-router-dom";
import Home from "./Pages/Home/Home";
import ToDo from "./Pages/ToDo/ToDo";
import alanBtn from "@alan-ai/alan-sdk-web";
import firebase from "./firebase";
import wordsToNumbers from "words-to-numbers";
import Timer from "./Pages/Timer/Timer";
import Auth from "./Pages/Authentication/Auth";
import { SettingContext } from "./context/settingContext";
import { PracticeContext } from "./context/contextPractice";
import ContextPractice from "./context/contextPractice";
import { SetMealRounded } from "@mui/icons-material";

function App() {
  const alanKey =
    "a3cc6f51ecf67f5aa246c0e8b0fbcf7d2e956eca572e1d8b807a3e2338fdd0dc/stage";

    const {handleDelete, handleUpdate} = useContext(PracticeContext);

  const [Todolist, setTodoList] = useState([]);
  const { setCurrentTimer, startTimer, pauseTimer } =
    useContext(SettingContext);

  // const [currentUserId, setCurrentUserId] = useState(null);

  const currentUserId = localStorage.getItem("userUid");
  // console.log('hilo',currentUserId);



  useEffect(() => {
    alanBtn({
      key: alanKey,
      onCommand: ({ command, todotask, deletetask, completed }) => {
        if (command === "open_todo") {
          window.location = `${window.location.origin}/todo`;
        }
        if (command === "start_timer") {
          startTimer();
        }
        if (command === "pause_timer") {
          pauseTimer();
        }
        if (command === "short_break") {
          setCurrentTimer("short");
        }
        if (command === "long_break") {
          setCurrentTimer("long");
        }
        if (command === "work") {
          setCurrentTimer("work");
        }
        if (command === "open_timer") {
          window.location = `${window.location.origin}/timer`;
        }
        if (command === "go_back") {
          window.history.back();
        }
        if (command === "log_out") {
          window.location = window.location.origin;
        }
        if (command === "check_task") {
        }
        if (command === "addatask") {
          const todoRef = firebase.database().ref(currentUserId).child("Task");
          const todo = {
            todotask,
            completed: false,
            type: "explorer",
          };
          todoRef.push(todo);
        }
        if (command === "deleteatask") {
          const todoRef = firebase.database().ref(currentUserId).child("Task");
          todoRef.on("value", (snap) => {
            const todos = snap.val();
            const todoList = [];
            for (let id in todos) {
              // if(type === todos[id].type){
              todoList.unshift({ id, ...todos[id] });
              // }
            }
            let itemId = todoList[wordsToNumbers(deletetask) - 1]
              ? todoList[wordsToNumbers(deletetask) - 1].id
              : null;
              handleDelete(currentUserId,itemId);
          });
        }
        if (command === "completetask") {
          const todoRef = firebase.database().ref(currentUserId).child("Task");
          todoRef.on("value", (snap) => {
            const todos = snap.val();
            const todoList = [];
            for (let id in todos) {
              // if(type === todos[id].type){
              todoList.unshift({ id, ...todos[id] });
              // }
            }
            let itemId = todoList[wordsToNumbers(completed) - 1]
              ? todoList[wordsToNumbers(completed) - 1].id
              : null;
              handleUpdate(currentUserId,itemId,false);
          });
        }
      },
    });
  }, []);
  return (
    <div className="App">
      <ContextPractice>
        <Routes>
          <Route path="/home" element={<Home />} />
          <Route path="/" element={<Auth />} />
          <Route
            path="/todo"
            element={<ToDo currentUserId={currentUserId} />}
          />
          <Route path="/timer" element={<Timer />} />
        </Routes>
      </ContextPractice>
    </div>
  );
}

export default App;
