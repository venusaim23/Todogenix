import React, { useContext} from 'react';
import './Timer.css';
import { SettingContext } from '../../context/settingContext';
import Button from '../../components/TimerComponents/Button';
import CountDownAnim from '../../components/TimerComponents/countDownAnim';
import TimerSettings from '../../components/TimerComponents/TimerSettings';

const Timer = () => {

    const {pomodoro, executing, setCurrentTimer, settingBtn, children,
        startAnimate, startTimer, pauseTimer} = useContext(SettingContext);
    return (
        <div>
            {
                pomodoro === 0 ?(
                    <TimerSettings/>
                ):(
                 <div className='main-container'>
                  <ul className="labels">
                      <li>
                          <Button
                          title="work"
                          activeClass={executing.active === "work"?'active-label':undefined}
                          _callback={() => setCurrentTimer("work")}
                          />
                      </li>
                      <li>
                          <Button
                          title="Short Break"
                          activeClass={executing.active === "short"?'active-label':undefined}
                          _callback={() => setCurrentTimer("short")}
                          />
                      </li>
                      <li>
                          <Button
                          title="Long Break"
                          activeClass={executing.active === "long"?'active-label':undefined}
                          _callback={() => setCurrentTimer("long")}
                          />
                      </li>
                  </ul>
                  <Button title="Setting" _callback={settingBtn} />
                  <div className="time-container">
                     <div className="time-wrapper">
                       <CountDownAnim
                       key={pomodoro}
                       timer={pomodoro}
                         animate={startAnimate}
                       >
                           {children}
                       </CountDownAnim>
                     </div>
                  </div>
                  <div className="button-wrapper">
                    <Button title="Start" className={!startAnimate && 'active'} _callback={startTimer} />
                    <Button title="Pause" className={startAnimate && 'active'} _callback={pauseTimer} />
                  </div>
                 </div>
                )
            }
        </div>
    )
}

export default Timer
