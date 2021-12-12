import "./App.css";
import { useState, useEffect, useContext } from "react";
import { Route, Routes } from "react-router-dom";
import Home from "./Pages/Home/Home";
import ToDo from "./Pages/ToDo/ToDo";
import alanBtn from "@alan-ai/alan-sdk-web";
import firebase from "./firebase";
import wordsToNumbers from 'words-to-numbers';
import Timer from './Pages/Timer/Timer';
import Auth from "./Pages/Authentication/Auth";
import { SettingContext } from './context/settingContext';
function App() {
  const alanKey ="a3cc6f51ecf67f5aa246c0e8b0fbcf7d2e956eca572e1d8b807a3e2338fdd0dc/stage";

  const [Todolist, setTodoList] = useState([]);
  const [currentUserId,setCurrentUserId] = useState('');

  const {setCurrentTimer, startTimer, pauseTimer} = useContext(SettingContext);

 
  useEffect(() => {
    const todoRef = firebase.database().ref("Todo");
    todoRef.on('value',(snap) => {
      const todos = snap.val();
      const todoList = [];
      for(let id in todos){
        todoList.push({id, ...todos[id]}); 
      }
      setTodoList(todoList);
    });
   }, [])

 


  useEffect(() => {
    alanBtn({
      key: alanKey,
      onCommand: ({ command, todotask, deletetask }) => {
        if (command === "open_todo") {
          window.location = `${window.location.origin}/todo`;
        }if(command === 'start_timer'){
          startTimer();
        }
        if(command ==='pause_timer'){
          pauseTimer();
        }
        if(command === "short_break"){
          setCurrentTimer("short")
        }if(command === "long_break"){
          setCurrentTimer("long")
        }if(command === "work"){
          setCurrentTimer("work")
        }
        if(command === "open_timer"){
          window.location = `${window.location.origin}/timer`;
        }
        if (command === "go_back") {
          window.history.back();
        }
        if(command === 'log_out'){
          window.location = window.location.origin;
        }
        if(command === 'check_task'){

        }
        if (command === "addatask") {
          const todoRef = firebase.database().ref("zCzY8ufAWFcgTnlkhZYmw6qwpuD2")
      .child("Task");
          const todo = {
            todotask,
            completed: false,
            type:'explorer'
          };
          todoRef.push(todo);
        }
        if(command === "deleteatask"){
 let itemId = Todolist[wordsToNumbers(deletetask) - 1]?Todolist[wordsToNumbers(deletetask) - 1].id:null;
          if(itemId){
            const todoRef = firebase.database().ref(currentUserId)
            .child("Task");
            todoRef.remove();
          }else{
            alert('something is wrong, please try again');
          }
         
        }
        if(command === "completetask"){
          const todoRef = firebase.database().ref("zCzY8ufAWFcgTnlkhZYmw6qwpuD2")
    .child("Task").child("-MqiVMqoFPBVOyA4fdYQ");
    todoRef.update({
      completed:true
    });
         
        }
      },
    });
  }, []);

  return (
    <div className="App">
     
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/" element={<Auth setCurrentUserId={setCurrentUserId} />} />
        <Route path="/todo" element={<ToDo currentUserId={currentUserId} />} />
        <Route path="/timer" element={<Timer />} />
      </Routes>
    </div>
  );
}

export default App;
