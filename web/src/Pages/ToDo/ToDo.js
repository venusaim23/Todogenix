import React, {useState} from 'react';
import './ToDo.css';
import Explorer from '../Explorer/explorer';
import Flexible from '../Flexible/flexible';
import Strict from'../Strict/strict';
import firebase from 'firebase';

const ToDo = ({complete, isDelete}) => {
 
    const [type, setType] = useState('explorer');
    const [loading,setLoading] = useState(true);

    const [currentUserId,setCurrentUserId] = useState(null);


    firebase.auth().onAuthStateChanged(function(user) {
        if (user != null) {
            setCurrentUserId(user.uid);
          setLoading(false);
        }
      });


    const handleType = (type) => {
          setType(type);
    }

    if(loading){
        return <h1>loading...</h1>
    }

    return (
        <div>
            <div className="todo-nav">
            <div className='nav'>
                <h1 style={{color:'#ffffff'}} >Todogenix</h1>
               <button className='log-out--button' onClick={() => window.location = '/'}>Log Out</button>
                </div>
               <div className='todo-category'>
               <h2 style={{borderBottom:`${type === 'explorer'?"6px solid #FAEDF0":"6px solid transparent"}`}} className='explore' onClick={() => handleType('explorer')}>Explorer</h2>
               <h2 style={{borderBottom:`${type === 'flexible'?"6px solid #FAEDF0":"6px solid transparent"}`}}  className='flexible'onClick={() => handleType('flexible')}>Flexible</h2>
               <h2 style={{borderBottom:`${type === 'strict'?"6px solid #FAEDF0":"6px solid transparent"}`}} className='strict' onClick={() => handleType('strict')}>Strict</h2>
               </div>
            </div>
            {
                type === 'explorer'? <Explorer type={type} currentUserId={currentUserId}/>
                :(type=== 'flexible'?<Flexible type={type} currentUserId={currentUserId} />:
                <Strict type={type} currentUserId={currentUserId}/>)
            }
          
        </div>
    )
}

export default ToDo
