import React from 'react'

const Button = ({title, activeClass, _callback}) => {
    return (
        <div>
            <button style={{margin:'10px'}} className={`auth-button ${activeClass}`} onClick={_callback}>{title}</button>
        </div>
    )
}

export default Button;
