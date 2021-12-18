import React from 'react';
import './HomeCard.css';

const HomeCard = (props) => {
    return (
        <div style={{borderTop:props.borderTop}} className="homeCard-container">
            <div className="homeCard-left--container">
                <h2>{props.title}</h2>
                <p>{props.descrip}</p>
            </div>
            <img src={props.img} alt="todo"/>
        </div>
    )
}

export default HomeCard;
