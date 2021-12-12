import React from 'react';
import './strict.css';
import Dialog from '../../components/Dialog/Dialog';

const Strict = ({type, currentUserId}) => {
    return (
        <div>
            <Dialog type={type} currentUserId={currentUserId} />
        </div>
    )
}

export default Strict;
