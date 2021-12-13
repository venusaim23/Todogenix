import React, { useContext } from "react";
import "./ListItem.css";
import Checkbox from "@mui/material/Checkbox";
import DeleteIcon from '@mui/icons-material/Delete';
import firebase from '../../firebase';
import EditIcon from '@mui/icons-material/Edit';
import { PracticeContext } from "../../context/contextPractice";

const ListItem = (props) => {
  
   const {handleUpdate, handleDelete} = useContext(PracticeContext);
  

  return (
    <div  className="list-item--container">
      <Checkbox
        style = {{width:'65px',marginRight:"20px"}}
        checked={props.todo.completed}
        onChange={() => handleUpdate(props.currentUserId, props.todo.id,props.todo.completed )}
        inputProps={{ "aria-label": "controlled" }}
      />
      <div className="todo-list-div">
      <h3 style={{textDecoration:`${props.todo.completed?'line-through':''}`,textDecorationThickness:'3px'}} >{parseInt(props.todoNum) +1}. {props.todo.title || props.todo.todotask}</h3>
      {
        props.type !== 'explorer'? <p>Due: {props.todo.time} {props.todo.date} {props.todo.duration} hr</p>:null
      }
      </div>
      <div>
        <div className="icon-div" onClick={() => handleDelete(props.currentUserId, props.todo.id)}><DeleteIcon /></div>  
        {
          props.type !== 'strict'?(
                   <div className="icon-div" onClick={() => props.editTask(true)}><EditIcon /></div>
          ):null
        }
            
      </div>
    </div>
  );
};

export default ListItem;
