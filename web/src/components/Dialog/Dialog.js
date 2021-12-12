import React, { useState, useEffect } from "react";
import "./Dialog.css";
import ListItem from "../../components/ListItem/ListItem";
import firebase from "../../firebase";

const Dialog = ({ type, currentUserId }) => {
  const [click, setClick] = useState(false);
  const [field, setField] = useState("");
  const [TodoList, setTodoList] = useState([]);

 

  const handleSubmit = (event) => {
    event.preventDefault();
    setField(event.target[0].value);
    if (type === "explorer") {
      addOrEdit(event.target[0].value);
    }else{
      addOrEdit(event.target[0].value,event.target[1].value,event.target[2].value,event.target[3].value);
    }

    setField("");
  };

  const handleChange = (event) => {
    setField(event.target.value);
  };

  const onClick = () => {
    setClick(false);
  };
  const handleCancel = () => {
    setClick(false);
    if (click === true) setField("");
  };

  useEffect(() => {
    const todoRef = firebase
      .database()
      .ref(currentUserId)
      .child("Task");
    todoRef.on("value", (snap) => {
      const todos = snap.val();
      const todoList = [];
      for (let id in todos) {
    
        if(type === todos[id].type){
          todoList.push({ id, ...todos[id] });
        }      
      }
      setTodoList(todoList);
    });
  }, []);

  const addOrEdit = (title,date,time,duration) => {
    const todoRef = firebase
      .database()
      .ref(currentUserId)
      .child("Task");
   let todo1 = {}; 
   
  if(date && time){
       const todo ={
          title,
          completed: false,
          type: type,
          date,
          time,
          duration,
          timestamp: Date.now()
        }
        todo1 = todo
      }else{
        const todo = {
          title,
          completed: false,
          type: type,
        }
        todo1 = todo
      }
    
    todoRef.push(todo1);
  };
   const editTask = (edit) => {
    //  setClick(edit);
    // const todoRef = firebase.database()
    // .ref(currentUserId).child("Task").child(todo.id);
   };
  return (
    <div style={{ overflow: "hidden" }}>
      <div className="add-item">
        {TodoList.length !== 0 ? (
          TodoList.slice(0)
            .reverse()
            .map((ele, index) => {
              return ele.title || ele.todotask ? (
                <ListItem key={index} editTask={editTask} type={type} currentUserId={currentUserId} todoNum={index} todo={ele} />
              ) : null;
            })
        ) : (
          <h2 className="empty-list">Add something in your to do list.</h2>
        )}
        <button
          className="open-button"
          onClick={() => (click ? setClick(false) : setClick(true))}
        >
          +
        </button>
        <div
          style={{
            transform: `${
              click ? "translate(420px,52px)" :type==='explorer'? "translate(790px,352px)":"translate(900px,500px)"
            }`,
          }}
          className={`add-item-container ${type === 'explorer'?'add-item-container-explore':null}`}
        >
          <form onSubmit={handleSubmit}>
            <h2 className="form-h2">Add a task</h2>
            <p className="form-para">
              Add something to start a new task sbsj hskkk hvhb.
            </p>
            <input
              className="authInput  auth-input--modify"
              value={field}
              onChange={handleChange}
              type="text"
              placeholder="Add Task"
            />
            {
              type !== "explorer"?(
                <>
                <input className="authInput" type="date" id="start" name="trip-start" />
                <input style={{margin:' 0 10px'}} className="authInput" type="time" id="appt" name="appt" />
                <input style={{width:'90%'}} className="authInput" type="text"  placeholder="Duration" />
                </>
              ):null
            }
            <div
              className="form-button form-div"
              onClick={() => handleCancel()}
            >
              cancel
            </div>
            <button
            style={{padding:'10px'}}
              className="form-button"
              onClick={() => onClick()}
              type="submit"
            >
              Add
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Dialog;
