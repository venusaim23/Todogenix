import React from 'react';
import './explorer.css';
import Dialog from '../../components/Dialog/Dialog';

const Explorer = ({type, currentUserId}) => {
    return (
        <div>
           <Dialog type={type} currentUserId={currentUserId} />
        </div>
    )
}

export default Explorer
