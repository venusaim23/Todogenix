import React from 'react';
import './flexible.css';
import Dialog from '../../components/Dialog/Dialog';

const Flexible = ({type, currentUserId}) => {
    return (
        <div>
            <Dialog type={type} currentUserId={currentUserId}/>
        </div>
    )
}

export default Flexible;
