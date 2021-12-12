import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import "./index.css";
import SettingContextProvider from "./context/settingContext";
import App from "./App";

ReactDOM.render(
  <SettingContextProvider>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </SettingContextProvider>,
  document.getElementById("root")
);
