import React from 'react';
import HomeCard from '../../components/HomeCard/HomeCard';
import './Home.css';
import { CardData } from '../../Data/CardData';
import { Link } from 'react-router-dom';
import QuoteImg from '../../Images/quote_icon.png';

const Home = () => {
     
    const userName = localStorage.getItem('name');

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
      }

    return (
        <div>
            <div className="home-text--container">
            <div className="todo-nav">
                <div className='nav'>
                <h1 style={{color:'#ffffff'}} >Todogenix</h1>
               <button className='log-out--button' onClick={() => window.location = '/'}>Log Out</button>
                </div>   
               </div>
            <h1 style={{marginTop:'20px',marginBottom:0}}>Good Morning, Mayank</h1>
            </div>
            {/* <div className="weather">
                <div className='top-weather-div'>
                <img style={{width:'30px',height:'30px',margin:0}} src="https://raw.githubusercontent.com/venusaim23/Todogenix/main/Android/Assets/rainy_day.png"  alt="weather-icon"/>
                  <h1>30°C</h1>
                </div>
                <p>Cloudy</p>
                <p>Budaun</p>
            </div> */}
             <div className="home-card--container">
                 {
                     CardData.map(ele => {
                         return (
                             <Link to={`/${ele.link}`} style={{textDecoration:'none'}}>
                               <HomeCard
                                  title={ele.title}
                                  descrip={ele.descrip}
                                  img={ele.image}
                                  borderTop={ele.borderTop}
                             />                             
                             </Link>
                         )
                     })
                 }
             </div>
             <div className="home-quote--container">
                 <img style={{width:'65px'}} src={QuoteImg} alt="quote"/>
                 <div className='quote-text--container'>
                     <h2>Quote of the day</h2>
                     <h3>“If you want to fly, you have to give up everything that weighs you down.”</h3>
                 </div>
            </div>
            
        </div>
    )
}

export default Home;