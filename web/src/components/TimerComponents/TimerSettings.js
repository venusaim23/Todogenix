import React,{useState, useContext} from 'react';
import Button from './Button';
import { SettingContext } from '../../context/settingContext';
import './Timer.css';

const TimerSettings = () => {

    const {updateExecute} = useContext(SettingContext);
 
    const [newTimer, setNewTimer] = useState({
        work:0.1,
        short:0.4,
        long:1,
        active:'work'
    });

    const handleChange = (input) => {
      const {name, value} = input.target;
      switch (name) {
          case 'work':
              setNewTimer({
                  ...newTimer,
                  work:parseInt(value)
              })
              break;
              case 'short':
              setNewTimer({
                  ...newTimer,
                  short:parseInt(value)
              })
              break;
              case 'long':
              setNewTimer({
                  ...newTimer,
                  long:parseInt(value)
              })
              break;
      
          default:
              break;
      }
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        updateExecute(newTimer);
    }

    return (
        <div className="form-container">
          <form noValidate className='inner-form-container'>
              <h1>Pomodoro</h1>
              <div className="input-wrapper">
                <input className="authInput" name="work" onChange={handleChange} placeholder='Work Duration' />
                <input className="authInput" name="short" onChange={handleChange} placeholder='Short Break'  />
                <input className="authInput" name="long" onChange={handleChange} placeholder='Long Break'  />
              </div>
              <Button title="Set Timer" _callback={handleSubmit} />
          </form>  
        </div>
    )
}

export default TimerSettings;
