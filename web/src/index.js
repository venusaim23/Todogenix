import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import "./index.css";
import SettingContextProvider from "./context/settingContext";
import App from "./App";
import ContextPractice from './context/contextPractice';

ReactDOM.render(
  <SettingContextProvider>
    <ContextPractice>
    <BrowserRouter>
      <App />
    </BrowserRouter>
    </ContextPractice>
  </SettingContextProvider>,
  document.getElementById("root")
);
