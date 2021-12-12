import React,{ createContext, useState} from 'react';

export const SettingContext = createContext();

const SettingContextProvider = (props) => {

const [pomodoro, setPomodoro] = useState(0);
const [executing, setExecuting] = useState({});
const [startAnimate, setStartAnimate] = useState(false);

function setCurrentTimer(active_state){
    updateExecute({
        ...executing,
        active:active_state

    })
}

function startTimer(){
    setStartAnimate(true);
}

function pauseTimer(){
    setStartAnimate(false);
}

function stopTimer(){
    setStartAnimate(false);
}
const settingBtn = () => {
    setExecuting({});
    setPomodoro(0);
}

function updateExecute(upadteSettings){
    setExecuting(upadteSettings);
    setTimerTime(upadteSettings);
}

function setTimerTime(evalute){
    switch (evalute.active) {
        case 'work':
            setPomodoro(evalute.work)
            break;
             case 'short':
             setPomodoro(evalute.short)
             break;
              case 'long':
              setPomodoro(evalute.long)
              break;
    
        default:
            setPomodoro(0);
            break;
    }
}

const children = ({remainingTime}) => {
    const minutes = Math.floor(remainingTime/60);
    const seconds = remainingTime % 60;
    return `${minutes}:${seconds}`
}

    return (
        <SettingContext.Provider value={{
            stopTimer,
            updateExecute,
            pomodoro,
            executing,
            startAnimate,
            setCurrentTimer,
            startTimer,
            pauseTimer,
            settingBtn,
            children
            }}>
            {props.children}
        </SettingContext.Provider>
    )
}

export default SettingContextProvider;
