import React,{ useContext } from 'react';
import { CountdownCircleTimer } from 'react-countdown-circle-timer';
import { SettingContext } from '../../context/settingContext';

const CountDownAnim = ({key=1, timer=20, animate=true, children}) => {

    const {stopTimer} = useContext(SettingContext);

    return (
        <CountdownCircleTimer
        key={key}
    isPlaying={animate}
    duration={timer*60}
    colors={[
      ['#292C6D', 0.33],
      ['#519259', 0.33],
      ['#DD4A48', 0.33],
    ]}
    strokeWidth={10}
    size={200}
    trailColor="#ffffff"
    onComplete={() => stopTimer()}
  >
    {children}
  </CountdownCircleTimer>
    )
}

export default CountDownAnim;
