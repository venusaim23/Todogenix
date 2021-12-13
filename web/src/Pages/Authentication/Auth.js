import React, { useContext, useState } from "react";
import { auth } from "../../firebase";
import './Auth.css';
import logoForBig from '../../Images/tg_shadow_notext.svg';
import './Auth.css';
import {PracticeContext} from '../../context/contextPractice';

const Auth = () => {
  const [signup, setSignup] = useState(true);

  const {handleLoginSubmit, handleSignUpSubmit} = useContext(PracticeContext);

  

  return (
    <div className="main-auth--container">
      {signup ? (
        <div className="sign-up">
          <div className="sign-up--div">
          <img className="sign-up-img" src={logoForBig} alt="todo"/>
          <form onSubmit={handleSignUpSubmit} className="auth-form">
          <input className="authInput" type="type" placeholder="Name" required />
            <input className="authInput" type="email" placeholder="Email" required />
            <input className="authInput" type="password" placeholder="Password" required />
            <input className="authInput" type="type" placeholder="Location" required />
            <button className="auth-button" type="submit">Sign Up</button>
                      
    
          </form>
          <p className="para-auth">
            Already have an account?{" "}
            <button className="para-auth--button" onClick={() => setSignup(false)}>Login</button>
          </p>
          </div>
        </div>
      ) : (
        <div className="sign-up">
           <div className="sign-up--div">
          <img className="sign-up-img" src={logoForBig} alt="todo"/>
          <form onSubmit={handleLoginSubmit} className="auth-form">
            <input className="authInput" type="email" placeholder="Email" required />
            <input className="authInput" type="password" placeholder="Password" required />                           
            <button className="auth-button" type="submit">Login</button>

          </form>
          <p className="para-auth">
            Don't have an accout yet?{" "}
            <button className="para-auth--button" onClick={() => setSignup(true)}>Sign up</button>
          </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default Auth;
